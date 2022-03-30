package com.example.habittracker.presentation.view_models

open class Event<out T>(private val data: T) {

    private var hasBeenHandled = false
//        private set

    fun transferIfNotHandled(): T? = if (hasBeenHandled) null
    else {
        hasBeenHandled = true
        data
    }
}