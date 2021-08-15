package com.example.gobblet5

import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_select_tutorial.*

class SelectHowToOperateActivity : AppCompatActivity() {
    //音関係
    private lateinit var sp: SoundPool
    private var cancelSE = 0
    private var menuSelectSE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_how_to_operate)

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

        cancelSE = sp.load(this, R.raw.cancel, 1)
        menuSelectSE = sp.load(this,R.raw.menu_selected,1)

        fun playSound(status: Int){
            Log.d("gobblet2", "status:${status}")
            if (SE){
                when(status){
                    cancelSE -> sp.play(cancelSE, 1.0f, 1.0f, 1, 0, 1.0f)
                    menuSelectSE -> sp.play(menuSelectSE, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }
        }


        howToControlButton.setOnClickListener {
            playSound(menuSelectSE)
            val intent = Intent(this, HowToOperateActivity1::class.java)
            startActivity(intent)
        }

        howToPlayButton.setOnClickListener {
            playSound(menuSelectSE)
            val intent = Intent(this, HowToPlayActivity::class.java)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}