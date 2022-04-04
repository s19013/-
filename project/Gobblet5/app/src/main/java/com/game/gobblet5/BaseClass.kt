package com.game.gobblet5


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


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