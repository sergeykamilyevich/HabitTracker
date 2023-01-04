package com.example.habittracker.feature_habits.presentation.view_models

import android.app.Application
import javax.inject.Inject

interface Resources {

    fun getString(resId: Int, vararg formatArgs: Any?): String

    fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String

    class Base @Inject constructor(private val application: Application) : Resources {
        override fun getString(resId: Int, vararg formatArgs: Any?): String =
            application.resources.getString(resId, *formatArgs)

        override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String =
            application.resources.getQuantityString(id, quantity, *formatArgs)

    }
}