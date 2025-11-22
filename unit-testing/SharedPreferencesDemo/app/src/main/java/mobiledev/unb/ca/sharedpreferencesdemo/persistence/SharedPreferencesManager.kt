package mobiledev.unb.ca.sharedpreferencesdemo.persistence

import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPreferencesManager {
    private lateinit var sharedPreferences: SharedPreferences

    fun saveIntValue(key: String, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }

    fun getIntValue(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun init(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }
}