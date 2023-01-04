package com.example.habittracker.feature_habits.presentation.ui

import androidx.annotation.StringRes

interface HasTitle {

    @StringRes
    fun getTitleResId(): Int
}