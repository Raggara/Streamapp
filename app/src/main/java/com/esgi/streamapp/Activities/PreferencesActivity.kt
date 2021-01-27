package com.esgi.streamapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.esgi.streamapp.R

class PreferencesActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preferences_layout)

        // Get the preferences
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        // Get the user dark theme settings
        val isDarkTheme = prefs.getBoolean("darktheme",true)
        val isPipMode: Boolean = prefs.getBoolean("pipMode", true)
        val isCellular: Boolean = prefs.getBoolean("cellular", true)

       supportFragmentManager
                .beginTransaction()
                .replace(R.id.pref_lay, PreferencesFragment())
                .commit()
    }
}