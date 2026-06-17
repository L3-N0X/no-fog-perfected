import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.kotlin.dsl.getByType

plugins {
    id("idea")
    id("fabric-loom") version "1.17-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT" apply false
    id("net.neoforged.moddev") version "2.0.141" apply false
    id("maven-publish")
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
    id("org.jetbrains.kotlin.jvm") version "2.4.0"
    id("com.google.devtools.ksp") version "2.3.6"
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.23" apply false
    id("dev.kikugie.fletching-table.neoforge") version "0.1.0-alpha.23" apply false
}

val modVersion: String = providers.gradleProperty("mod_version").get()
val mavenGroup: String = providers.gradleProperty("maven_group").get()

version = modVersion
group = mavenGroup

// val isFabric = stonecutter.current.project.endsWith("-fabric")
// val isNeoForge = stonecutter.current.project.endsWith("-neoforge")
// val mcVersion = stonecutter.current.version
val isFabric = project.name.endsWith("-fabric")
val isNeoForge = project.name.endsWith("-neoforge")
val mcVersion = stonecutter.current.version  // This one is fine, version is global state

println("[${project.rootDir}] ${project.version} - Minecraft Version is $mcVersion")

stonecutter {
    val loader = when {
        project.name.endsWith("-fabric") -> "fabric"
        project.name.endsWith("-neoforge") -> "neoforge"
        else -> error("Unknown loader for project ${project.name}")
    }
    constants.match(loader, "fabric", "neoforge")
}

sourceSets {
    main {
        if (isFabric) {
        	kotlin.srcDir(rootProject.file("src/fabric/kotlin"))
        	resources.srcDir(rootProject.file("src/fabric/resources"))
        } else
        if (isNeoForge) {
            kotlin.srcDir(rootProject.file("src/neoforge/kotlin"))
            resources.srcDir(rootProject.file("src/neoforge/resources"))
        }
    }
}

if (isFabric) {
    apply(
        plugin = if (mcVersion == "1.21.11") {
            "fabric-loom"
        } else {
            "net.fabricmc.fabric-loom"
        }
    )
    apply(plugin = "dev.kikugie.fletching-table.fabric")
} else if (isNeoForge) {
    apply(plugin = "net.neoforged.moddev")
}

data class FabricVersionSet(
    val loader: String,
    val fabricApi: String,
    val fabricKotlin: String
)

val fabricVersions = when (mcVersion) {
    "1.21.11" -> FabricVersionSet(
        loader = "0.18.6",
        fabricApi = "0.141.3+1.21.11",
        fabricKotlin = "1.13.5+kotlin.2.2.10"
    )

    "26.1.2" -> FabricVersionSet(
        loader = "0.18.6",
        fabricApi = "0.152.1+26.1.2",
        fabricKotlin = "1.13.10+kotlin.2.3.20"
    )

    "26.2" -> FabricVersionSet(
        loader = "0.19.3",
        fabricApi = "0.152.1+26.2",
        fabricKotlin = "1.13.12+kotlin.2.4.0"
    )

    else -> error("No Fabric versions configured for $mcVersion")
}

data class NeoForgeVersionSet(
    val kotlinforforge: String,
    val neoforgeVersion: String,
    val neoforgeVersionRange: String,
    val minecraftVersionRange: String
)

val neoforgeVersions = when (mcVersion) {
    "1.21.11" -> NeoForgeVersionSet(
        kotlinforforge = "6.2.0",
        neoforgeVersion = "21.11.42",
        neoforgeVersionRange = "[21.11,)",
        minecraftVersionRange = "[1.21.11,)"
    )

    "26.1.2" -> NeoForgeVersionSet(
        kotlinforforge = "6.2.0",
        neoforgeVersion = "26.1.2.76",
        neoforgeVersionRange = "[26.1,)",
        minecraftVersionRange = "[26.1,)"
    )

    "26.2" -> NeoForgeVersionSet(
        kotlinforforge = "6.2.0",
        neoforgeVersion = "26.2.0.0-beta",
        neoforgeVersionRange = "[26.2.0.0-beta,)",
        minecraftVersionRange = "[26.2,)"
    )

    else -> error("No NeoForge versions configured for $mcVersion")
}

repositories {
    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
    maven {
        name = "CaffeineMC"
        url = uri("https://maven.caffeinemc.net/releases")
    }
}

if (isFabric) {
    configure<net.fabricmc.loom.api.LoomGradleExtensionAPI> {
        mods {
            create("no-fog-perfected") {
                sourceSet(sourceSets.main.get())
            }
        }
    }
} else if (isNeoForge) {
    configure<net.neoforged.moddevgradle.dsl.NeoForgeExtension> {
        version = neoforgeVersions.neoforgeVersion
        runs {
            create("client") {
                client()
            }
        }
        mods {
            create("no_fog_perfected") {
                sourceSet(sourceSets.main.get())
            }
        }
    }
}

val patchedKff = configurations.create("patchedKff")

val unzipKff = tasks.register<Copy>("unzipKff") {
    val jarFile = provider { patchedKff.files.firstOrNull() }
    dependsOn(patchedKff)
    
    from(provider { jarFile.get()?.let { zipTree(it) } ?: files() })
    into(layout.buildDirectory.dir("tmp/unzipKff"))
    
    doLast {
        val tomlFile = layout.buildDirectory.file("tmp/unzipKff/META-INF/neoforge.mods.toml").get().asFile
        if (tomlFile.exists()) {
            var content = tomlFile.readText()
            content = content.replace("versionRange=\"[1.21.9,26.2)\"", "versionRange=\"[1.21.9,)\"")
            tomlFile.writeText(content)
        }
    }
}

val patchKotlinForForge = tasks.register<Zip>("patchKotlinForForge") {
    dependsOn(unzipKff)
    from(layout.buildDirectory.dir("tmp/unzipKff"))
    archiveFileName.set("kffmod-neoforge-patched.jar")
    destinationDirectory.set(layout.buildDirectory.dir("libs"))
}

dependencies {
    if (isNeoForge && mcVersion == "26.2") {
        "patchedKff"("thedarkcolour:kffmod-neoforge:${neoforgeVersions.kotlinforforge}")
    }
    if (isFabric) {
        add("minecraft", "com.mojang:minecraft:$mcVersion")

        if (mcVersion == "1.21.11") {
            add("mappings", project.extensions.getByType<LoomGradleExtensionAPI>().officialMojangMappings())

            add("modImplementation", "net.fabricmc:fabric-loader:${fabricVersions.loader}")
            add("modImplementation", "net.fabricmc.fabric-api:fabric-api:${fabricVersions.fabricApi}")
            add("modImplementation", "net.fabricmc:fabric-language-kotlin:${fabricVersions.fabricKotlin}")
        } else {
            add("implementation", "net.fabricmc:fabric-loader:${fabricVersions.loader}")
            add("implementation", "net.fabricmc.fabric-api:fabric-api:${fabricVersions.fabricApi}")
            add("implementation", "net.fabricmc:fabric-language-kotlin:${fabricVersions.fabricKotlin}")
        }
        add("compileOnly", "net.caffeinemc:sodium-fabric-api:0.8.12+mc26.1.2")
    } else if (isNeoForge) {
        add("compileOnly", "net.caffeinemc:sodium-neoforge-api:0.8.12+mc26.1.2")
        if (mcVersion == "26.2") {
            val kff = dependencies.create("thedarkcolour:kotlinforforge-neoforge:${neoforgeVersions.kotlinforforge}") as ModuleDependency
            kff.exclude(mapOf("group" to "thedarkcolour", "module" to "kffmod-neoforge"))
            "implementation"(kff)
            "implementation"(files(patchKotlinForForge.map { it.archiveFile.get() }))
        } else {
            "implementation"("thedarkcolour:kotlinforforge-neoforge:${neoforgeVersions.kotlinforforge}")
        }
    }
}

val minecraftDependency = when {
    mcVersion == "1.21.11" -> ">=1.21.11 <1.22"
    mcVersion.startsWith("26.1") -> ">=26.1 <26.2"
    mcVersion.startsWith("26.2") -> ">=26.2 <26.3"
    else -> error("No minecraft dependency range configured for $mcVersion")
}

tasks.withType<ProcessResources>().configureEach {
    inputs.property("version", project.version)
    inputs.property("minecraft_dependency", minecraftDependency)

    if (isFabric) {
        inputs.property("loader_version", fabricVersions.loader)
        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version,
                "minecraft_dependency" to minecraftDependency,
                "loader_version" to fabricVersions.loader
            )
        }
    } else if (isNeoForge) {
        inputs.property("neoforge_version_range", neoforgeVersions.neoforgeVersionRange)
        inputs.property("minecraft_version_range", neoforgeVersions.minecraftVersionRange)
        inputs.property("kotlinforforge_version", neoforgeVersions.kotlinforforge)
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(
                "version" to project.version,
                "neoforge_version_range" to neoforgeVersions.neoforgeVersionRange,
                "minecraft_version_range" to neoforgeVersions.minecraftVersionRange,
                "kotlinforforge_version" to neoforgeVersions.kotlinforforge
            )
        }
    }
}

val javaVersion = when {
    mcVersion == "1.21.11" -> JavaVersion.VERSION_21
    mcVersion.startsWith("26.") -> JavaVersion.VERSION_25
    else -> error("No Java version configured for $mcVersion")
}

val kotlinJvmTarget = when {
    mcVersion == "1.21.11" -> org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
    mcVersion.startsWith("26.") -> org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25
    else -> error("No Kotlin JVM target configured for $mcVersion")
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(
        when {
            mcVersion == "1.21.11" -> 21
            mcVersion.startsWith("26.") -> 25
            else -> error("No Java release configured for $mcVersion")
        }
    )
}

kotlin {
    compilerOptions {
        jvmTarget.set(kotlinJvmTarget)
    }
}


java {
    withSourcesJar()
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks.named<Jar>("jar").configure {
    inputs.property("projectName", project.name)
    from("LICENSE") {
        rename { "${it}_${project.name}" }
    }
}

publishMods {
    // 1. Set the release file based on the active loader
    if (isFabric) {
        if (mcVersion == "1.21.11") {
            file.set(tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar").get().archiveFile)
        } else {
            file.set(tasks.named<Jar>("jar").get().archiveFile)
        }
        modLoaders.add("fabric")
    } else if (isNeoForge) {
        file.set(tasks.named<Jar>("jar").get().archiveFile)
        modLoaders.add("neoforge")
    }

    // 2. Read the changelog file from your root project folder
    val changelogFile = rootProject.file("CHANGELOG.md")
    if (changelogFile.exists()) {
        changelog.set(changelogFile.readText())
    } else {
        changelog.set("No changelog provided for this release.")
    }

    // Set release type (STABLE, BETA, or ALPHA)
    type.set(me.modmuss50.mpp.ReleaseType.STABLE)

    // 3. Modrinth Configuration
    val modrinthToken = providers.environmentVariable("MODRINTH_TOKEN")
    if (modrinthToken.isPresent) {
        modrinth {
            accessToken.set(modrinthToken)
            projectId.set("Lgo2vvWd") // Replace with your actual project ID
            minecraftVersions.add(mcVersion)

            // Adapt dependencies based on the loader
            if (isFabric) {
                requires("fabric-api")
            }
        }
    }

    // 4. CurseForge Configuration (Optional, if you also want to publish there)
    val curseforgeToken = providers.environmentVariable("CURSEFORGE_TOKEN")
    if (curseforgeToken.isPresent) {
        curseforge {
            accessToken.set(curseforgeToken)
            projectId.set("YOUR_CURSEFORGE_ID_HERE")
            minecraftVersions.add(mcVersion)

            if (isFabric) {
                requires("fabric-api")
            }
        }
    }
}
