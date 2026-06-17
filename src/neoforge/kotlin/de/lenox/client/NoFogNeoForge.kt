package de.lenox.client

import com.mojang.brigadier.Command
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent
import net.neoforged.fml.ModContainer

@Mod("no_fog_perfected")
class NoFogNeoForge(container: ModContainer) {
    init {
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
                .then(Commands.literal("overworld").executes { context ->
                    NoFogConfig.overworldFog = !NoFogConfig.overworldFog
                    NoFogConfig.save()
                    context.source.sendSystemMessage(Component.literal("Overworld fog: ${boolState(NoFogConfig.overworldFog)}"))
                    Command.SINGLE_SUCCESS
                })
                .then(Commands.literal("nether").executes { context ->
                    NoFogConfig.netherFog = !NoFogConfig.netherFog
                    NoFogConfig.save()
                    context.source.sendSystemMessage(Component.literal("Nether fog: ${boolState(NoFogConfig.netherFog)}"))
                    Command.SINGLE_SUCCESS
                })
                .then(Commands.literal("end").executes { context ->
                    NoFogConfig.endFog = !NoFogConfig.endFog
                    NoFogConfig.save()
                    context.source.sendSystemMessage(Component.literal("End fog: ${boolState(NoFogConfig.endFog)}"))
                    Command.SINGLE_SUCCESS
                })
                .then(Commands.literal("water").executes { context ->
                    NoFogConfig.waterFog = !NoFogConfig.waterFog
                    NoFogConfig.save()
                    context.source.sendSystemMessage(Component.literal("Water fog: ${boolState(NoFogConfig.waterFog)}"))
                    Command.SINGLE_SUCCESS
                })
                .then(Commands.literal("lava").executes { context ->
                    NoFogConfig.lavaFog = !NoFogConfig.lavaFog
                    NoFogConfig.save()
                    context.source.sendSystemMessage(Component.literal("Lava fog: ${boolState(NoFogConfig.lavaFog)}"))
                    Command.SINGLE_SUCCESS
                })
                .then(Commands.literal("powder_snow").executes { context ->
                    NoFogConfig.powderSnowFog = !NoFogConfig.powderSnowFog
                    NoFogConfig.save()
                    context.source.sendSystemMessage(Component.literal("Powder snow fog: ${boolState(NoFogConfig.powderSnowFog)}"))
                    Command.SINGLE_SUCCESS
                })
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
        sendMessage(Component.literal("Overworld fog: ${boolState(NoFogConfig.overworldFog)}"))
        sendMessage(Component.literal("Nether fog: ${boolState(NoFogConfig.netherFog)}"))
        sendMessage(Component.literal("End fog: ${boolState(NoFogConfig.endFog)}"))
        sendMessage(Component.literal("Water fog: ${boolState(NoFogConfig.waterFog)}"))
        sendMessage(Component.literal("Lava fog: ${boolState(NoFogConfig.lavaFog)}"))
        sendMessage(Component.literal("Powder snow fog: ${boolState(NoFogConfig.powderSnowFog)}"))
        sendMessage(Component.literal("Blindness fog: ${boolState(NoFogConfig.blindnessFog)}"))
        sendMessage(Component.literal("Darkness fog: ${boolState(NoFogConfig.darknessFog)}"))
    }
}
