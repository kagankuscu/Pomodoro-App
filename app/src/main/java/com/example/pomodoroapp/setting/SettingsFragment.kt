package com.example.pomodoroapp.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.pomodoroapp.R
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val pre = preferenceScreen.sharedPreferences.all

        
        Timber.i(pre.toString())
        Timber.i(pre.keys.toString())
        Timber.i(pre["work_time"].toString())
    }
}