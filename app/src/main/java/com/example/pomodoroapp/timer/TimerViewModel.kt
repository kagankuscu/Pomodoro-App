package com.example.pomodoroapp.timer

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class TimerViewModel : ViewModel() {
    companion object {
        private const val DONE = 0L

        private const val SHORT_BREAK = 300_000L
        private const val LONG_BREAK = 900_000L

        private const val WORK = 1_500_000L

        private const val SECOND = 1_000L
        private const val MINUTE = 60_000L

    }

    private var pomodoro = 0

    private val pomodoroArr = arrayOf(
        WORK, SHORT_BREAK,
        WORK, SHORT_BREAK,
        WORK, SHORT_BREAK,
        WORK, LONG_BREAK
    )

    private val _startTimerStatus = MutableLiveData<Boolean>()

    val startTimerStatus: LiveData<Boolean>
        get() = _startTimerStatus

    private val _timerString = MutableLiveData<String>()

    val timerString: LiveData<String>
        get() = _timerString

    private var countDownTimer: CountDownTimer

    private val _resetTimerStatus = MutableLiveData<Boolean>()

    val resetTimerStatus: LiveData<Boolean>
        get() = _resetTimerStatus

    private var onTickMilliSecond = 0L

    private val _pauseTimerStatus = MutableLiveData<Boolean>()

    val pauseTimerStatus: LiveData<Boolean>
        get() = _pauseTimerStatus

    private val _infoText = MutableLiveData<String>()

    val infoText: LiveData<String>
        get() = _infoText

    private val timerZero = "00:00"

    init {
        countDownTimer = createTimerObject(WORK)
        _startTimerStatus.value = false
        _resetTimerStatus.value = false

        _timerString.value = timerZero
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
        pauseTimerCompleted()

        nextTimer()
    }

    fun onResetTimer() {
        Timber.i("timer reset")

        timerCancel()
        resetTimer()
        timerCompleted()
        pauseTimerNull()
        resetTextString()
        pomodoro = 0
    }

    private fun createTimerObject(workTime: Long): CountDownTimer {

        return object : CountDownTimer(
            workTime,
            SECOND
        ) {
            override fun onFinish() {
                Timber.i("Timer Finished. $pomodoro")
                nextTimer()
            }

            override fun onTick(milliSecond: Long) {
                updateText(milliSecond)
            }
        }
    }

    private fun onStartTimer() {
        nextTimer()
        setTimer()
        Timber.i("timer started")
        timerStarted()
    }


    private fun updateText(milliSecond: Long) {
        val minutes = milliSecond / MINUTE
        val seconds = milliSecond % MINUTE / SECOND

        _timerString.value = "$minutes:"
        if (seconds < 10) _timerString.value += "0"
        _timerString.value += "$seconds"

        onTickMilliSecond = milliSecond
    }

    private fun nextTimer() {
        timerCancel()

        if (pomodoro > pomodoroArr.size - 1) pomodoro = 0

        countDownTimer = createTimerObject(pomodoroArr[pomodoro])

        setTimer()

        when (pomodoroArr[pomodoro]) {
            WORK -> _infoText.value = "Work"
            SHORT_BREAK -> _infoText.value = "Short Break"
            LONG_BREAK -> _infoText.value = "Long Break"
        }

        pomodoro++
    }

    private fun onResumeTimer() {
        countDownTimer = createTimerObject(onTickMilliSecond)
        countDownTimer.start()
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

    private fun timerStarted() {
        _startTimerStatus.value = true
    }

    private fun timerCompleted() {
        _startTimerStatus.value = false
    }

    private fun timerCancel() {
        countDownTimer.cancel()
    }

    private fun resetTimer() {
        _resetTimerStatus.value = true
    }

    fun resetTimerCompleted() {
        _resetTimerStatus.value = false
    }

    private fun setTimer() {
        countDownTimer.start()
    }

    private fun resetTextString() {
        _timerString.value = timerZero
    }
}