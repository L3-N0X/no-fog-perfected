package de.lenox.client

import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint
import net.caffeinemc.mods.sodium.api.config.ConfigState
import net.caffeinemc.mods.sodium.api.config.option.Range
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

class NoFogSodiumConfig : ConfigEntryPoint {
    override fun registerConfigLate(builder: ConfigBuilder) {
        builder.registerOwnModOptions()
            .setIcon(Identifier.fromNamespaceAndPath("no-fog-perfected", "icon_mono.png"))
            .addPage(builder.createOptionPage()
                .setName(Component.literal("General"))
                .addOptionGroup(builder.createOptionGroup()
                    .setName(Component.literal("Dimension Fog"))
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "overworld_fog"))
                        .setName(Component.literal("Render Overworld Fog"))
                        .setTooltip(Component.literal("Renders distance-based fog over far-away terrain and sky in the Overworld."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setBinding(
                            { value -> NoFogConfig.overworldFog = value },
                            { NoFogConfig.overworldFog }
                        )
                        .setDefaultValue(false)
                    )
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "nether_fog"))
                        .setName(Component.literal("Render Nether Fog"))
                        .setTooltip(Component.literal("Renders the dense distance-based fog while exploring the Nether."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setBinding(
                            { value -> NoFogConfig.netherFog = value },
                            { NoFogConfig.netherFog }
                        )
                        .setDefaultValue(false)
                    )
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "end_fog"))
                        .setName(Component.literal("Render End Fog"))
                        .setTooltip(Component.literal("Renders distance-based fog over far-away terrain and the void in the End."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setBinding(
                            { value -> NoFogConfig.endFog = value },
                            { NoFogConfig.endFog }
                        )
                        .setDefaultValue(false)
                    )
                )
                .addOptionGroup(builder.createOptionGroup()
                    .setName(Component.literal("Render Fluid Fog"))
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "water_fog"))
                        .setName(Component.literal("Water Fog"))
                        .setTooltip(Component.literal("Renders fog and limits visibility while submerged underwater."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setBinding(
                            { value -> NoFogConfig.waterFog = value },
                            { NoFogConfig.waterFog }
                        )
                        .setDefaultValue(true)
                    )
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "lava_fog"))
                        .setName(Component.literal("Render Lava Fog"))
                        .setTooltip(Component.literal("Renders dense fog and severely limits visibility while submerged in lava."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setBinding(
                            { value -> NoFogConfig.lavaFog = value },
                            { NoFogConfig.lavaFog }
                        )
                        .setDefaultValue(true)
                    )
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "powder_snow_fog"))
                        .setName(Component.literal("Render Powder Snow Fog"))
                        .setTooltip(Component.literal("Renders thick whiteout fog while trapped inside powder snow blocks."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setBinding(
                            { value -> NoFogConfig.powderSnowFog = value },
                            { NoFogConfig.powderSnowFog }
                        )
                        .setDefaultValue(true)
                    )
                )
                .addOptionGroup(builder.createOptionGroup()
                    .setName(Component.literal("Status Effect Fog"))
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "blindness_fog"))
                        .setName(Component.literal("Render Blindness Fog"))
                        .setTooltip(Component.literal("Renders the restrictive, close-range fog caused by the Blindness status effect."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setBinding(
                            { value -> NoFogConfig.blindnessFog = value },
                            { NoFogConfig.blindnessFog }
                        )
                        .setDefaultValue(true)
                    )
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "darkness_fog"))
                        .setName(Component.literal("Render Darkness Fog"))
                        .setTooltip(Component.literal("Renders the pulsing darkness fog caused by the Warden or Sculk Shriekers."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setBinding(
                            { value -> NoFogConfig.darknessFog = value },
                            { NoFogConfig.darknessFog }
                        )
                        .setDefaultValue(true)
                    )
                )
            )
            .addPage(builder.createOptionPage()
                .setName(Component.literal("Advanced"))
                .addOptionGroup(builder.createOptionGroup()
                    .setName(Component.literal("Dimension Fog Multiplier"))
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "overworld_fog_multiplier"))
                        .setName(Component.literal("Overworld Fog Multiplier"))
                        .setTooltip(Component.literal("Multiplies overworld fog distance. Fog still scales with your render distance.\n0% = Vanilla. Requires Overworld fog to be enabled."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.DIMENSION_FOG_SLIDER_MIN, FogSliderFormulas.DIMENSION_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.overworldFogMultiplier = FogSliderFormulas.sliderToDimensionMultiplier(value) },
                            { FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.overworldFogMultiplier) }
                        )
                        .setControlHiddenWhenDisabled(false)
                        .setEnabledProvider(
                            { state -> state.readBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "overworld_fog")) },
                            Identifier.fromNamespaceAndPath("no-fog-perfected", "overworld_fog")
                        )
                        .setDefaultValue(0)
                    )
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "nether_fog_offset"))
                        .setName(Component.literal("Nether Fog Offset"))
                        .setTooltip(Component.literal("Pushes Nether fog further away from you.\n0% = Vanilla. Requires Nether fog to be enabled."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.DIMENSION_FOG_SLIDER_MIN, FogSliderFormulas.DIMENSION_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.netherFogOffset = FogSliderFormulas.sliderToDimensionOffset(value) },
                            { FogSliderFormulas.dimensionOffsetToSlider(NoFogConfig.netherFogOffset) }
                        )
                        .setControlHiddenWhenDisabled(false)
                        .setEnabledProvider(
                            { state -> state.readBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "nether_fog")) },
                            Identifier.fromNamespaceAndPath("no-fog-perfected", "nether_fog")
                        )
                        .setDefaultValue(0)
                    )
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "end_fog_multiplier"))
                        .setName(Component.literal("End Fog Multiplier"))
                        .setTooltip(Component.literal("Multiplies End fog distance. Fog still scales with your render distance.\n0% = Vanilla. Requires End fog to be enabled."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.DIMENSION_FOG_SLIDER_MIN, FogSliderFormulas.DIMENSION_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.endFogMultiplier = FogSliderFormulas.sliderToDimensionMultiplier(value) },
                            { FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.endFogMultiplier) }
                        )
                        .setControlHiddenWhenDisabled(false)
                        .setEnabledProvider(
                            { state -> state.readBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "end_fog")) },
                            Identifier.fromNamespaceAndPath("no-fog-perfected", "end_fog")
                        )
                        .setDefaultValue(0)
                    )
                )
                .addOptionGroup(builder.createOptionGroup()
                    .setName(Component.literal("Fluid Fog Offset"))
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "water_fog_offset"))
                        .setName(Component.literal("Water Fog Offset"))
                        .setTooltip(Component.literal("Makes you see further under water.\n0% = Vanilla. Requires Water fog to be enabled."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.FLUID_FOG_SLIDER_MIN, FogSliderFormulas.FLUID_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.waterFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.WATER_MAX_DISTANCE) },
                            { FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.waterFogOffset, FogSliderFormulas.WATER_MAX_DISTANCE) }
                        )
                        .setControlHiddenWhenDisabled(false)
                        .setEnabledProvider(
                            { state -> state.readBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "water_fog")) },
                            Identifier.fromNamespaceAndPath("no-fog-perfected", "water_fog")
                        )
                        .setDefaultValue(0)
                    )
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "lava_fog_offset"))
                        .setName(Component.literal("Lava Fog Offset"))
                        .setTooltip(Component.literal("Makes you see further under lava.\n0% = Vanilla. Requires Lava fog to be enabled."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.FLUID_FOG_SLIDER_MIN, FogSliderFormulas.FLUID_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.lavaFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.LAVA_MAX_DISTANCE) },
                            { FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.lavaFogOffset, FogSliderFormulas.LAVA_MAX_DISTANCE) }
                        )
                        .setControlHiddenWhenDisabled(false)
                        .setEnabledProvider(
                            { state -> state.readBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "lava_fog")) },
                            Identifier.fromNamespaceAndPath("no-fog-perfected", "lava_fog")
                        )
                        .setDefaultValue(0)
                    )
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "powder_snow_fog_offset"))
                        .setName(Component.literal("Powder Snow Fog Offset"))
                        .setTooltip(Component.literal("Makes you see further under powder snow.\n0% = Vanilla. Requires Powder Snow fog to be enabled."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.FLUID_FOG_SLIDER_MIN, FogSliderFormulas.FLUID_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.powderSnowFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE) },
                            { FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.powderSnowFogOffset, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE) }
                        )
                        .setControlHiddenWhenDisabled(false)
                        .setEnabledProvider(
                            { state -> state.readBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "powder_snow_fog")) },
                            Identifier.fromNamespaceAndPath("no-fog-perfected", "powder_snow_fog")
                        )
                        .setDefaultValue(0)
                    )
                )
                .addOptionGroup(builder.createOptionGroup()
                    .setName(Component.literal("Status Effect Fog Offset"))
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "blindness_fog_offset"))
                        .setName(Component.literal("Blindness Fog Offset"))
                        .setTooltip(Component.literal("Makes you see further when blind.\n0% = Vanilla. Requires Render Blindness Fog to be enabled."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.FLUID_FOG_SLIDER_MIN, FogSliderFormulas.FLUID_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.blindnessFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.BLINDNESS_MAX_DISTANCE) },
                            { FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.blindnessFogOffset, FogSliderFormulas.BLINDNESS_MAX_DISTANCE) }
                        )
                        .setControlHiddenWhenDisabled(false)
                        .setEnabledProvider(
                            { state -> state.readBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "blindness_fog")) },
                            Identifier.fromNamespaceAndPath("no-fog-perfected", "blindness_fog")
                        )
                        .setDefaultValue(0)
                    )
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "darkness_fog_offset"))
                        .setName(Component.literal("Darkness Fog Offset"))
                        .setTooltip(Component.literal("Makes you see further under darkness.\n0% = Vanilla. Requires Render Darkness Fog to be enabled."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.FLUID_FOG_SLIDER_MIN, FogSliderFormulas.FLUID_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.darknessFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.DARKNESS_MAX_DISTANCE) },
                            { FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.darknessFogOffset, FogSliderFormulas.DARKNESS_MAX_DISTANCE) }
                        )
                        .setControlHiddenWhenDisabled(false)
                        .setEnabledProvider(
                            { state -> state.readBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "darkness_fog")) },
                            Identifier.fromNamespaceAndPath("no-fog-perfected", "darkness_fog")
                        )
                        .setDefaultValue(0)
                    )
                )
            )
    }
}
