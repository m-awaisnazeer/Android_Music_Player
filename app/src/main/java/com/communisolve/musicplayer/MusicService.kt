package com.communisolve.musicplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import com.communisolve.musicplayer.model.MusicFiles

class MusicService : Service() {
    var mBinder: MyBinder = MyBinder()
    lateinit var mediaPlayer: MediaPlayer
    val listOfSongs: ArrayList<MusicFiles> = ArrayList()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder?{
        Toast.makeText(this, "onBind", Toast.LENGTH_SHORT).show()
        return mBinder
    }

    inner class MyBinder : Binder() {
        fun getMusicService(): MusicService {
            return this@MusicService
        }
    }
}