package de.hhn.gnsstrackingapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import de.hhn.gnsstrackingapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

const val CHANNEL_ID = "GNSSTrackingApp"
const val CHANNEL_NAME = "GNSSTrackingApp"

val webSocketIp = mutableStateOf("192.168.221.60")

class BaseApplication : Application() {
    override fun onCreate() {
        val modules = listOf(viewModelModule)

        super.onCreate()

        startKoin {
            androidContext(this@BaseApplication)
            modules(modules)
        }

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel for location tracking notifications"
            setSound(null, null)
            enableVibration(false)
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}