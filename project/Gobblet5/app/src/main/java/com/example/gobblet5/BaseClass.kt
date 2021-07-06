package com.example.gobblet5

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

open class BaseClass: AppCompatActivity()  {
    private lateinit var sp: SoundPool
    private var putSE=0
    private var selectSE = 0
    private var cancelSE = 0
    private var menuSelectSE = 0
    private var cannotDoitSE = 0
    private var gameStartSE = 0

    open override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //共有プリファレンス
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        var SE =pref.getBoolean("SEOnOff", true)
        var BGM =pref.getBoolean("BGMOnOff", true)

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

        cannotDoitSE=sp.load(this, R.raw.cannotdoit, 1)
        putSE=sp.load(this, R.raw.select_se, 1)
        selectSE = sp.load(this, R.raw.put, 1)
        cancelSE = sp.load(this, R.raw.cancel, 1)
        menuSelectSE = sp.load(this, R.raw.menu_selected, 1)
        gameStartSE = sp.load(this,R.raw.game_start_se,1)

        fun playSound(status: String){
            if (SE){
                when(status){
                    "cannotDoit" -> sp.play(cannotDoitSE, 1.0f, 1.0f, 1, 0, 1.5f)
                    "put" -> sp.play(putSE, 1.0f, 1.0f, 1, 0, 1.0f)
                    "select" -> sp.play(selectSE, 1.0f, 1.0f, 1, 0, 1.0f)
                    "cancel" -> sp.play(cancelSE, 1.0f, 1.0f, 1, 0, 1.0f)
                    "menuSelect" -> sp.play(menuSelectSE, 1.0f, 1.0f, 1, 0, 1.0f)
                    "gameStart" -> sp.play(gameStartSE, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sp.release()
    }
}