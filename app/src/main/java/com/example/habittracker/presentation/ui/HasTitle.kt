package com.example.habittracker.presentation.ui

import androidx.annotation.StringRes

interface HasTitle {

    @StringRes
    fun getTitleResId(): Int
}