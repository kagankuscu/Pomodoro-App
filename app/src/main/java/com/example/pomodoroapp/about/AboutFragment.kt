package com.example.pomodoroapp.about

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoroapp.R
import com.example.pomodoroapp.databinding.FragmentAboutBinding
import timber.log.Timber

class AboutFragment : Fragment() {

    private val urlGithub = "https://github.com/kagankuscu/Pomodoro-App"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentAboutBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_about, container, false
        )

        val viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.version.text = getAppVersion()

        binding.btnGithub.setOnClickListener {
            val uri = Uri.parse("$urlGithub")
            val i = Intent(Intent.ACTION_VIEW, uri)
            startActivity(i)
        }

        return binding.root
    }

    private fun getAppVersion(): String {
        var version = ""
        try {
            val pInfo = context?.packageManager?.getPackageInfo(requireContext().packageName, 0)
            version = pInfo!!.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return version
    }
}