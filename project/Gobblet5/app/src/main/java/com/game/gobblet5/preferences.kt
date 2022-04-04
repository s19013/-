package com.game.gobblet5

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class preferences {
    var pref: SharedPreferences? =null
    var seVolume  = 0
    var bgmVolume = 0
    var playFirst = 1

    fun iniPreference(context:Context){
        //共有プリファレンス
        pref = PreferenceManager.getDefaultSharedPreferences(context)
        seVolume  = pref!!.getInt("seVolume",5)
        bgmVolume = pref!!.getInt("bgmVolume",5)
        playFirst = pref!!.getInt("playFirst", 1)
    }
}