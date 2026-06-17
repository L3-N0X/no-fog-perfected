package de.lenox.client

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent
import net.neoforged.fml.ModContainer

@Mod("no_fog_perfected")
class NoFogNeoForge(container: ModContainer)

@EventBusSubscriber(modid = "no_fog_perfected", value = [Dist.CLIENT])
object NoFogNeoForgeSetup {
    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        NoFogConfig.init()
    }
}

@EventBusSubscriber(modid = "no_fog_perfected", value = [Dist.CLIENT])
object NoFogNeoForgeEvents {

    @SubscribeEvent
    fun onRegisterClientCommands(event: RegisterClientCommandsEvent) {
        event.dispatcher.register(
            Commands.literal("nofog")
                .executes { context ->
                    showConfig { context.source.sendSystemMessage(it) }
                    Command.SINGLE_SUCCESS
                }
                // ── overworld ─────────────────────────────────────────────
                .then(Commands.literal("overworld")
                    .executes { context ->
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatLine(
                            "Overworld fog", NoFogConfig.overworldFog,
                            "Multiplier", "${FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.overworldFogMultiplier)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.literal("toggle").executes { context ->
                        NoFogConfig.overworldFog = !NoFogConfig.overworldFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("Overworld fog", NoFogConfig.overworldFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(Commands.literal("set")
                        .then(Commands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.overworldFogMultiplier = FogSliderFormulas.sliderToDimensionMultiplier(value)
                            NoFogConfig.save()
                            context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("Overworld fog multiplier", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
                // ── nether ────────────────────────────────────────────────
                .then(Commands.literal("nether")
                    .executes { context ->
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatLine(
                            "Nether fog", NoFogConfig.netherFog,
                            "Offset", "${FogSliderFormulas.dimensionOffsetToSlider(NoFogConfig.netherFogOffset)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.literal("toggle").executes { context ->
                        NoFogConfig.netherFog = !NoFogConfig.netherFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("Nether fog", NoFogConfig.netherFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(Commands.literal("set")
                        .then(Commands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.netherFogOffset = FogSliderFormulas.sliderToDimensionOffset(value)
                            NoFogConfig.save()
                            context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("Nether fog offset", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
                // ── end ───────────────────────────────────────────────────
                .then(Commands.literal("end")
                    .executes { context ->
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatLine(
                            "End fog", NoFogConfig.endFog,
                            "Multiplier", "${FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.endFogMultiplier)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.literal("toggle").executes { context ->
                        NoFogConfig.endFog = !NoFogConfig.endFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("End fog", NoFogConfig.endFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(Commands.literal("set")
                        .then(Commands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.endFogMultiplier = FogSliderFormulas.sliderToDimensionMultiplier(value)
                            NoFogConfig.save()
                            context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("End fog multiplier", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
                // ── water ─────────────────────────────────────────────────
                .then(Commands.literal("water")
                    .executes { context ->
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatLine(
                            "Water fog", NoFogConfig.waterFog,
                            "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.waterFogOffset, FogSliderFormulas.WATER_MAX_DISTANCE)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.literal("toggle").executes { context ->
                        NoFogConfig.waterFog = !NoFogConfig.waterFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("Water fog", NoFogConfig.waterFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(Commands.literal("set")
                        .then(Commands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.waterFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.WATER_MAX_DISTANCE)
                            NoFogConfig.save()
                            context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("Water fog offset", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
                // ── lava ──────────────────────────────────────────────────
                .then(Commands.literal("lava")
                    .executes { context ->
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatLine(
                            "Lava fog", NoFogConfig.lavaFog,
                            "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.lavaFogOffset, FogSliderFormulas.LAVA_MAX_DISTANCE)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.literal("toggle").executes { context ->
                        NoFogConfig.lavaFog = !NoFogConfig.lavaFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("Lava fog", NoFogConfig.lavaFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(Commands.literal("set")
                        .then(Commands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.lavaFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.LAVA_MAX_DISTANCE)
                            NoFogConfig.save()
                            context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("Lava fog offset", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
                // ── powder_snow ───────────────────────────────────────────
                .then(Commands.literal("powder_snow")
                    .executes { context ->
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatLine(
                            "Powder snow fog", NoFogConfig.powderSnowFog,
                            "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.powderSnowFogOffset, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE)}%"
                        ))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.literal("toggle").executes { context ->
                        NoFogConfig.powderSnowFog = !NoFogConfig.powderSnowFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("Powder snow fog", NoFogConfig.powderSnowFog))
                        Command.SINGLE_SUCCESS
                    })
                    .then(Commands.literal("set")
                        .then(Commands.argument("0-100", IntegerArgumentType.integer(0, 100)).executes { context ->
                            val value = IntegerArgumentType.getInteger(context, "0-100")
                            NoFogConfig.powderSnowFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE)
                            NoFogConfig.save()
                            context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("Powder snow fog offset", "${value}%"))
                            Command.SINGLE_SUCCESS
                        })
                    )
                )
                // ── blindness (toggle-only) ───────────────────────────────
                .then(Commands.literal("blindness")
                    .executes { context ->
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatLine("Blindness fog", NoFogConfig.blindnessFog))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.literal("toggle").executes { context ->
                        NoFogConfig.blindnessFog = !NoFogConfig.blindnessFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("Blindness fog", NoFogConfig.blindnessFog))
                        Command.SINGLE_SUCCESS
                    })
                )
                // ── darkness (toggle-only) ────────────────────────────────
                .then(Commands.literal("darkness")
                    .executes { context ->
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatLine("Darkness fog", NoFogConfig.darknessFog))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.literal("toggle").executes { context ->
                        NoFogConfig.darknessFog = !NoFogConfig.darknessFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(NoFogCommandFormatting.formatFeedback("Darkness fog", NoFogConfig.darknessFog))
                        Command.SINGLE_SUCCESS
                    })
                )
        )
    }

    private fun showConfig(sendMessage: (Component) -> Unit) {
        sendMessage(NoFogCommandFormatting.formatHeader("No Fog Config"))
        sendMessage(NoFogCommandFormatting.formatLine("Overworld fog", NoFogConfig.overworldFog, "Multiplier", "${FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.overworldFogMultiplier)}%"))
        sendMessage(NoFogCommandFormatting.formatLine("Nether fog", NoFogConfig.netherFog, "Offset", "${FogSliderFormulas.dimensionOffsetToSlider(NoFogConfig.netherFogOffset)}%"))
        sendMessage(NoFogCommandFormatting.formatLine("End fog", NoFogConfig.endFog, "Multiplier", "${FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.endFogMultiplier)}%"))
        sendMessage(NoFogCommandFormatting.formatLine("Water fog", NoFogConfig.waterFog, "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.waterFogOffset, FogSliderFormulas.WATER_MAX_DISTANCE)}%"))
        sendMessage(NoFogCommandFormatting.formatLine("Lava fog", NoFogConfig.lavaFog, "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.lavaFogOffset, FogSliderFormulas.LAVA_MAX_DISTANCE)}%"))
        sendMessage(NoFogCommandFormatting.formatLine("Powder snow fog", NoFogConfig.powderSnowFog, "Offset", "${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.powderSnowFogOffset, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE)}%"))
        sendMessage(NoFogCommandFormatting.formatLine("Blindness fog", NoFogConfig.blindnessFog))
        sendMessage(NoFogCommandFormatting.formatLine("Darkness fog", NoFogConfig.darknessFog))
    }
}
