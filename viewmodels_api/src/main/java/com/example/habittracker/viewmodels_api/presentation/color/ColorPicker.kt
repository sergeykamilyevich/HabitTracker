package com.example.habittracker.viewmodels_api.presentation.color

import android.graphics.Color
import androidx.annotation.ColorInt
import javax.inject.Inject

class ColorPicker @Inject constructor() {

    private val squareSizeInHue =
        ((HUE_MAX - HUE_MIN) / (NUMBER_OF_COLORS
                * (1 + (FREE_SPACE_SIZE_IN_PERCENTS / SQUARE_SIZE_IN_PERCENTS))))

    private val freeSpaceSizeInHue =
        squareSizeInHue * FREE_SPACE_SIZE_IN_PERCENTS / SQUARE_SIZE_IN_PERCENTS

    @ColorInt
    fun colors(): IntArray {
        val colors = IntArray(NUMBER_OF_COLORS)
        val positionOfFirstSquare = ((freeSpaceSizeInHue + squareSizeInHue) / 2)
        for (i in colors.indices) {
            val hue = (i * (freeSpaceSizeInHue + squareSizeInHue) + positionOfFirstSquare)
            colors[i] = mapHueToColor(hue)
        }
        return colors
    }

    @ColorInt
    fun gradientColors(): IntArray {
        val colors = IntArray(NUMBER_OF_COLORS + 1)
        for (i in colors.indices) {
            val hue = (i * (freeSpaceSizeInHue + squareSizeInHue))
            colors[i] = mapHueToColor(hue)
        }
        return colors
    }

    private fun mapHueToColor(hue: Float): Int = Color.HSVToColor(floatArrayOf(hue, 1F, 1F))

    companion object {
        private const val HUE_MIN = 0F
        private const val HUE_MAX = 360F
        private const val NUMBER_OF_COLORS = 16
        private const val FREE_SPACE_SIZE_IN_PERCENTS = 40F
        private const val SQUARE_SIZE_IN_PERCENTS = 100F
    }
}