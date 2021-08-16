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
import kotlinx.android.synthetic.main.activity_config.*

class ConfigActivity : AppCompatActivity() {
    private lateinit var sp: SoundPool
    private var cancelSE = 0
    private var radioButtonSE = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

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
        radioButtonSE = sp.load(this,R.raw.radio_button,1)

        fun playSound(status: Int){
            if (SE){
                when(status){
                    cancelSE -> sp.play(cancelSE, 1.0f, 1.0f, 1, 0, 1.0f)
                    radioButtonSE -> sp.play(radioButtonSE,1.0f,1.0f,1,0,1.0f)
                }
            }
        }

        backButton.setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this,MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }



        val RadioSE = findViewById<RadioGroup>(R.id.SEOnOff)
        when(SE){
            true -> {findViewById<RadioButton>(R.id.SEOn).isChecked = true}
            false -> {findViewById<RadioButton>(R.id.SEOff).isChecked = true}
        }

        val RadioBGM = findViewById<RadioGroup>(R.id.BGMOnOff)
        when(BGM){
            true -> {findViewById<RadioButton>(R.id.BGMOn).isChecked = true}
            false -> {findViewById<RadioButton>(R.id.BGMOff).isChecked = true}
        }


        // 関数設定
        RadioSE.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.SEOn->{SE=true}
                R.id.SEOff->{SE=false}
            }
            playSound(radioButtonSE)
            val editor=pref.edit()
            editor.putBoolean("SEOnOff",SE).apply()
            Log.d("gobblet2", "SE=${SE}")
        }

        RadioBGM.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.BGMOn->{BGM=true}
                R.id.BGMOff->{BGM=false}
            }
            playSound(radioButtonSE)
            val editor=pref.edit()
            editor.putBoolean("BGMOnOff",BGM).apply()
            Log.d("gobblet2", "BGM=${BGM}")
        }

    }
}