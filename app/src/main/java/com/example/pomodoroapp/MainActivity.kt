package com.example.pomodoroapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.example.pomodoroapp.notification.TimerNotification
import com.example.pomodoroapp.notification.TimerNotification.NotificationID.TIMER_NOTIFICATION
import com.example.pomodoroapp.notification.TimerNotification.NotificationID.TIMER_ID
import com.huawei.hms.ads.HwAds
import timber.log.Timber
import timber.log.Timber.DebugTree


class MainActivity : AppCompatActivity() {
    private lateinit var notificationManager: NotificationManager
    private val myNotification = TimerNotification(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        Timber.plant(DebugTree())

        // Initialize the HUAWEI Ads SDK.
        HwAds.init(this)
        createNotificationChannel()

    }

    override fun onDestroy() {
        super.onDestroy()
        cancelNotification()
    }

    // Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        return when (item.itemId) {
            R.id.settingsFragment -> item.onNavDestinationSelected(navController)
            R.id.aboutFragment -> item.onNavDestinationSelected(navController)
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun createNotificationChannel() {
        notificationManager =
            getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
        notificationManager.createNotificationChannel(myNotification.createNotificationChannel())
    }

    fun cancelNotification() {
        notificationManager.cancel(TIMER_ID)
    }
}