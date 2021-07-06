package com.example.gobblet5

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.view.KeyEvent

class BgmService : Service() {
    var player:MediaPlayer?=null

    override fun onCreate() {
        super.onCreate()
        player=MediaPlayer.create(applicationContext,R.raw.okashi_time)
        player?.isLooping=true
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player?.start()
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player=null
    }

    

}