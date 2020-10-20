package com.example.pomodoroapp.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.pomodoroapp.R
import com.example.pomodoroapp.timer.TimerViewModel
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat() {
    private var prefs: SharedPreferences? = null
    private val FIRST_RUN = "first_run"

//    private var darkMode: SwitchPreference? = null
    private var keepScreenOn: SwitchPreference? = null
    private var workTime: ListPreference? = null
    private var shortBreak: ListPreference? = null
    private var longBreak: ListPreference? = null

    private lateinit var timerViewModel: TimerViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        timerViewModel = ViewModelProvider(requireActivity()).get(TimerViewModel::class.java)
        prefs = preferenceManager.sharedPreferences

        setPreferences()
    }

    private fun firstRun() {
        if (!prefs?.getBoolean(FIRST_RUN, false)!!) {
            prefs?.edit()?.putBoolean(FIRST_RUN, true)?.apply()
            setDefaultValue()
        } else {
            setClickListener()
        }
    }

    private fun setPreferences() {
//        darkMode =
//            preferenceScreen.findPreference<SwitchPreference>(getString(R.string.key_mode))
        keepScreenOn =
            preferenceScreen.findPreference<SwitchPreference>(getString(R.string.key_keep_screen_on))
        workTime =
            preferenceScreen.findPreference<ListPreference>(getString(R.string.key_work_time))
        shortBreak =
            preferenceScreen.findPreference<ListPreference>(getString(R.string.key_short_break))
        longBreak =
            preferenceScreen.findPreference<ListPreference>(getString(R.string.key_long_break))

        firstRun()
    }

    private fun setDefaultValue() {
        workTime?.setValueIndex(4)
        shortBreak?.setValueIndex(0)
        longBreak?.setValueIndex(2)

        setClickListener()
    }

    private fun setClickListener() {

//        darkMode?.setOnPreferenceChangeListener { preference, newValue ->
//            Timber.d("preferences name:$preference, new value $newValue")
//            true
//        }

        keepScreenOn?.setOnPreferenceChangeListener { preference, newValue ->
            Timber.d("preference name: $preference, newValue: $newValue")
            timerViewModel.setKeepScreenOff()

            true
        }

        workTime?.setOnPreferenceChangeListener { preference, newValue ->
            Timber.d("preference name: ${preference}, newValue:${newValue}")
            if (newValue is String) {
//                timerViewModel.setWorkTime(newValue.toLong())
                Timber.d("set the work time")
            }
            true
        }

        shortBreak?.setOnPreferenceChangeListener { preference, newValue ->
            Timber.d("preference name: ${preference}, newValue:${newValue}")
            if (newValue is Long) timerViewModel.setShortBreak(newValue.toLong())
            true
        }

        longBreak?.setOnPreferenceChangeListener { preference, newValue ->
            Timber.d("preference name: ${preference}, newValue:${newValue}")
            if (newValue is Long) timerViewModel.setLongBreak(newValue.toLong())
            true
        }
    }
}