package com.example.habittracker.feature_habits.di.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

class ViewModelComponent : ViewModel() {

    var component: Any? = null

    override fun onCleared() {
        super.onCleared()
        component = null
    }
}

fun <T> ViewModelStoreOwner.getComponent(createComponent: () -> T): T {
    val viewModel = ViewModelProvider(this)[ViewModelComponent::class.java]
    if (viewModel.component == null) viewModel.component = createComponent()
    @Suppress("UNCHECKED_CAST")
    return viewModel.component as T
}