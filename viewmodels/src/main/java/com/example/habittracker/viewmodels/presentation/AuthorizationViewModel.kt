package com.example.habittracker.viewmodels.presentation

import androidx.lifecycle.ViewModel
import com.example.habittracker.core_api.data.store.UserPreferences
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class AuthorizationViewModel @Inject constructor(
    private val preferences: UserPreferences
) : ViewModel()
{

    suspend fun saveToken(accessToken: CharSequence) {
        preferences.saveAccessTokens(accessToken)
    }
}