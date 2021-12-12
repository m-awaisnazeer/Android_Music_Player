package com.communisolve.musicplayer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class ApplicationClass: Application() {
    companion object{
        const val CHANNEL_ID_1="channel1"
        const val CHANNEL_ID_2="channel2"
        const val ACTION_PREVIOUS = "actionPrevious"
        const val ACTION_NEXT = "actionNext"
        const val ACTION_PLAY = "actionPlay"

    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var notificationChannel1:NotificationChannel = NotificationChannel(
                CHANNEL_ID_1,"Channel(1)",NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel1.description = "Channel 1 Desc.."

            var notificationChannel2:NotificationChannel = NotificationChannel(
                CHANNEL_ID_2,"Channel(2)",NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel2.description = "Channel 2 Desc.."

            val notificationManager:NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel1)
            notificationManager.createNotificationChannel(notificationChannel2)
        }
    }
}