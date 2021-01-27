package com.esgi.streamapp.Activities

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.esgi.streamapp.R

class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}