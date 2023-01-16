package com.example.habittracker.viewmodels_impl.presentation.view_models

import androidx.lifecycle.ViewModel
import com.example.habittracker.viewmodels_api.presentation.view_models.ViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactoryImpl @Inject constructor(
    private val viewModelProviders: @JvmSuppressWildcards Map<Class<*>, Provider<Any>>,
) : ViewModelFactory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return viewModelProviders[modelClass]?.get() as T
    }
}