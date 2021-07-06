package com.example.gobblet5

import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_pre_game_with_man.*
import kotlinx.android.synthetic.main.activity_select_tutorial.*
import kotlinx.android.synthetic.main.activity_select_tutorial.backButton

class SelectTutorialActivity : AppCompatActivity() {
    private lateinit var sp: SoundPool
    private var cancelSE = 0
    private var menuSelectSE = 0
    private var gameStartSE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_tutorial)

        //共有プリファレンス
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        var SE =pref.getBoolean("SEOnOff", true)
        var playFirst=pref.getInt("playFirst", 1)

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
        menuSelectSE = sp.load(this, R.raw.menu_selected, 1)

        fun playSound(status: String){
            if (SE){
                when(status){
                    "cancel" -> sp.play(cancelSE, 1.0f, 1.0f, 1, 0, 1.0f)
                    "menuSelect" -> sp.play(menuSelectSE, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }
        }

        howToControlBtn.setOnClickListener {
            playSound("menuSelect")
            val intent = Intent(this,HowToControlActivity::class.java)
            startActivity(intent)
        }

        howToPlayBtn.setOnClickListener {
            playSound("menuSelect")
            val intent = Intent(this,HowToPlayActivity::class.java)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            playSound("cancel")
            val intent = Intent(this,MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}