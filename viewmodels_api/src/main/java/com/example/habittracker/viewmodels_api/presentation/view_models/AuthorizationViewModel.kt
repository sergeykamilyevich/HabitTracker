package com.example.habittracker.viewmodels_api.presentation.view_models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

abstract class AuthorizationViewModel : ViewModel() {

    abstract val accessToken: Flow<CharSequence?>

    abstract suspend fun saveToken(accessToken: CharSequence)
}