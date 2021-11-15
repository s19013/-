package com.example.gobblet5

import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

open class BaseClass: AppCompatActivity()  {
    
    //効果音
    private lateinit var sp: SoundPool
    private var cancelSE = 0
    private var seekSE = 0
    private var radioButtonSE = 0
    private var menuSelectSE = 0
    private var gameStartSE = 0


    //プレファレンス
    protected var pref: SharedPreferences? =null
    protected var seVolume = 0
    protected var bgmVolume = 0

    protected fun iniall(){
        iniPreference()
        iniSoundPool()
    }

    protected fun iniPreference(){
        //共有プリファレンス
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        seVolume =pref!!.getInt("seVolume",0)
        bgmVolume =pref!!.getInt("bgmVolume",0)
    }

    protected fun iniSoundPool(){
        //soundPool
        val audioAttributes = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
        } else { TODO("VERSION.SDK_INT < LOLLIPOP") }
        sp = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(1)
            .build()
        //使う効果音を準備
        cancelSE = sp.load(this, R.raw.cancel, 1)
        radioButtonSE = sp.load(this,R.raw.radio_button,1)
        seekSE=sp.load(this,R.raw.select_se,1)
        menuSelectSE = sp.load(this, R.raw.button, 1)
        gameStartSE = sp.load(this,R.raw.game_start_se,1)

    }

    protected fun playSound(status: Int){
        if (seVolume>0){ sp.play(status,seVolume*0.1f,seVolume*0.1f,1,0,1.0f) }
    }

    override fun onDestroy() {
        super.onDestroy()
        sp!!.release()
    }
}