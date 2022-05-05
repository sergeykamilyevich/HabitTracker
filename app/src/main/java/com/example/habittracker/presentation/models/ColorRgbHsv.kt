package com.example.habittracker.presentation.models

import android.graphics.Color
import androidx.annotation.ColorInt

data class ColorRgbHsv(
    val red: Int,
    val green: Int,
    val blue: Int,
    val hue: Int,
    val saturation: Int,
    val value: Int
) {

    companion object {
        fun fromColor(@ColorInt color: Int): ColorRgbHsv {
            val hsv = FloatArray(3)
            Color.colorToHSV(color, hsv)
            return ColorRgbHsv(
                red = Color.red(color),
                green = Color.green(color),
                blue = Color.blue(color),
                hue = hsv[0].toInt(),
                saturation = (hsv[1] * 100).toInt(),
                value = (hsv[2] * 100).toInt()
            )
        }
    }
}