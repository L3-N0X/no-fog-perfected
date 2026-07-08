//? if fabric {
package de.lenox.client

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
//? if >=26.1 {
import net.fabricmc.fabric.api.client.command.v2.ClientCommands
//?}
//? if <26.1 {
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager as ClientCommands
*///?}
import net.minecraft.network.chat.Component
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object NoFogFabricCommands {
    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(
            ClientCommands.literal("nofog")
                .executes { context ->
                    showConfig { context.source.sendFeedback(it) }
                    Command.SINGLE_SUCCESS
                }
                // ── overworld ─────────────────────────────────────────────
                .then(ClientCommands.literal("overworld")
                    .executes { context ->
                        context.source.sendFeedback(NoFogCommandFormatting.formatLine(
                            "Overworld fog", NoFogConfig.overworldFog,
                            "Multiplier", "${FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.overworldFogMultiplier)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.literal("toggle").executes { context ->
                        NoFogConfig.overworldFog = !NoFogConfig.overworldFog
                        NoFogConfig.save()
                        context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Overworld fog", NoFogConfig.overworldFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(ClientCommands.literal("set")
                        .then(ClientCommands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.overworldFogMultiplier = FogSliderFormulas.sliderToDimensionMultiplier(value)
                            NoFogConfig.save()
                            context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Overworld fog multiplier", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
                // ── nether ────────────────────────────────────────────────
                .then(ClientCommands.literal("nether")
                    .executes { context ->
                        context.source.sendFeedback(NoFogCommandFormatting.formatLine(
                            "Nether fog", NoFogConfig.netherFog,
                            "Offset", "${FogSliderFormulas.dimensionOffsetToSlider(NoFogConfig.netherFogOffset)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.literal("toggle").executes { context ->
                        NoFogConfig.netherFog = !NoFogConfig.netherFog
                        NoFogConfig.save()
                        context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Nether fog", NoFogConfig.netherFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(ClientCommands.literal("set")
                        .then(ClientCommands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.netherFogOffset = FogSliderFormulas.sliderToDimensionOffset(value)
                            NoFogConfig.save()
                            context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Nether fog offset", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
                // ── end ───────────────────────────────────────────────────
                .then(ClientCommands.literal("end")
                    .executes { context ->
                        context.source.sendFeedback(NoFogCommandFormatting.formatLine(
                            "End fog", NoFogConfig.endFog,
                            "Multiplier", "${FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.endFogMultiplier)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.literal("toggle").executes { context ->
                        NoFogConfig.endFog = !NoFogConfig.endFog
                        NoFogConfig.save()
                        context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("End fog", NoFogConfig.endFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(ClientCommands.literal("set")
                        .then(ClientCommands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.endFogMultiplier = FogSliderFormulas.sliderToDimensionMultiplier(value)
                            NoFogConfig.save()
                            context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("End fog multiplier", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
                // ── water ─────────────────────────────────────────────────
                .then(ClientCommands.literal("water")
                    .executes { context ->
                        context.source.sendFeedback(NoFogCommandFormatting.formatLine(
                            "Water fog", NoFogConfig.waterFog,
                            "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.waterFogOffset, FogSliderFormulas.WATER_MAX_DISTANCE)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.literal("toggle").executes { context ->
                        NoFogConfig.waterFog = !NoFogConfig.waterFog
                        NoFogConfig.save()
                        context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Water fog", NoFogConfig.waterFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(ClientCommands.literal("set")
                        .then(ClientCommands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.waterFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.WATER_MAX_DISTANCE)
                            NoFogConfig.save()
                            context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Water fog offset", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
                // ── lava ──────────────────────────────────────────────────
                .then(ClientCommands.literal("lava")
                    .executes { context ->
                        context.source.sendFeedback(NoFogCommandFormatting.formatLine(
                            "Lava fog", NoFogConfig.lavaFog,
                            "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.lavaFogOffset, FogSliderFormulas.LAVA_MAX_DISTANCE)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.literal("toggle").executes { context ->
                        NoFogConfig.lavaFog = !NoFogConfig.lavaFog
                        NoFogConfig.save()
                        context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Lava fog", NoFogConfig.lavaFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(ClientCommands.literal("set")
                        .then(ClientCommands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.lavaFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.LAVA_MAX_DISTANCE)
                            NoFogConfig.save()
                            context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Lava fog offset", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
                // ── powder_snow ───────────────────────────────────────────
                .then(ClientCommands.literal("powder_snow")
                    .executes { context ->
                        context.source.sendFeedback(NoFogCommandFormatting.formatLine(
                            "Powder snow fog", NoFogConfig.powderSnowFog,
                            "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.powderSnowFogOffset, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.literal("toggle").executes { context ->
                        NoFogConfig.powderSnowFog = !NoFogConfig.powderSnowFog
                        NoFogConfig.save()
                        context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Powder snow fog", NoFogConfig.powderSnowFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(ClientCommands.literal("set")
                        .then(ClientCommands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.powderSnowFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE)
                            NoFogConfig.save()
                            context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Powder snow fog offset", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
                // ── blindness ─────────────────────────────────────────────
                .then(ClientCommands.literal("blindness")
                    .executes { context ->
                        context.source.sendFeedback(NoFogCommandFormatting.formatLine(
                            "Blindness fog", NoFogConfig.blindnessFog,
                            "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.blindnessFogOffset, FogSliderFormulas.BLINDNESS_MAX_DISTANCE)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.literal("toggle").executes { context ->
                        NoFogConfig.blindnessFog = !NoFogConfig.blindnessFog
                        NoFogConfig.save()
                        context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Blindness fog", NoFogConfig.blindnessFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(ClientCommands.literal("set")
                        .then(ClientCommands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.blindnessFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.BLINDNESS_MAX_DISTANCE)
                            NoFogConfig.save()
                            context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Blindness fog offset", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
                // ── darkness ──────────────────────────────────────────────
                .then(ClientCommands.literal("darkness")
                    .executes { context ->
                        context.source.sendFeedback(NoFogCommandFormatting.formatLine(
                            "Darkness fog", NoFogConfig.darknessFog,
                            "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.darknessFogOffset, FogSliderFormulas.DARKNESS_MAX_DISTANCE)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.literal("toggle").executes { context ->
                        NoFogConfig.darknessFog = !NoFogConfig.darknessFog
                        NoFogConfig.save()
                        context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Darkness fog", NoFogConfig.darknessFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(ClientCommands.literal("set")
                        .then(ClientCommands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.darknessFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.DARKNESS_MAX_DISTANCE)
                            NoFogConfig.save()
                            context.source.sendFeedback(NoFogCommandFormatting.formatFeedback("Darkness fog offset", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
        )
    }

    private fun showConfig(sendFeedback: (Component) -> Unit) {
        sendFeedback(NoFogCommandFormatting.formatHeader("No Fog Config"))
        sendFeedback(NoFogCommandFormatting.formatLine("Overworld fog", NoFogConfig.overworldFog, "Multiplier", "${FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.overworldFogMultiplier)}%"))
        sendFeedback(NoFogCommandFormatting.formatLine("Nether fog", NoFogConfig.netherFog, "Offset", "${FogSliderFormulas.dimensionOffsetToSlider(NoFogConfig.netherFogOffset)}%"))
        sendFeedback(NoFogCommandFormatting.formatLine("End fog", NoFogConfig.endFog, "Multiplier", "${FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.endFogMultiplier)}%"))
        sendFeedback(NoFogCommandFormatting.formatLine("Water fog", NoFogConfig.waterFog, "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.waterFogOffset, FogSliderFormulas.WATER_MAX_DISTANCE)}%"))
        sendFeedback(NoFogCommandFormatting.formatLine("Lava fog", NoFogConfig.lavaFog, "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.lavaFogOffset, FogSliderFormulas.LAVA_MAX_DISTANCE)}%"))
        sendFeedback(NoFogCommandFormatting.formatLine("Powder snow fog", NoFogConfig.powderSnowFog, "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.powderSnowFogOffset, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE)}%"))
        sendFeedback(NoFogCommandFormatting.formatLine("Blindness fog", NoFogConfig.blindnessFog, "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.blindnessFogOffset, FogSliderFormulas.BLINDNESS_MAX_DISTANCE)}%"))
        sendFeedback(NoFogCommandFormatting.formatLine("Darkness fog", NoFogConfig.darknessFog, "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.darknessFogOffset, FogSliderFormulas.DARKNESS_MAX_DISTANCE)}%"))
    }
}

//?}
