package mobiledev.unb.ca.preferencesdatastoreexample

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object SharedPreferencesManager {
    private lateinit var dataStore: DataStore<Preferences>

    private fun getIntKeyName(key: String): Preferences.Key<Int> {
        return intPreferencesKey(key)
    }

    suspend fun saveIntValue(key: String, value: Int) {
        dataStore.edit {  preferences ->
            preferences[getIntKeyName(key)] = value
        }
    }

    suspend fun getIntValue(key: String): Int {
        // Collect the first emitted value and then complete
        return dataStore.data
            .map { preferences ->
                preferences[getIntKeyName(key)]
            }
            .first() ?: 0
    }

    suspend fun clearIntValue(key: String) {
        dataStore.edit { preferences -> preferences.remove(getIntKeyName(key)) }
    }

    fun init(dataStore: DataStore<Preferences>) {
        this.dataStore = dataStore
    }
}
