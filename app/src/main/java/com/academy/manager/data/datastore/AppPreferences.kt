package com.academy.manager.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "academy_preferences")

data class AppPreferencesState(
    val adminPinHash: String? = null,
    val pinSalt: String? = null,
    val darkMode: Boolean = false,
    val nextReceiptNo: Int = 1
) {
    val isPinConfigured: Boolean
        get() = !adminPinHash.isNullOrBlank() && !pinSalt.isNullOrBlank()
}

class AppPreferences(private val context: Context) {

    private object Keys {
        val ADMIN_PIN_HASH = stringPreferencesKey("admin_pin_hash")
        val PIN_SALT = stringPreferencesKey("pin_salt")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val NEXT_RECEIPT_NO = intPreferencesKey("next_receipt_no")
    }

    val stateFlow: Flow<AppPreferencesState> = context.dataStore.data.map { prefs ->
        AppPreferencesState(
            adminPinHash = prefs[Keys.ADMIN_PIN_HASH],
            pinSalt = prefs[Keys.PIN_SALT],
            darkMode = prefs[Keys.DARK_MODE] ?: false,
            nextReceiptNo = prefs[Keys.NEXT_RECEIPT_NO] ?: 1
        )
    }

    suspend fun getState(): AppPreferencesState = stateFlow.first()

    suspend fun savePin(hash: String, salt: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ADMIN_PIN_HASH] = hash
            prefs[Keys.PIN_SALT] = salt
        }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DARK_MODE] = enabled
        }
    }

    suspend fun consumeNextReceiptNo(): Int {
        var assigned = 1
        context.dataStore.edit { prefs ->
            assigned = prefs[Keys.NEXT_RECEIPT_NO] ?: 1
            prefs[Keys.NEXT_RECEIPT_NO] = assigned + 1
        }
        return assigned
    }
}
