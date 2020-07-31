package com.example.pomodoroapp.timer

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    companion object {
        private const val DONE = 0L
        private const val SHORT_BREAK = 300000L
        private const val LONG_BREAK = 15000L

        //        private const val WORK = 1500000L
        private const val WORK = 120000L

        private const val SECOND = 1000L

        private var oneMin = 60000L
    }

    private val pomodoroArr = arrayOf(WORK, SHORT_BREAK, LONG_BREAK)

    private val _startTimerStatus = MutableLiveData<Boolean>()

    val startTimerStatus: LiveData<Boolean>
        get() = _startTimerStatus

    private val _timerString = MutableLiveData<String>()

    val timerString: LiveData<String>
        get() = _timerString

    private var myTimer: CountDownTimer

    private val _resetTimer = MutableLiveData<Boolean>()

    val resetTimer: LiveData<Boolean>
        get() = _resetTimer

    private var onTickMilliSecond = 0L

    private val _pauseTimerStatus = MutableLiveData<Boolean>()

    private val pauseTimerStatus: LiveData<Boolean>
        get() = _pauseTimerStatus

    init {
        myTimer = createTimerObject(WORK)
        _startTimerStatus.value = false
        _pauseTimerStatus.value = false
    }

    private fun createTimerObject(workTime: Long): CountDownTimer {

        return object : CountDownTimer(
            workTime,
            SECOND
        ) {
            override fun onFinish() {
                onResetTimer()
            }

            override fun onTick(milliSecond: Long) {
                val o = countDownOneMin()
                Log.i("Timer", "second ${milliSecond / 60000} : $o")
                _timerString.value = "${milliSecond / 60000}:${o}"
                onTickMilliSecond = milliSecond
            }
        }
    }

    fun toggleStartAndStop() {
        if (checkStartTimerStatus()) {
            onStartTimer()
        } else {

            if (!checkPauseTimerStatus()) {
                onResumeTimer()
                pauseTimerCompleted()
            } else {
                onPauseTimer()
                pauseTimer()
            }
        }
    }

    private fun checkPauseTimerStatus(): Boolean {
        if (_pauseTimerStatus.value == false) {
            return true
        }
        return false
    }

    private fun checkStartTimerStatus(): Boolean {
        if (_startTimerStatus.value == false) {
            return true
        }
        return false
    }

    private fun onStartTimer() {
        myTimer = createTimerObject(WORK)
        myTimer.start()
        Log.i("TimerViewModel", "timer started")
        timerStarted()
    }

    private fun onPauseTimer() {
        Log.i("TimerViewModel", "timer pause")
        timerCancel()
    }

    private fun pauseTimer() {
        _pauseTimerStatus.value = true
    }

    private fun pauseTimerCompleted() {
        _pauseTimerStatus.value = false
    }

    private fun onResumeTimer() {
        Log.i("TimerViewModel", "timer resume timer")
        myTimer = createTimerObject(onTickMilliSecond)
        myTimer.start()
    }

    fun onSkipTimer() {
        Log.i("Timer", "Skip timer")
//        onResetTimer()

        timerCancel()
        oneMinReset()

        myTimer = createTimerObject(SHORT_BREAK)
        myTimer.start()
    }

    fun onResetTimer() {
        Log.i("TimerViewModel", "timer reset")

        timerCancel()
        resetTimer()
        timerCompleted()
        oneMinReset()
    }

    private fun timerStarted() {
        _startTimerStatus.value = true
    }

    private fun timerCompleted() {
        _startTimerStatus.value = false
    }

    private fun timerCancel() {
        myTimer.cancel()
    }

    private fun countDownOneMin(): Long {
        oneMin -= 1000

        if (oneMin < 0L) {
            oneMin = 60000L
        }

        return oneMin / 1000
    }

    private fun resetTimer() {
        _resetTimer.value = true
    }

    fun resetTimerCompleted() {
        _resetTimer.value = false
    }

    private fun oneMinReset() {
        oneMin = 60000L
    }
}