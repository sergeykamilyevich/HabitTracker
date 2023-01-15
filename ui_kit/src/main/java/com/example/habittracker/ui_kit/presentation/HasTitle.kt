package com.example.habittracker.ui_kit.presentation

import androidx.annotation.StringRes

interface HasTitle {

    @StringRes
    fun getTitleResId(): Int
}