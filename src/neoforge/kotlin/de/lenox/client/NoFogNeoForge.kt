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
                .then(Commands.literal("overworld")
                    .executes { context ->
                        NoFogConfig.overworldFog = !NoFogConfig.overworldFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(Component.literal("Overworld fog: ${boolState(NoFogConfig.overworldFog)}"))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.argument("value", IntegerArgumentType.integer(0, 100)).executes { context ->
                        val value = IntegerArgumentType.getInteger(context, "value")
                        NoFogConfig.overworldFogMultiplier = FogSliderFormulas.sliderToDimensionMultiplier(value)
                        NoFogConfig.save()
                        context.source.sendSystemMessage(Component.literal("Overworld fog multiplier: ${value}%"))
                        Command.SINGLE_SUCCESS
                    })
                )
                .then(Commands.literal("nether")
                    .executes { context ->
                        NoFogConfig.netherFog = !NoFogConfig.netherFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(Component.literal("Nether fog: ${boolState(NoFogConfig.netherFog)}"))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.argument("value", IntegerArgumentType.integer(0, 100)).executes { context ->
                        val value = IntegerArgumentType.getInteger(context, "value")
                        NoFogConfig.netherFogOffset = FogSliderFormulas.sliderToDimensionOffset(value)
                        NoFogConfig.save()
                        context.source.sendSystemMessage(Component.literal("Nether fog offset: ${value}%"))
                        Command.SINGLE_SUCCESS
                    })
                )
                .then(Commands.literal("end")
                    .executes { context ->
                        NoFogConfig.endFog = !NoFogConfig.endFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(Component.literal("End fog: ${boolState(NoFogConfig.endFog)}"))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.argument("value", IntegerArgumentType.integer(0, 100)).executes { context ->
                        val value = IntegerArgumentType.getInteger(context, "value")
                        NoFogConfig.endFogMultiplier = FogSliderFormulas.sliderToDimensionMultiplier(value)
                        NoFogConfig.save()
                        context.source.sendSystemMessage(Component.literal("End fog multiplier: ${value}%"))
                        Command.SINGLE_SUCCESS
                    })
                )
                .then(Commands.literal("water")
                    .executes { context ->
                        NoFogConfig.waterFog = !NoFogConfig.waterFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(Component.literal("Water fog: ${boolState(NoFogConfig.waterFog)}"))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.argument("value", IntegerArgumentType.integer(0, 100)).executes { context ->
                        val value = IntegerArgumentType.getInteger(context, "value")
                        NoFogConfig.waterFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.WATER_MAX_DISTANCE)
                        NoFogConfig.save()
                        context.source.sendSystemMessage(Component.literal("Water fog offset: ${value}%"))
                        Command.SINGLE_SUCCESS
                    })
                )
                .then(Commands.literal("lava")
                    .executes { context ->
                        NoFogConfig.lavaFog = !NoFogConfig.lavaFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(Component.literal("Lava fog: ${boolState(NoFogConfig.lavaFog)}"))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.argument("value", IntegerArgumentType.integer(0, 100)).executes { context ->
                        val value = IntegerArgumentType.getInteger(context, "value")
                        NoFogConfig.lavaFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.LAVA_MAX_DISTANCE)
                        NoFogConfig.save()
                        context.source.sendSystemMessage(Component.literal("Lava fog offset: ${value}%"))
                        Command.SINGLE_SUCCESS
                    })
                )
                .then(Commands.literal("powder_snow")
                    .executes { context ->
                        NoFogConfig.powderSnowFog = !NoFogConfig.powderSnowFog
                        NoFogConfig.save()
                        context.source.sendSystemMessage(Component.literal("Powder snow fog: ${boolState(NoFogConfig.powderSnowFog)}"))
                        Command.SINGLE_SUCCESS
                    }
                    .then(Commands.argument("value", IntegerArgumentType.integer(0, 100)).executes { context ->
                        val value = IntegerArgumentType.getInteger(context, "value")
                        NoFogConfig.powderSnowFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE)
                        NoFogConfig.save()
                        context.source.sendSystemMessage(Component.literal("Powder snow fog offset: ${value}%"))
                        Command.SINGLE_SUCCESS
                    })
                )
                .then(Commands.literal("blindness").executes { context ->
                    NoFogConfig.blindnessFog = !NoFogConfig.blindnessFog
                    NoFogConfig.save()
                    context.source.sendSystemMessage(Component.literal("Blindness fog: ${boolState(NoFogConfig.blindnessFog)}"))
                    Command.SINGLE_SUCCESS
                })
                .then(Commands.literal("darkness").executes { context ->
                    NoFogConfig.darknessFog = !NoFogConfig.darknessFog
                    NoFogConfig.save()
                    context.source.sendSystemMessage(Component.literal("Darkness fog: ${boolState(NoFogConfig.darknessFog)}"))
                    Command.SINGLE_SUCCESS
                })
        )
    }

    private fun boolState(value: Boolean) = if (value) "ON" else "OFF"

    private fun showConfig(sendMessage: (Component) -> Unit) {
        sendMessage(Component.literal("--- No Fog Config ---"))
        sendMessage(Component.literal("Overworld fog: ${boolState(NoFogConfig.overworldFog)} | Multiplier: ${FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.overworldFogMultiplier)}%"))
        sendMessage(Component.literal("Nether fog: ${boolState(NoFogConfig.netherFog)} | Offset: ${FogSliderFormulas.dimensionOffsetToSlider(NoFogConfig.netherFogOffset)}%"))
        sendMessage(Component.literal("End fog: ${boolState(NoFogConfig.endFog)} | Multiplier: ${FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.endFogMultiplier)}%"))
        sendMessage(Component.literal("Water fog: ${boolState(NoFogConfig.waterFog)} | Offset: ${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.waterFogOffset, FogSliderFormulas.WATER_MAX_DISTANCE)}%"))
        sendMessage(Component.literal("Lava fog: ${boolState(NoFogConfig.lavaFog)} | Offset: ${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.lavaFogOffset, FogSliderFormulas.LAVA_MAX_DISTANCE)}%"))
        sendMessage(Component.literal("Powder snow fog: ${boolState(NoFogConfig.powderSnowFog)} | Offset: ${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.powderSnowFogOffset, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE)}%"))
        sendMessage(Component.literal("Blindness fog: ${boolState(NoFogConfig.blindnessFog)}"))
        sendMessage(Component.literal("Darkness fog: ${boolState(NoFogConfig.darknessFog)}"))
    }
}
