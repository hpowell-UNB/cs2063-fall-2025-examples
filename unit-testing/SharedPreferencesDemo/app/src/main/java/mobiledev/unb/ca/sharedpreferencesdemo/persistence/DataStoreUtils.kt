package mobiledev.unb.ca.sharedpreferencesdemo.persistence

import android.content.Context
import android.content.SharedPreferences

private const val PREFERENCES_NAME = "app_preferences"

fun Context.getAppSharedPreferences(name: String = PREFERENCES_NAME, mode: Int = Context.MODE_PRIVATE): SharedPreferences {
    return getSharedPreferences(name, mode)
}