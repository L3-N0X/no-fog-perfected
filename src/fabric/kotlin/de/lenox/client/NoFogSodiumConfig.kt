package de.lenox.client

import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint
import net.caffeinemc.mods.sodium.api.config.option.Range
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

class NoFogSodiumConfig : ConfigEntryPoint {
    override fun registerConfigLate(builder: ConfigBuilder) {
        builder.registerOwnModOptions()
            .setIcon(Identifier.fromNamespaceAndPath("no-fog-perfected", "icon.png"))
            .addPage(builder.createOptionPage()
                .setName(Component.literal("General"))
                .addOptionGroup(builder.createOptionGroup()
                    .setName(Component.literal("Dimension Fog"))
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "overworld_fog"))
                        .setName(Component.literal("Render Overworld Fog"))
                        .setTooltip(Component.literal("When enabled, the distance-based fog in the Overworld will be rendered.\nThis affects the fog you see when looking at far-away terrain and sky in the Overworld."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setBinding(
                            { value -> NoFogConfig.overworldFog = value },
                            { NoFogConfig.overworldFog }
                        )
                        .setDefaultValue(false)
                    )
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "nether_fog"))
                        .setName(Component.literal("Render Nether Fog"))
                        .setTooltip(Component.literal("When enabled, the dense distance-based fog in the Nether will be rendered.\nThis affects the thick fog you see when looking around in the Nether."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setBinding(
                            { value -> NoFogConfig.netherFog = value },
                            { NoFogConfig.netherFog }
                        )
                        .setDefaultValue(false)
                    )
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "end_fog"))
                        .setName(Component.literal("Render End Fog"))
                        .setTooltip(Component.literal("When enabled, the distance-based fog in the End will be rendered.\nThis affects the fog you see when looking at far-away terrain and void in the End."))
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
                        .setTooltip(Component.literal("When enabled, the underwater fog will be rendered.\nThis affects visibility while submerged in water."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setBinding(
                            { value -> NoFogConfig.waterFog = value },
                            { NoFogConfig.waterFog }
                        )
                        .setDefaultValue(true)
                    )
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "lava_fog"))
                        .setName(Component.literal("Render Lava Fog"))
                        .setTooltip(Component.literal("When enabled, the lava fog will be rendered.\nThis affects visibility while submerged in lava."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setBinding(
                            { value -> NoFogConfig.lavaFog = value },
                            { NoFogConfig.lavaFog }
                        )
                        .setDefaultValue(true)
                    )
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "powder_snow_fog"))
                        .setName(Component.literal("Render Powder Snow Fog"))
                        .setTooltip(Component.literal("When enabled, the powder snow fog will be rendered.\nThis affects visibility while inside powder snow blocks."))
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
                        .setTooltip(Component.literal("When enabled, the blindness effect fog will be rendered.\nThis affects visibility when the blindness status effect is active."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setBinding(
                            { value -> NoFogConfig.blindnessFog = value },
                            { NoFogConfig.blindnessFog }
                        )
                        .setDefaultValue(true)
                    )
                    .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "darkness_fog"))
                        .setName(Component.literal("Render Darkness Fog"))
                        .setTooltip(Component.literal("When enabled, the darkness effect fog will be rendered.\nThis affects visibility when the darkness status effect from the Warden is active."))
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
                    .setName(Component.literal("Fluid Fog Offset"))
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "water_fog_offset"))
                        .setName(Component.literal("Water Fog Offset"))
                        .setTooltip(Component.literal("Offsets the water fog further away from the player.\nAt 0%%, the default vanilla fog distance is used.\nHigher values push the fog further out while preserving the gradient.\nThe value scales quadratically for easier fine-tuning at lower distances."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.FLUID_FOG_SLIDER_MIN, FogSliderFormulas.FLUID_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.waterFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.WATER_MAX_DISTANCE) },
                            { FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.waterFogOffset, FogSliderFormulas.WATER_MAX_DISTANCE) }
                        )
                        .setDefaultValue(0)
                    )
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "lava_fog_offset"))
                        .setName(Component.literal("Lava Fog Offset"))
                        .setTooltip(Component.literal("Offsets the lava fog further away from the player.\nAt 0%%, the default vanilla fog distance is used.\nHigher values push the fog further out while preserving the gradient.\nThe value scales quadratically for easier fine-tuning at lower distances."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.FLUID_FOG_SLIDER_MIN, FogSliderFormulas.FLUID_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.lavaFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.LAVA_MAX_DISTANCE) },
                            { FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.lavaFogOffset, FogSliderFormulas.LAVA_MAX_DISTANCE) }
                        )
                        .setDefaultValue(0)
                    )
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "powder_snow_fog_offset"))
                        .setName(Component.literal("Powder Snow Fog Offset"))
                        .setTooltip(Component.literal("Offsets the powder snow fog further away from the player.\nAt 0%%, the default vanilla fog distance is used.\nHigher values push the fog further out while preserving the gradient.\nThe value scales quadratically for easier fine-tuning at lower distances."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.FLUID_FOG_SLIDER_MIN, FogSliderFormulas.FLUID_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.powderSnowFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE) },
                            { FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.powderSnowFogOffset, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE) }
                        )
                        .setDefaultValue(0)
                    )
                )
                .addOptionGroup(builder.createOptionGroup()
                    .setName(Component.literal("Dimension Fog Offset"))
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "overworld_fog_offset"))
                        .setName(Component.literal("Overworld Fog Offset"))
                        .setTooltip(Component.literal("Offsets the overworld fog further away from the player.\nAt 0%%, the default vanilla fog distance is used.\nHigher values push the fog further out while preserving the gradient.\nThe value scales quadratically for easier fine-tuning at lower distances."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.DIMENSION_FOG_SLIDER_MIN, FogSliderFormulas.DIMENSION_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.overworldFogOffset = FogSliderFormulas.sliderToDimensionOffset(value) },
                            { FogSliderFormulas.dimensionOffsetToSlider(NoFogConfig.overworldFogOffset) }
                        )
                        .setDefaultValue(0)
                    )
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "nether_fog_offset"))
                        .setName(Component.literal("Nether Fog Offset"))
                        .setTooltip(Component.literal("Offsets the nether fog further away from the player.\nAt 0%%, the default vanilla fog distance is used.\nHigher values push the fog further out while preserving the gradient.\nThe value scales quadratically for easier fine-tuning at lower distances."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.DIMENSION_FOG_SLIDER_MIN, FogSliderFormulas.DIMENSION_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.netherFogOffset = FogSliderFormulas.sliderToDimensionOffset(value) },
                            { FogSliderFormulas.dimensionOffsetToSlider(NoFogConfig.netherFogOffset) }
                        )
                        .setDefaultValue(0)
                    )
                    .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("no-fog-perfected", "end_fog_offset"))
                        .setName(Component.literal("End Fog Offset"))
                        .setTooltip(Component.literal("Offsets the end fog further away from the player.\nAt 0%%, the default vanilla fog distance is used.\nHigher values push the fog further out while preserving the gradient.\nThe value scales quadratically for easier fine-tuning at lower distances."))
                        .setStorageHandler { NoFogConfig.save() }
                        .setRange(Range(FogSliderFormulas.DIMENSION_FOG_SLIDER_MIN, FogSliderFormulas.DIMENSION_FOG_SLIDER_MAX, 1))
                        .setValueFormatter { value -> Component.literal("$value%") }
                        .setBinding(
                            { value -> NoFogConfig.endFogOffset = FogSliderFormulas.sliderToDimensionOffset(value) },
                            { FogSliderFormulas.dimensionOffsetToSlider(NoFogConfig.endFogOffset) }
                        )
                        .setDefaultValue(0)
                    )
                )
            )
    }
}
