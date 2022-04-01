package com.game.gobblet5

import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

//主にどのクラスでも使うプレファレンスと効果音をまとめた
open class BaseClass: AppCompatActivity()  {
    //効果音
    val sound = Sound()

    //共有プリファレンス
    val save = preferences()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        save.iniPreference(applicationContext)
        sound.iniSoundPool(applicationContext)

    }


}