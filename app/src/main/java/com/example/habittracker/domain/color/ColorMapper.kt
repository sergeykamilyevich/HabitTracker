package com.example.habittracker.domain.color

import android.graphics.Color

class ColorMapper {

    fun mapHueToColor(hue: Float): Int = Color.HSVToColor(floatArrayOf(hue, 1F, 1F))

    fun mapColorToRgbHsv(color: Int): ColorRgbHsv {
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