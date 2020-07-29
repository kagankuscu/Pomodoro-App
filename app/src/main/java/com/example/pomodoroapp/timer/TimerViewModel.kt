package com.example.pomodoroapp.timer

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    companion object {
        private const val DONE = 0L
        private const val SHORT_BREAK = 5000L
        private const val LONG_BREAK = 15000L

        //        private const val WORK = 1500000L
        private const val WORK = 120000L

        private const val SECOND = 1000L

        private var oneMin = 60000L
    }

    private val _startTimerStatus = MutableLiveData<Boolean>()

    val startTimerStatus: LiveData<Boolean>
        get() = _startTimerStatus

    private val _timerString = MutableLiveData<String>()

    val timerString: LiveData<String>
        get() = _timerString

    private lateinit var myTimer: CountDownTimer

    init {
        myTimer = createTimerObject(WORK)
        _startTimerStatus.value = false
    }

    private fun createTimerObject(workTime: Long): CountDownTimer {

        return object : CountDownTimer(
            workTime,
            SECOND
        ) {
            override fun onFinish() {
                timerCancel()
                timerStopped()
            }

            override fun onTick(milliSecond: Long) {
                var o = countDownOneMin()
                Log.i("Timer", "second ${milliSecond / 60000} : ${o}")
                _timerString.value = "${milliSecond / 60000}:${o}"
            }
        }
    }

    fun toggleStartAndStop() {
        if (_startTimerStatus.value == false) {
            onStartTimer()
            timerStarted()
        } else {
            onStopTimer()
            timerStopped()
        }
    }

    private fun onStartTimer() {
        myTimer.start()
        Log.i("TimerViewModel", "timer started")
        timerStarted()
    }

    private fun onStopTimer() {
        myTimer.onFinish()
        Log.i("TimerViewModel", "timer stop")

    }

    private fun onPauseTimer() {
        Log.i("TimerViewModel", "timer pause")

    }

    private fun onResumeTimer() {
        Log.i("TimerViewModel", "timer resume timer")
    }

    fun onReset() {
        Log.i("TimerViewModel", "timer reset")

    }

    private fun timerStarted() {
        _startTimerStatus.value = true
    }

    private fun timerStopped() {
        _startTimerStatus.value = false
    }

    fun timerCancel() {
        myTimer.cancel()
    }

    private fun countDownOneMin(): Long {
        oneMin -= 1000

        if (oneMin == 0L) {
            oneMin = 60000L
        }

        return oneMin / 1000
    }
}