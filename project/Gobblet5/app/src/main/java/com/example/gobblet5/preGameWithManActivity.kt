package com.example.gobblet5

import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_pre_game_with_man.*

class preGameWithManActivity : AppCompatActivity() {
    private lateinit var sp: SoundPool
    private var cancelSE = 0
    private var menuSelectSE = 0
    private var gameStartSE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game_with_man)

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
        gameStartSE = sp.load(this,R.raw.game_start_se,1)

        fun playSound(status: String){
            if (SE){
                when(status){
                    "cancel" -> sp.play(cancelSE, 1.0f, 1.0f, 1, 0, 1.0f)
                    "menuSelect" -> sp.play(menuSelectSE, 1.0f, 1.0f, 1, 0, 1.0f)
                    "gameStart" -> sp.play(gameStartSE, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }
        }

        val Radio = findViewById<RadioGroup>(R.id.RadioGroup)
        when(playFirst){
            1 -> {findViewById<RadioButton>(R.id.Button1p).isChecked = true}
            -1 -> {findViewById<RadioButton>(R.id.Button2p).isChecked = true}
            0 -> {findViewById<RadioButton>(R.id.ButtonRandom).isChecked = true}
        }

        Radio.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.Button1p->{playFirst=1}
                R.id.Button2p->{playFirst= -1}
                R.id.ButtonRandom->{playFirst=0}
            }
            playSound("menuSelect")
            val editor=pref.edit()
            editor.putInt("playFirst",playFirst).apply()
            Log.d("gobblet2", "${playFirst}")
        }

        backButton.setOnClickListener {
            playSound("cancel")
            val intent = Intent(this,MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        gameStartBtn.setOnClickListener {
            playSound("gameStart")
            val intent = Intent(this, GameWithManActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sp.release()
    }
}