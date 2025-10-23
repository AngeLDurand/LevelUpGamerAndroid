package com.example.levelupgamer.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.levelupgamer.model.CartLinePersist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


private val Context.cartDataStore by preferencesDataStore(name = "cart")

class CartLocal(private val appContext: Context) {

    private val CART_KEY = stringPreferencesKey("cart_json")
    private val json = Json { ignoreUnknownKeys = true }

    fun flow(): Flow<List<CartLinePersist>> =
        appContext.cartDataStore.data.map { prefs ->
            prefs[CART_KEY]?.let { json.decodeFromString(it) } ?: emptyList()
        }

    suspend fun save(lines: List<CartLinePersist>) {
        val encoded = json.encodeToString(lines)
        appContext.cartDataStore.edit { it[CART_KEY] = encoded }
    }

    suspend fun clear() {
        appContext.cartDataStore.edit { it.remove(CART_KEY) }
    }
}