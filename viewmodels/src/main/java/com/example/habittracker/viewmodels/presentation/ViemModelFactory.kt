package com.example.habittracker.viewmodels.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

//@ApplicationScope
//class ViewModelFactory @Inject constructor(
//    private val viewModelProviders: @JvmSuppressWildcards Map<Class<*>, Provider<Any>>,
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return viewModelProviders[modelClass]?.get() as T
//    }
//}