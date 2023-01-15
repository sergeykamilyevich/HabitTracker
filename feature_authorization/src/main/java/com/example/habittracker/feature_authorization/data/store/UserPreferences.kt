package com.example.habittracker.feature_authorization.data.store

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.habittracker.feature_authorization.data.crypto.Encryption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences
@Inject constructor(
    private val encryption: Encryption,
    private val dataStore: DataStore<Preferences>
) {

    val accessToken: Flow<CharSequence?>
        get() = dataStore.data.map { preferences ->
            val iv = preferences[ACCESS_TOKEN_IV]
            iv?.let {
                val encryptedToken = preferences[ACCESS_TOKEN]
                encryption.decrypt(
                    encryptedToken?.toByteArrayNoWrap(),
                    iv.toByteArrayNoWrap()
                ).decodeToString()
            }
        }

    suspend fun saveAccessTokens(accessToken: CharSequence?) {
        val accessTokenEncryptionResult =
            encryption.encrypt((accessToken as? String)?.encodeToByteArray())

        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessTokenEncryptionResult.bytes.toStringNoWrap()
            preferences[ACCESS_TOKEN_IV] = accessTokenEncryptionResult.iv.toStringNoWrap()
        }
    }

    private fun String.toByteArrayNoWrap(): ByteArray = Base64.decode(this, Base64.NO_WRAP)
    private fun ByteArray.toStringNoWrap(): String = Base64.encodeToString(this, Base64.NO_WRAP)

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("key_access_token")
        private val ACCESS_TOKEN_IV = stringPreferencesKey("key_access_iv")
    }
}