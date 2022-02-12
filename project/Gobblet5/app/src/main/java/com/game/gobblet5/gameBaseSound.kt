package com.game.gobblet5

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build

class gameBaseSound {
    var sp: SoundPool? = null
    var putSE=0
    var selectSE = 0
    var cancelSE = 0
    var menuSelectSE = 0
    var cannotDoItSE = 0
    var gameStartSE = 0
    var openSE = 0
    var closeSE = 0
    var winSE = 0
    var loosSE = 0
    var seekSE = 0
    var seVolume = 0

    fun setSeVoluem(arg:Int){seVolume = arg}

    fun iniSoundPool(context: Context){
        //soundPool
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            val audioAttributes =
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            sp = SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(1)
                .build()
        }
        //使う効果音を準備
        cannotDoItSE= sp!!.load(context, R.raw.cannotdoit, 1)
        putSE= sp!!.load(context, R.raw.select_se, 1)
        selectSE = sp!!.load(context, R.raw.put, 1)
        cancelSE = sp!!.load(context, R.raw.cancel, 1)
        menuSelectSE = sp!!.load(context, R.raw.menu_selected, 1)
        gameStartSE = sp!!.load(context,R.raw.game_start_se,1)
        openSE = sp!!.load(context,R.raw.open,1)
        closeSE = sp!!.load(context,R.raw.close,1)
        winSE = sp!!.load(context,R.raw.win_single,1)
        loosSE = sp!!.load(context,R.raw.loose_single,1)
        seekSE=sp!!.load(context,R.raw.seekbar,1)
    }

    fun playSound(status: Int){
        if (seVolume > 0){ sp!!.play(status,seVolume*0.1f,seVolume*0.1f,1,0,1.0f) }
    }
}