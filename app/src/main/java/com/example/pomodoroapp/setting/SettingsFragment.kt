package com.example.pomodoroapp.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.pomodoroapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}