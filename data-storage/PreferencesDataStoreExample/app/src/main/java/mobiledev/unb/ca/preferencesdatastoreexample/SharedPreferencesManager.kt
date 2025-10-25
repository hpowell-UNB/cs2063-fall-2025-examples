package mobiledev.unb.ca.preferencesdatastoreexample

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.myDataStore by preferencesDataStore(name = "app_preferences")

class SharedPreferencesManager(private val context: Context) {
    // Create keys to store and retrieve the data
    companion object {
        val HIGH_SCORE_KEY = intPreferencesKey("HIGH_SCORE")
    }

    // Save the high score value
    suspend fun saveHighScore(score: Int) {
        context.myDataStore.edit { preferences ->
            preferences[HIGH_SCORE_KEY] = score
        }
    }

    suspend fun getHighScore(): Int {
        // Collect the first emitted value and then complete
        return context.myDataStore.data
            .map { preferences ->
                preferences[HIGH_SCORE_KEY]
            }
            .first() ?: 0
    }

    // Clear the high score value
    suspend fun clearHighScore() {
        context.myDataStore.edit { preferences -> preferences.remove(HIGH_SCORE_KEY) }
    }
}
