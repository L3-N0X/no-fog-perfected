# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A **client-side** Minecraft mod (Kotlin) that removes/adjusts fog. One codebase builds **7 artifacts**: Minecraft 1.21.11, 26.1.2 and 26.2 × Fabric and NeoForge, plus 26.3 Fabric (26.3 NeoForge is waiting on a NeoForge release). User-facing behaviour and commands are documented in `README.md`.

## Build system: Gradle + Stonecutter

`settings.gradle.kts` uses [Stonecutter](https://stonecutter.kikugie.dev) to create one subproject per version/loader combo (`versions/<mcVersion>-<loader>/`), all driven by the single root `build.gradle.kts`. `gradle.properties` sets `dev.kikugie.stonecutter.hard_mode=true`.

```bash
./gradlew build                    # build all version/loader combos
./gradlew :26.2-fabric:build       # build one combo
./gradlew :26.2-fabric:runClient   # launch a dev client for that combo
./gradlew publishMods              # publish all versions (needs MODRINTH_TOKEN)
```

There are **no tests** in this repo; CI (`.github/workflows/build.yml`) just runs `./gradlew build` on JDK 25. Releases are cut by pushing a `v*` / `*.*.*` tag (`.github/workflows/release.yml`).

The **active version** for IDE resolution is set in `stonecutter.gradle.kts` (`stonecutter active "26.3-fabric"`). Source files on disk are chiseled in place for the active version — the comment/uncomment state of `//? if ...` blocks in the working tree reflects whichever version is active, so a `git diff` after switching versions is expected noise, not a real change.

### Version-conditional code

Sources are preprocessed with Stonecutter comment directives. Inactive branches are stored as `/*...*/` comments:

```kotlin
//? if >=26.1 {
@Inject(method = ["setupFog"], at = [At("RETURN")])
//?}
//? if <26.1 {
/*@Inject(method = ["setupFog"], at = [At(value = "INVOKE", ...)])
*///?}
```

`build.gradle.kts` registers `stonecutter.constants.match(loader, "fabric", "neoforge")`, so `//? if fabric {` / `//? if neoforge {` also work. `NoFogFabricCommands.kt` lives in `src/main` but is wrapped entirely in `//? if fabric {` — i.e. **shared source can still be loader-specific**.

## Source layout

Source sets are wired manually in `build.gradle.kts` (there are no per-version source dirs):

- `src/main/kotlin` — always compiled. `NoFogConfig` (GSON-backed `config/no-fog.json`), `FogSliderFormulas`, `NoFogCommandFormatting`, `NoFogSodiumConfig`, the mixin, and `NoFogFabricCommands` (fabric-gated).
- `src/fabric/**` — added to `main` for `*-fabric` projects: `NoFogClient` (`ClientModInitializer`), `fabric.mod.json`, mixin config.
- `src/neoforge/**` — added to `main` for `*-neoforge` projects: `NoFogNeoForge` (`@Mod` + `@EventBusSubscriber`), `neoforge.mods.toml`, mixin config.

Manifests are templated through `processResources` (`${version}`, `${minecraft_dependency}`, `${loader_version}`, `${neoforge_version_range}`, …); the values live in the `fabricVersions` / `neoforgeVersions` `when (mcVersion)` blocks in `build.gradle.kts`. **Adding a Minecraft version means adding a branch to every one of those `when` blocks** (fabric deps, neoforge deps, `minecraftDependency`, `javaVersion`, `kotlinJvmTarget`, `JavaCompile` release) plus a `version(...)` line in `settings.gradle.kts`.

Per-version quirks already encoded there: 1.21.11 uses `fabric-loom` with Mojang mappings and `modImplementation`/`remapJar`; 26.1.2+ uses `net.fabricmc.fabric-loom` with plain `implementation` and a non-remapped `jar`. NeoForge versions newer than the latest Kotlin-for-Forge release need KFF repackaged (`kffNeedsPatch` → `unzipKff` → `patchKotlinForForge`) to widen its `versionRange`.

### 26.3 (NeoForge pending)

`26.3-fabric` targets the 26.3 release. NeoForge has published nothing for 26.3 yet (only NeoForm), so `26.3-neoforge` stays commented out in `settings.gradle.kts` and its `neoforgeVersions` branch holds placeholder numbers.

Kotlin for Forge 6.3.0 declares Minecraft `[1.21.9,26.3)`, so `kffNeedsPatch` in `build.gradle.kts` routes NeoForge 26.3 through the `unzipKff` → `patchKotlinForForge` repackaging. Remove 26.3 from it once a KFF release covers 26.3.

Sodium's config API is compiled per version (`sodiumApiVersion`): `0.9.2+mc26.2` / `0.9.2+mc26.3`, and `0.9.2+mc26.1.2` for 26.1.2 and 1.21.11 (Sodium's 1.21.11 API artifact is intermediary-named). The 0.8 → 0.9 API change was purely additive.

Watch out for semver when a pre-release is in the version list: `26.3-pre-1` sorts **below** `26.3`, so `//? if >=26.3 {` is false on it — write `//? if >=26.3-` instead.

The 26.3 rendering refactor moved `com.mojang.blaze3d.buffers` to `com.mojang.renderpearl.api.buffers`, but `FogRenderer.setupFog`, `FogRenderer.getFogType` and every `FogData` field this mod uses are unchanged, so the mixin needed no version-conditional branch.

## Architecture

**All fog behaviour is one mixin**: `MixinFogRenderer` injects into `FogRenderer.setupFog` (priority 900) and mutates the returned/local `FogData`. `modifyFogImpl` decides per frame:

1. Pick the relevant setting from `FogType` (WATER/LAVA/POWDER_SNOW) or `level.dimension()` (overworld/nether/end).
2. If fog is enabled and no offset applies, return untouched.
3. Blindness/darkness (checked on `LocalPlayer` effects) short-circuit before the fluid/dimension paths.
4. Otherwise apply an additive offset (fluids, nether), a multiplier (overworld, end), or push all four `FogData` distances to `1e9f` to disable fog entirely — this is what avoids the sky cut-off other mods have.

**Offsets only apply when that fog type is enabled**; disabled fog is never rendered regardless of slider.

**Slider ↔ internal value conversion lives solely in `FogSliderFormulas`** — 0–100 UI sliders map non-linearly (squared for offsets, quartic for dimension multipliers) onto distances/multipliers. Both the Sodium GUI and the chat commands must go through it so the two UIs agree; `NoFogConfig` stores the *converted* values, not slider positions.

**Two config front-ends, one state object:**
- Sodium GUI: `NoFogSodiumConfig` implements Sodium's `ConfigEntryPoint`, registered via the `sodium:config_api_user` entrypoint (`fabric.mod.json`) / `[modproperties]` key (`neoforge.mods.toml`). Sodium is `compileOnly` — the class must never be touched when Sodium is absent. API reference: `net/caffeinemc/mods/sodium/api/config/USAGE.md`.
- Chat commands: registered separately per loader (`NoFogFabricCommands` via `ClientCommandRegistrationCallback`, the command tree inside `NoFogNeoForge` via `RegisterClientCommandsEvent`). **These two command trees are duplicated by hand** — a change to one (new subcommand, new fog type) must be mirrored in the other; only `NoFogCommandFormatting` and `FogSliderFormulas` are shared.

Adding a new fog setting therefore touches: `NoFogConfig` (field + `ConfigData` + load/save), `FogSliderFormulas` (if it has a slider), `MixinFogRenderer`, `NoFogSodiumConfig`, and *both* command trees.
