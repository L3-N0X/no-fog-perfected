package de.lenox.client

import com.mojang.brigadier.Command
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommands
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.network.chat.Component

class NoFogClient : ClientModInitializer {
    override fun onInitializeClient() {
        NoFogConfig.init()

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommands.literal("nofog")
                    .executes { context ->
                        showConfig { context.source.sendFeedback(it) }
                        Command.SINGLE_SUCCESS
                    }
                    .then(ClientCommands.literal("overworld").executes { context ->
                        NoFogConfig.overworldFog = !NoFogConfig.overworldFog
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Overworld fog: ${boolState(NoFogConfig.overworldFog)}"))
                        Command.SINGLE_SUCCESS
                    })
                    .then(ClientCommands.literal("nether").executes { context ->
                        NoFogConfig.netherFog = !NoFogConfig.netherFog
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Nether fog: ${boolState(NoFogConfig.netherFog)}"))
                        Command.SINGLE_SUCCESS
                    })
                    .then(ClientCommands.literal("end").executes { context ->
                        NoFogConfig.endFog = !NoFogConfig.endFog
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("End fog: ${boolState(NoFogConfig.endFog)}"))
                        Command.SINGLE_SUCCESS
                    })
                    .then(ClientCommands.literal("water").executes { context ->
                        NoFogConfig.waterFog = !NoFogConfig.waterFog
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Water fog: ${boolState(NoFogConfig.waterFog)}"))
                        Command.SINGLE_SUCCESS
                    })
                    .then(ClientCommands.literal("lava").executes { context ->
                        NoFogConfig.lavaFog = !NoFogConfig.lavaFog
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Lava fog: ${boolState(NoFogConfig.lavaFog)}"))
                        Command.SINGLE_SUCCESS
                    })
                    .then(ClientCommands.literal("powder_snow").executes { context ->
                        NoFogConfig.powderSnowFog = !NoFogConfig.powderSnowFog
                        NoFogConfig.save()
                        context.source.sendFeedback(Component.literal("Powder snow fog: ${boolState(NoFogConfig.powderSnowFog)}"))
                        Command.SINGLE_SUCCESS
                    })
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
    }

    private fun boolState(value: Boolean) = if (value) "ON" else "OFF"

    private fun showConfig(sendFeedback: (Component) -> Unit) {
        sendFeedback(Component.literal("--- No Fog Config ---"))
        sendFeedback(Component.literal("Overworld fog: ${boolState(NoFogConfig.overworldFog)}"))
        sendFeedback(Component.literal("Nether fog: ${boolState(NoFogConfig.netherFog)}"))
        sendFeedback(Component.literal("End fog: ${boolState(NoFogConfig.endFog)}"))
        sendFeedback(Component.literal("Water fog: ${boolState(NoFogConfig.waterFog)}"))
        sendFeedback(Component.literal("Lava fog: ${boolState(NoFogConfig.lavaFog)}"))
        sendFeedback(Component.literal("Powder snow fog: ${boolState(NoFogConfig.powderSnowFog)}"))
        sendFeedback(Component.literal("Blindness fog: ${boolState(NoFogConfig.blindnessFog)}"))
        sendFeedback(Component.literal("Darkness fog: ${boolState(NoFogConfig.darknessFog)}"))
    }
}
