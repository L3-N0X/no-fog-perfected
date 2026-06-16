package de.lenox.client

import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint
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
    }
}
