package com.example.pomodoroapp.timer

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class TimerViewModel : ViewModel() {
    companion object {
        private const val DONE = 0L
        private const val SHORT_BREAK = 300000L
        private const val LONG_BREAK = 15000L

        //        private const val WORK = 1500000L
        private const val WORK = 60000L

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

    private val _resetTimerStatus = MutableLiveData<Boolean>()

    val resetTimerStatus: LiveData<Boolean>
        get() = _resetTimerStatus

    private var onTickMilliSecond = 0L

    private val _pauseTimerStatus = MutableLiveData<Boolean>()

    val pauseTimerStatus: LiveData<Boolean>
        get() = _pauseTimerStatus

    init {
        myTimer = createTimerObject(WORK)
        _startTimerStatus.value = false
        _resetTimerStatus.value = false
    }

    fun toggleStartAndStop() {
        if (checkStartTimerStatus()) {
            onStartTimer()
        } else {

            if (checkPauseTimerStatus()) {
                onPauseTimer()
                pauseTimer()
            } else {
                onResumeTimer()
                pauseTimerCompleted()
            }
        }
    }

    fun onSkipTimer() {
        Timber.i("Skip timer")

        timerCancel()
        oneMinReset()
        pauseTimerCompleted()

        myTimer = createTimerObject(WORK)
        // TODO() myTimer = createTimerObject(SHORT_BREAK) should change because
        // TODO() every click must change the time depends on the next time.

        myTimer.start()
    }

    fun onResetTimer() {
        Timber.i("timer reset")

        timerCancel()
        resetTimer()
        timerCompleted()
        oneMinReset()
        pauseTimerNull()
    }

    fun resetTimerCompleted() {
        _resetTimerStatus.value = false
    }

    var pomodoro = 0

    private fun createTimerObject(workTime: Long): CountDownTimer {

        return object : CountDownTimer(
            workTime,
            SECOND
        ) {
            override fun onFinish() {
                Timber.i("Timer Finished. ${pomodoro++}")
                when (workTime) {
                    WORK -> {
                        Timber.i("NEXT WORK")
                        onSkipTimer()
                    }
                    SHORT_BREAK -> Timber.i("NEXT SHORT BREAK")
                }
            }

            override fun onTick(milliSecond: Long) {
                val o = countDownOneMin()
                Timber.i("second ${milliSecond / 60000} : $o")
                _timerString.value = "${milliSecond / 60000}:${o}"
                onTickMilliSecond = milliSecond
            }
        }
    }

    private fun checkPauseTimerStatus(): Boolean {
        if (_pauseTimerStatus.value == false || _pauseTimerStatus.value == null) {
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
        myTimer = createTimerObject(pomodoroArr[0])
        myTimer.start()
        Timber.i("timer started")
        timerStarted()
    }

    private fun onPauseTimer() {
        timerCancel()
    }

    private fun pauseTimer() {
        _pauseTimerStatus.value = true
    }

    private fun pauseTimerCompleted() {
        _pauseTimerStatus.value = false
    }

    private fun pauseTimerNull() {
        _pauseTimerStatus.value = null
    }

    private fun onResumeTimer() {
        myTimer = createTimerObject(onTickMilliSecond)
        myTimer.start()
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
        _resetTimerStatus.value = true
    }

    private fun oneMinReset() {
        oneMin = 60000L
    }
}