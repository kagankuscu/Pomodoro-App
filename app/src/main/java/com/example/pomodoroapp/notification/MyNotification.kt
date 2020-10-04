package com.example.pomodoroapp.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pomodoroapp.R

class MyNotification(private val context: Context) {

    val CHANNEL_ID = "timerNotification"

    private fun createNotification(): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer_minute)
            .setContentTitle(context.resources.getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)

        return builder
    }

    fun show(text: String) {
        val builder = this.createNotification()
            .setContentText(text)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder)
        }
    }
}