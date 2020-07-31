package com.example.pomodoroapp.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                    binding.btnStart.text = getString(R.string.resume_btn)
                    binding.btnStop.isEnabled = true
                    binding.btnStop.visibility = View.VISIBLE
                } else {
                    binding.btnStart.text = getString(R.string.start_btn)
                    binding.btnStop.isEnabled = false
                    binding.btnStop.visibility = View.INVISIBLE
                }
            }
        })

        timerViewModel.timerString.observe(viewLifecycleOwner, Observer {timerText ->
            timerText?.let {
                binding.timerText.text = timerText
            }
        })

        timerViewModel.resetTimer.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    binding.timerText.text = getString(R.string.timer_0)
                    timerViewModel.resetTimerCompleted()
                }
            }
        })

        return binding.root
    }
}