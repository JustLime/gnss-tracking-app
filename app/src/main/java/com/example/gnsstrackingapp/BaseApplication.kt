package com.example.gnsstrackingapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

const val CHANNEL_ID = "GNSSTrackingApp"
const val CHANNEL_NAME = "GNSSTrackingApp"

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}