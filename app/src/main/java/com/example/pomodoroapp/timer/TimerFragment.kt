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
import com.example.pomodoroapp.notification.TimerNotification
import com.example.pomodoroapp.notification.TimerNotification.NotificationID.TIMER_ID
import com.example.pomodoroapp.notification.TimerNotification.NotificationID.TIMER_NOTIFICATION
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.banner.BannerView

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
        val vibe: Vibrator? = getSystemService(requireContext(), Vibrator::class.java)
        val myNotification = TimerNotification(requireContext())

        // Huawei Ads variable
        val adParam: AdParam = AdParam.Builder().build()
        val bannerView: BannerView = BannerView(context)

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

        // TODO: 9/12/2020 Refactor TimerViewModal has hard coded change from there to here and use string values getString() method.
        timerViewModel.infoText.observe(viewLifecycleOwner, Observer { info ->
            info?.let {
                binding.infoText.text = info
            }
        })

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


        bannerView.adId = "testw6vs28auh3"
        if (Build.MANUFACTURER == "HUAWEI")
            bannerView.loadAd(adParam)

        binding.constraintLayout.addView(bannerView)

        myNotification.show("App Started")
        return binding.root
    }
}