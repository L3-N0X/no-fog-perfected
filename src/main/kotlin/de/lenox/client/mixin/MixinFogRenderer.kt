package de.lenox.client.mixin

import de.lenox.client.FogSliderFormulas
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
//? if <26.1 {
/*import com.llamalad7.mixinextras.sugar.Local
*///?}

@Mixin(value = [FogRenderer::class], priority = 900)
abstract class MixinFogRenderer {
    @Shadow
    abstract fun getFogType(camera: Camera): FogType

    //? if >=26.1 {
    @Inject(method = ["setupFog"], at = [At("RETURN")])
    private fun modifyFog(
        camera: Camera,
        renderDistanceInChunks: Int,
        deltaTracker: DeltaTracker,
        darkenWorldAmount: Float,
        level: ClientLevel,
        cir: CallbackInfoReturnable<FogData>
    ) {
        modifyFogImpl(cir.returnValue, camera, level)
    }
    //?}
    //? if <26.1 {
    /*@Inject(method = ["setupFog"], at = [At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;getDevice()Lcom/mojang/blaze3d/systems/GpuDevice;", shift = At.Shift.BEFORE)])
    private fun modifyFog(
        camera: Camera,
        renderDistanceInChunks: Int,
        deltaTracker: DeltaTracker,
        darkenWorldAmount: Float,
        level: ClientLevel,
        cir: CallbackInfoReturnable<org.joml.Vector4f>,
        @Local fogData: FogData
    ) {
        modifyFogImpl(fogData, camera, level)
    }
    *///?}

    private fun modifyFogImpl(fogData: FogData, camera: Camera, level: ClientLevel) {
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
        val applyPowderSnowOffset =
            fogType == FogType.POWDER_SNOW && NoFogConfig.powderSnowFog && NoFogConfig.powderSnowFogOffset > 0f

        val applyOverworldOffset =
            fogType == FogType.ATMOSPHERIC && level.dimension() == Level.OVERWORLD && NoFogConfig.overworldFog && NoFogConfig.overworldFogMultiplier > 1.0
        val applyNetherOffset =
            fogType == FogType.ATMOSPHERIC && level.dimension() == Level.NETHER && NoFogConfig.netherFog && NoFogConfig.netherFogOffset > 0f
        val applyEndOffset =
            fogType == FogType.ATMOSPHERIC && level.dimension() == Level.END && NoFogConfig.endFog && NoFogConfig.endFogMultiplier > 1.0

        if (keepFog && !applyWaterOffset && !applyLavaOffset && !applyPowderSnowOffset && !applyOverworldOffset && !applyNetherOffset && !applyEndOffset) return

        if (entity is LocalPlayer) {
            if (entity.hasEffect(MobEffects.BLINDNESS)) {
                if (NoFogConfig.blindnessFog) {
                    val offset = NoFogConfig.blindnessFogOffset
                    if (offset > 0f) {
                        fogData.environmentalEnd = (fogData.environmentalEnd + offset).coerceAtMost(FogSliderFormulas.BLINDNESS_MAX_DISTANCE)
                        fogData.renderDistanceEnd = (fogData.renderDistanceEnd + offset).coerceAtMost(FogSliderFormulas.BLINDNESS_MAX_DISTANCE)
                    }
                    return
                }
            }
            if (entity.hasEffect(MobEffects.DARKNESS)) {
                if (NoFogConfig.darknessFog) {
                    val offset = NoFogConfig.darknessFogOffset
                    if (offset > 0f) {
                        fogData.environmentalEnd = (fogData.environmentalEnd + offset).coerceAtMost(FogSliderFormulas.DARKNESS_MAX_DISTANCE)
                        fogData.renderDistanceEnd = (fogData.renderDistanceEnd + offset).coerceAtMost(FogSliderFormulas.DARKNESS_MAX_DISTANCE)
                    }
                    return
                }
            }
        }

        if (applyWaterOffset || applyLavaOffset || applyPowderSnowOffset) {
            val offset = when {
                applyWaterOffset -> NoFogConfig.waterFogOffset
                applyLavaOffset -> NoFogConfig.lavaFogOffset
                else -> NoFogConfig.powderSnowFogOffset
            }
            val maxDist = when {
                applyWaterOffset -> FogSliderFormulas.WATER_MAX_DISTANCE
                applyLavaOffset -> FogSliderFormulas.LAVA_MAX_DISTANCE
                else -> FogSliderFormulas.POWDER_SNOW_MAX_DISTANCE
            }
            fogData.environmentalEnd = (fogData.environmentalEnd + offset).coerceAtMost(maxDist)
            fogData.renderDistanceEnd = (fogData.renderDistanceEnd + offset).coerceAtMost(maxDist)
        } else if (applyNetherOffset) {
            fogData.environmentalStart = (fogData.environmentalStart + NoFogConfig.netherFogOffset / 2f)
            fogData.environmentalEnd = (fogData.environmentalEnd + NoFogConfig.netherFogOffset)
            fogData.renderDistanceStart = (fogData.renderDistanceStart + NoFogConfig.netherFogOffset / 2f)
            fogData.renderDistanceEnd = (fogData.renderDistanceEnd + NoFogConfig.netherFogOffset)
        } else if (applyOverworldOffset || applyEndOffset) {
            val multiplier = when {
                applyOverworldOffset -> NoFogConfig.overworldFogMultiplier.toFloat()
                else -> NoFogConfig.endFogMultiplier.toFloat()
            }
            fogData.environmentalStart *= multiplier + 0 * (multiplier - 1)
            fogData.environmentalEnd *= multiplier
            fogData.renderDistanceStart *= multiplier + 0 * (multiplier - 1)
            fogData.renderDistanceEnd *= multiplier
        } else {
            fogData.environmentalStart = 1e9f
            fogData.environmentalEnd = 1e9f
            fogData.renderDistanceStart = 1e9f
            fogData.renderDistanceEnd = 1e9f
        }
    }
}
