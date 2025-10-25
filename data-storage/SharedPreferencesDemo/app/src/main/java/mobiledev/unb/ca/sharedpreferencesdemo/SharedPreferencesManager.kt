package mobiledev.unb.ca.sharedpreferencesdemo

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

// An object declaration is a concise way of creating a singleton class
// without the need to define a class and a companion object.
object SharedPreferencesManager {
    private const val PREF_NAME = "AppPrefs"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveIntValue(key: String, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }

    fun getIntValue(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }
}