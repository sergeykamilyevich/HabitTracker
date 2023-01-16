package com.example.habittracker.viewmodels_api.presentation.tools

import android.content.Context
import javax.inject.Inject

interface Resources {

    fun getString(resId: Int, vararg formatArgs: Any?): String

    fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String

    class Base @Inject constructor(private val context: Context) : Resources {
        override fun getString(resId: Int, vararg formatArgs: Any?): String =
            context.resources.getString(resId, *formatArgs)

        override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String =
            context.resources.getQuantityString(id, quantity, *formatArgs)

    }
}