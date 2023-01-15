package com.example.habittracker.core_api.data.store

import kotlinx.coroutines.flow.Flow

interface UserPreferences {

    val accessToken: Flow<CharSequence?>

    suspend fun saveAccessTokens(accessToken: CharSequence?)

    suspend fun clear()
}