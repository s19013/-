package com.example.gobblet5

import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.MobileAds

//主にどのクラスでも使うプレファレンスと効果音をまとめた
open class BaseClass: AppCompatActivity()  {
    //効果音
    private   var sp: SoundPool?= null
    protected var cancelSE = 0
    protected var seekSE = 0
    protected var radioButtonSE = 0
    protected var menuSelectSE = 0
    protected var gameStartSE = 0
    protected var pageSE = 0

    //プレファレンス
    protected var pref: SharedPreferences? =null
    protected var seVolume = 0
    protected var bgmVolume = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniAll()
        MobileAds.initialize(this) {}
    }

    private fun iniAll(){
        iniPreference()
        iniSoundPool()
    }

    private fun iniPreference(){
        //共有プリファレンス
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        seVolume =pref!!.getInt("seVolume",5)
        bgmVolume =pref!!.getInt("bgmVolume",5)
    }

    private fun iniSoundPool(){
        //soundPool
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()
        sp = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(1)
            .build()
        //使う効果音を準備
        cancelSE = sp!!.load(this, R.raw.cancel, 1)
        radioButtonSE = sp!!.load(this,R.raw.radio_button,1)
        seekSE=sp!!.load(this,R.raw.select_se,1)
        menuSelectSE = sp!!.load(this, R.raw.button, 1)
        gameStartSE = sp!!.load(this,R.raw.game_start_se,1)
        pageSE = sp!!.load(this,R.raw.page_sound,1)
    }

    protected fun playSound(status: Int){
        if (seVolume > 0){ sp!!.play(status,seVolume*0.1f,seVolume*0.1f,1,0,1.0f) }
    }

    override fun onDestroy() {
        super.onDestroy()
        sp!!.release()
    }
}