package mobiledev.unb.ca.preferencesdatastoreexample

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val PREFERENCES_NAME = "app_preferences"

val Context.appPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)
