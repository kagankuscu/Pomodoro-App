package com.example.pomodoroapp

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.ads.HwAds
import timber.log.Timber
import timber.log.Timber.DebugTree


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        Timber.plant(DebugTree())

        // Initialize the HUAWEI Ads SDK.
        HwAds.init(this)
    }
}