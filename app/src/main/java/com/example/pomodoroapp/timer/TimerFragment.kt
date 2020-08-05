package com.example.pomodoroapp.timer

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoroapp.R
import com.example.pomodoroapp.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTimerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_timer, container, false
        )

        val timerViewModel = ViewModelProvider(this).get(TimerViewModel::class.java)

        binding.lifecycleOwner = this

        binding.timerViewModel = timerViewModel

        timerViewModel.startTimerStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    binding.btnStart.text = getString(R.string.pause_btn)
                    binding.btnSkip.visibility = View.VISIBLE
                    binding.btnReset.isEnabled = true
                    binding.infoText.visibility = View.VISIBLE
                } else {
                    binding.btnStart.text = getString(R.string.start_btn)
                    binding.btnSkip.visibility = View.INVISIBLE
                    binding.infoText.visibility = View.INVISIBLE
                }
            }
        })

        timerViewModel.pauseTimerStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    binding.btnStart.text = getString(R.string.resume_btn)
                } else {
                    binding.btnStart.text = getString(R.string.pause_btn)
                }
            }
        })

        timerViewModel.timerString.observe(viewLifecycleOwner, Observer { timerText ->
            timerText?.let {
                binding.timerText.text = timerText
            }
        })

        timerViewModel.resetTimerStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    binding.btnReset.isEnabled = false
                    timerViewModel.resetTimerCompleted()
                }
            }
        })

        timerViewModel.infoText.observe(viewLifecycleOwner, Observer { info ->
            info?.let {
                binding.infoText.text = info
            }
        })

        val vibe: Vibrator? = getSystemService(requireContext(), Vibrator::class.java)

        timerViewModel.vibration.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibe?.vibrate(VibrationEffect.createOneShot(500L, 255))
                        timerViewModel.vibrationCompleted()
                    }
                }
            }
        })

        return binding.root
    }
}