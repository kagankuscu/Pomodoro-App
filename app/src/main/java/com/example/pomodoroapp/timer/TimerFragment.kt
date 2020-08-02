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
                    binding.btnStart.text = getString(R.string.pause_btn)
                    binding.btnSkip.visibility = View.VISIBLE
                    binding.btnReset.isEnabled = true
                } else {
                    binding.btnStart.text = getString(R.string.start_btn)
                    binding.btnSkip.visibility = View.INVISIBLE
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
                    binding.timerText.text = getString(R.string.timer_0)
                    timerViewModel.resetTimerCompleted()
                } else {
                    binding.btnReset.isEnabled = false
                }
            }
        })

        return binding.root
    }
}