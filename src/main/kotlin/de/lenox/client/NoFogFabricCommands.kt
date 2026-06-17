//? if fabric {
/*package de.lenox.client

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
//? if >=26.1 {
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommands
*///?}
//? if <26.1 {
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager as ClientCommands
//?}
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
                .then(ClientCommands.literal("overworld")
                    .executes { context ->
                        NoFogConfig.overworldFog = !NoFogConfig.overworldFog
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Overworld fog: ${boolState(NoFogConfig.overworldFog)}"))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.argument("value", IntegerArgumentType.integer(0, 100)).executes { context ->
                        val value = IntegerArgumentType.getInteger(context, "value")
                        NoFogConfig.overworldFogMultiplier = FogSliderFormulas.sliderToDimensionMultiplier(value)
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Overworld fog multiplier: ${value}%"))
                        Command.SINGLE_SUCCESS
                    })
                )
                .then(ClientCommands.literal("nether")
                    .executes { context ->
                        NoFogConfig.netherFog = !NoFogConfig.netherFog
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Nether fog: ${boolState(NoFogConfig.netherFog)}"))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.argument("value", IntegerArgumentType.integer(0, 100)).executes { context ->
                        val value = IntegerArgumentType.getInteger(context, "value")
                        NoFogConfig.netherFogOffset = FogSliderFormulas.sliderToDimensionOffset(value)
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Nether fog offset: ${value}%"))
                        Command.SINGLE_SUCCESS
                    })
                )
                .then(ClientCommands.literal("end")
                    .executes { context ->
                        NoFogConfig.endFog = !NoFogConfig.endFog
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("End fog: ${boolState(NoFogConfig.endFog)}"))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.argument("value", IntegerArgumentType.integer(0, 100)).executes { context ->
                        val value = IntegerArgumentType.getInteger(context, "value")
                        NoFogConfig.endFogMultiplier = FogSliderFormulas.sliderToDimensionMultiplier(value)
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("End fog multiplier: ${value}%"))
                        Command.SINGLE_SUCCESS
                    })
                )
                .then(ClientCommands.literal("water")
                    .executes { context ->
                        NoFogConfig.waterFog = !NoFogConfig.waterFog
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Water fog: ${boolState(NoFogConfig.waterFog)}"))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.argument("value", IntegerArgumentType.integer(0, 100)).executes { context ->
                        val value = IntegerArgumentType.getInteger(context, "value")
                        NoFogConfig.waterFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.WATER_MAX_DISTANCE)
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Water fog offset: ${value}%"))
                        Command.SINGLE_SUCCESS
                    })
                )
                .then(ClientCommands.literal("lava")
                    .executes { context ->
                        NoFogConfig.lavaFog = !NoFogConfig.lavaFog
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Lava fog: ${boolState(NoFogConfig.lavaFog)}"))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.argument("value", IntegerArgumentType.integer(0, 100)).executes { context ->
                        val value = IntegerArgumentType.getInteger(context, "value")
                        NoFogConfig.lavaFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.LAVA_MAX_DISTANCE)
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Lava fog offset: ${value}%"))
                        Command.SINGLE_SUCCESS
                    })
                )
                .then(ClientCommands.literal("powder_snow")
                    .executes { context ->
                        NoFogConfig.powderSnowFog = !NoFogConfig.powderSnowFog
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Powder snow fog: ${boolState(NoFogConfig.powderSnowFog)}"))
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.argument("value", IntegerArgumentType.integer(0, 100)).executes { context ->
                        val value = IntegerArgumentType.getInteger(context, "value")
                        NoFogConfig.powderSnowFogOffset = FogSliderFormulas.sliderToFluidOffset(value, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE)
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Powder snow fog offset: ${value}%"))
                        Command.SINGLE_SUCCESS
                    })
                )
                .then(ClientCommands.literal("blindness").executes { context ->
                    NoFogConfig.blindnessFog = !NoFogConfig.blindnessFog
                    NoFogConfig.save()
                    context.source.sendFeedback(Component.literal("Blindness fog: ${boolState(NoFogConfig.blindnessFog)}"))
                    Command.SINGLE_SUCCESS
                })
                .then(ClientCommands.literal("darkness").executes { context ->
                    NoFogConfig.darknessFog = !NoFogConfig.darknessFog
                    NoFogConfig.save()
                    context.source.sendFeedback(Component.literal("Darkness fog: ${boolState(NoFogConfig.darknessFog)}"))
                    Command.SINGLE_SUCCESS
                })
        )
    }

    private fun boolState(value: Boolean) = if (value) "ON" else "OFF"

    private fun showConfig(sendFeedback: (Component) -> Unit) {
        sendFeedback(Component.literal("--- No Fog Config ---"))
        sendFeedback(Component.literal("Overworld fog: ${boolState(NoFogConfig.overworldFog)} | Multiplier: ${FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.overworldFogMultiplier)}%"))
        sendFeedback(Component.literal("Nether fog: ${boolState(NoFogConfig.netherFog)} | Offset: ${FogSliderFormulas.dimensionOffsetToSlider(NoFogConfig.netherFogOffset)}%"))
        sendFeedback(Component.literal("End fog: ${boolState(NoFogConfig.endFog)} | Multiplier: ${FogSliderFormulas.dimensionMultiplierToSlider(NoFogConfig.endFogMultiplier)}%"))
        sendFeedback(Component.literal("Water fog: ${boolState(NoFogConfig.waterFog)} | Offset: ${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.waterFogOffset, FogSliderFormulas.WATER_MAX_DISTANCE)}%"))
        sendFeedback(Component.literal("Lava fog: ${boolState(NoFogConfig.lavaFog)} | Offset: ${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.lavaFogOffset, FogSliderFormulas.LAVA_MAX_DISTANCE)}%"))
        sendFeedback(Component.literal("Powder snow fog: ${boolState(NoFogConfig.powderSnowFog)} | Offset: ${FogSliderFormulas.fluidOffsetToSlider(NoFogConfig.powderSnowFogOffset, FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE)}%"))
        sendFeedback(Component.literal("Blindness fog: ${boolState(NoFogConfig.blindnessFog)}"))
        sendFeedback(Component.literal("Darkness fog: ${boolState(NoFogConfig.darknessFog)}"))
    }
}
*///?}
