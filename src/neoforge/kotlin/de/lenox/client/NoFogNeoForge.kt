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
class NoFogNeoForge(container: ModContainer)

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
                .then(Commands.literal("environment").executes { context ->
                    NoFogConfig.environmentFog = !NoFogConfig.environmentFog
                    context.source.sendSystemMessage(Component.literal("Environment fog: ${boolState(NoFogConfig.environmentFog)}"))
                    Command.SINGLE_SUCCESS
                })
                .then(Commands.literal("water").executes { context ->
                    NoFogConfig.waterFog = !NoFogConfig.waterFog
                    context.source.sendSystemMessage(Component.literal("Water fog: ${boolState(NoFogConfig.waterFog)}"))
                    Command.SINGLE_SUCCESS
                })
                .then(Commands.literal("lava").executes { context ->
                    NoFogConfig.lavaFog = !NoFogConfig.lavaFog
                    context.source.sendSystemMessage(Component.literal("Lava fog: ${boolState(NoFogConfig.lavaFog)}"))
                    Command.SINGLE_SUCCESS
                })
                .then(Commands.literal("powder_snow").executes { context ->
                    NoFogConfig.powderSnowFog = !NoFogConfig.powderSnowFog
                    context.source.sendSystemMessage(Component.literal("Powder snow fog: ${boolState(NoFogConfig.powderSnowFog)}"))
                    Command.SINGLE_SUCCESS
                })
                .then(Commands.literal("blindness").executes { context ->
                    NoFogConfig.blindnessFog = !NoFogConfig.blindnessFog
                    context.source.sendSystemMessage(Component.literal("Blindness fog: ${boolState(NoFogConfig.blindnessFog)}"))
                    Command.SINGLE_SUCCESS
                })
                .then(Commands.literal("darkness").executes { context ->
                    NoFogConfig.darknessFog = !NoFogConfig.darknessFog
                    context.source.sendSystemMessage(Component.literal("Darkness fog: ${boolState(NoFogConfig.darknessFog)}"))
                    Command.SINGLE_SUCCESS
                })
        )
    }

    private fun boolState(value: Boolean) = if (value) "ON" else "OFF"

    private fun showConfig(sendMessage: (Component) -> Unit) {
        sendMessage(Component.literal("--- No Fog Config ---"))
        sendMessage(Component.literal("Environment fog: ${boolState(NoFogConfig.environmentFog)}"))
        sendMessage(Component.literal("Water fog: ${boolState(NoFogConfig.waterFog)}"))
        sendMessage(Component.literal("Lava fog: ${boolState(NoFogConfig.lavaFog)}"))
        sendMessage(Component.literal("Powder snow fog: ${boolState(NoFogConfig.powderSnowFog)}"))
        sendMessage(Component.literal("Blindness fog: ${boolState(NoFogConfig.blindnessFog)}"))
        sendMessage(Component.literal("Darkness fog: ${boolState(NoFogConfig.darknessFog)}"))
    }
}
