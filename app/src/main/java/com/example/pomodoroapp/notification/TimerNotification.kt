package com.example.pomodoroapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pomodoroapp.MainActivity
import com.example.pomodoroapp.R
import com.example.pomodoroapp.notification.TimerNotification.NotificationID.TIMER_ID
import com.example.pomodoroapp.notification.TimerNotification.NotificationID.TIMER_NOTIFICATION

class TimerNotification(private val context: Context) : BaseNotification {

    object NotificationID {
        const val TIMER_NOTIFICATION = "timerNotification"
        const val TIMER_ID = 1
    }

    private fun createTapFunction(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            Log.d("Timer" +
                    "notification", "${flags}")
        }

        return PendingIntent.getActivity(
            context, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createNotification(): NotificationCompat.Builder {
        //        TODO: add here for addAction

        return NotificationCompat.Builder(context, TIMER_NOTIFICATION)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.resources.getString(R.string.app_name))
            .setOngoing(true)
            .setContentIntent(createTapFunction())
            .setOnlyAlertOnce(true)
    }

    fun createNotificationChannel(): NotificationChannel {
        val name = context.resources.getString(R.string.timer_notification)
        val descriptionText = "My first notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(TIMER_NOTIFICATION, name, importance).apply {
            description = descriptionText
        }

        return channel
    }


    override fun show(text: String) {
        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(
            TIMER_ID, this.createNotification()
                .setContentText(text)
                .build()
        )
    }
}