package de.lenox.client

import kotlin.math.pow
import kotlin.math.sqrt

object FogSliderFormulas {
    const val FLUID_FOG_SLIDER_MIN = 0
    const val FLUID_FOG_SLIDER_MAX = 100
    const val DIMENSION_FOG_SLIDER_MIN = 0
    const val DIMENSION_FOG_SLIDER_MAX = 100

    const val WATER_MAX_DISTANCE = 1000f
    const val LAVA_MAX_DISTANCE = 200f
    const val POWDER_SNOW_MAX_DISTANCE = 120f
    const val DIMENSION_MAX_DISTANCE = 800f

    fun sliderToFluidOffset(slider: Int, maxDistance: Float): Float {
        return ((slider.toDouble() / 100.0).pow(2) * maxDistance).toFloat()
    }

    fun fluidOffsetToSlider(offset: Float, maxDistance: Float): Int {
        return (sqrt(offset.toDouble() / maxDistance) * 100.0).toInt()
    }

    fun sliderToDimensionOffset(slider: Int): Float {
        return ((slider.toDouble() / 100.0).pow(2) * DIMENSION_MAX_DISTANCE).toFloat()
    }

    fun dimensionOffsetToSlider(offset: Float): Int {
        return (sqrt(offset.toDouble() / DIMENSION_MAX_DISTANCE) * 100.0).toInt()
    }
}
