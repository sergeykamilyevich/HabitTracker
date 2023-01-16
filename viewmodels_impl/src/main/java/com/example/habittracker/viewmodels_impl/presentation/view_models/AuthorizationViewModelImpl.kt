package com.example.habittracker.viewmodels_impl.presentation.view_models

import com.example.habittracker.core_api.data.store.UserPreferences
import com.example.habittracker.viewmodels_api.presentation.view_models.AuthorizationViewModel
import javax.inject.Inject

class AuthorizationViewModelImpl @Inject constructor(
    private val preferences: UserPreferences
) : AuthorizationViewModel()
{

    override val accessToken = preferences.accessToken

    override suspend fun saveToken(accessToken: CharSequence) {
        preferences.saveAccessTokens(accessToken)
    }
}