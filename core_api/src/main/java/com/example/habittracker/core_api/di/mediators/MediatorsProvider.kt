package com.example.habittracker.core_api.di.mediators

import javax.inject.Provider

interface MediatorsProvider {

    fun mediatorsMap(): Map<Class<*>, @JvmSuppressWildcards Provider<Any>>
}