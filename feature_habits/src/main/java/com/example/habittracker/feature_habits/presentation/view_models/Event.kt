package com.example.habittracker.feature_habits.presentation.view_models

open class Event<out T>(private val data: T) {

    private var hasBeenHandled = false

    fun transferIfNotHandled(): T? = if (hasBeenHandled) null
    else {
        hasBeenHandled = true
        data
    }
}