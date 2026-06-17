package de.lenox.client.mixin

import de.lenox.client.NoFogConfig
import de.lenox.client.NoFogMod
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

        val applyWaterOffset = fogType == FogType.WATER && NoFogConfig.waterFog && NoFogConfig.waterFogOffset > 0f
        val applyLavaOffset = fogType == FogType.LAVA && NoFogConfig.lavaFog && NoFogConfig.lavaFogOffset > 0f
        val applyPowderSnowOffset = fogType == FogType.POWDER_SNOW && NoFogConfig.powderSnowFog && NoFogConfig.powderSnowFogOffset > 0f

        val applyOverworldOffset = fogType == FogType.ATMOSPHERIC && level.dimension() == Level.OVERWORLD && NoFogConfig.overworldFogOffset != 1f
        val applyNetherOffset = fogType == FogType.ATMOSPHERIC && level.dimension() == Level.NETHER && NoFogConfig.netherFogOffset != 1f
        val applyEndOffset = fogType == FogType.ATMOSPHERIC && level.dimension() == Level.END && NoFogConfig.endFogOffset != 1f

        if (keepFog && !applyWaterOffset && !applyLavaOffset && !applyPowderSnowOffset && !applyOverworldOffset && !applyNetherOffset && !applyEndOffset) return

        if (entity is LocalPlayer) {
            if (NoFogConfig.blindnessFog && entity.hasEffect(MobEffects.BLINDNESS)) return
            if (NoFogConfig.darknessFog && entity.hasEffect(MobEffects.DARKNESS)) return
        }

        val fogData = cir.returnValue
        NoFogMod.LOGGER.info("Fog type: $fogType")
        NoFogMod.LOGGER.info("applyOverworldOffset: $applyOverworldOffset")
        if (applyWaterOffset || applyLavaOffset || applyPowderSnowOffset) {
            val offset = when {
                applyWaterOffset -> NoFogConfig.waterFogOffset
                applyLavaOffset -> NoFogConfig.lavaFogOffset
                else -> NoFogConfig.powderSnowFogOffset
            }
            val maxDist = if (applyWaterOffset) 1000f else if (applyLavaOffset) 200f else 120f
            fogData.environmentalEnd = (fogData.environmentalEnd + offset).coerceAtMost(maxDist)
            fogData.renderDistanceEnd = (fogData.renderDistanceEnd + offset).coerceAtMost(maxDist)
        } else if (applyOverworldOffset || applyNetherOffset || applyEndOffset) {
            val multiplier = when {
                applyOverworldOffset -> NoFogConfig.overworldFogOffset
                applyNetherOffset -> NoFogConfig.netherFogOffset
                else -> NoFogConfig.endFogOffset
            }
            fogData.environmentalStart *= multiplier
            fogData.environmentalEnd *= multiplier
            fogData.renderDistanceStart *= multiplier
            fogData.renderDistanceEnd *= multiplier
            NoFogMod.LOGGER.info("Overworld fog: ${fogData.environmentalStart} - ${fogData.environmentalEnd} - ${fogData.renderDistanceStart} - ${fogData.renderDistanceEnd}")
            NoFogMod.LOGGER.info("Multiplier: $multiplier")
        } else {
            fogData.environmentalStart = 1e9f
            fogData.environmentalEnd = 1e9f
            fogData.renderDistanceStart = 1e9f
            fogData.renderDistanceEnd = 1e9f
        }
    }
}
