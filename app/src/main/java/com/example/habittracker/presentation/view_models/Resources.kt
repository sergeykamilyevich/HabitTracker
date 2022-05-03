package com.example.habittracker.presentation.view_models

import android.app.Application
import android.content.res.Resources.NotFoundException
import javax.inject.Inject

class Resources @Inject constructor(private val application: Application) {

    fun getString(resId: Int, vararg formatArgs: Any?): String =
        application.resources.getString(resId, *formatArgs)

    fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String =
        application.resources.getQuantityString(id, quantity, *formatArgs)

}