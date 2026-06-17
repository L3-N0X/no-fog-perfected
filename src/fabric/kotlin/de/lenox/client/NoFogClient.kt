package de.lenox.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback

class NoFogClient : ClientModInitializer {
    override fun onInitializeClient() {
        NoFogConfig.init()

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            NoFogFabricCommands.register(dispatcher)
        }
    }
}
