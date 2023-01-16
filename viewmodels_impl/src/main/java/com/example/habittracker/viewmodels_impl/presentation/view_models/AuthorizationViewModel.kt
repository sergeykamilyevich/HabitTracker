package com.example.habittracker.viewmodels_impl.presentation.view_models

import androidx.lifecycle.ViewModel
import com.example.habittracker.core_api.data.store.UserPreferences
import javax.inject.Inject

class AuthorizationViewModel @Inject constructor(
    private val preferences: UserPreferences
) : ViewModel()
{

    val accessToken = preferences.accessToken

    suspend fun saveToken(accessToken: CharSequence) {
        preferences.saveAccessTokens(accessToken)
    }
}