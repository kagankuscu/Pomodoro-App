package com.example.pomodoroapp.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoroapp.R
import com.example.pomodoroapp.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

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

        return binding.root
    }
}