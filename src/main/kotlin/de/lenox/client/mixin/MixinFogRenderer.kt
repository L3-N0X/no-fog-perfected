package de.lenox.client.mixin

import de.lenox.client.NoFogConfig
import net.minecraft.client.Camera
import net.minecraft.client.DeltaTracker
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.fog.FogData
import net.minecraft.client.renderer.fog.FogRenderer
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.FogType
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(value = [FogRenderer::class], priority = 900)
abstract class MixinFogRenderer {
    @Shadow
    abstract fun getFogType(camera: Camera): FogType

    @Inject(method = ["setupFog"], at = [At("RETURN")])
    private fun modifyFog(
        camera: Camera,
        renderDistanceInChunks: Int,
        deltaTracker: DeltaTracker,
        darkenWorldAmount: Float,
        level: ClientLevel,
        cir: CallbackInfoReturnable<FogData>
    ) {
        val fogType = this.getFogType(camera)
        val entity = camera.entity()

        val keepFog = when (fogType) {
            FogType.WATER -> NoFogConfig.waterFog
            FogType.LAVA -> NoFogConfig.lavaFog
            FogType.POWDER_SNOW -> NoFogConfig.powderSnowFog
            else -> when (level.dimension()) {
                Level.OVERWORLD -> NoFogConfig.overworldFog
                Level.NETHER -> NoFogConfig.netherFog
                Level.END -> NoFogConfig.endFog
                else -> false
            }
        }
        if (keepFog) return

        if (entity is LocalPlayer) {
            if (NoFogConfig.blindnessFog && entity.hasEffect(MobEffects.BLINDNESS)) return
            if (NoFogConfig.darknessFog && entity.hasEffect(MobEffects.DARKNESS)) return
        }

        val fogData = cir.returnValue
        fogData.environmentalStart = 1e9f
        fogData.environmentalEnd = 1e9f
        fogData.renderDistanceStart = 1e9f
        fogData.renderDistanceEnd = 1e9f
    }
}
