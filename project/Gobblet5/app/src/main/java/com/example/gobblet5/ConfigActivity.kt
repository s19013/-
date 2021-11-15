package com.example.gobblet5

import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_config.*

class ConfigActivity : AppCompatActivity() {
    private lateinit var sp: SoundPool
    private var cancelSE = 0
    private var radioButtonSE = 0
    private var seekSE = 0
    private var pref: SharedPreferences? =null
    private var seVolume = 0
    private var bgmVolume = 0
    private var seSeekBar:SeekBar? = null
    private var bgmSeekBar:SeekBar? = null
    private var seVolumeText:TextView? = null
    private var bgmVolumeText:TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        iniPreference()
        iniSoundPool()
        iniTextView()
        iniSeekBar()

        backButton.setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun iniTextView(){
        seVolumeText = findViewById(R.id.seVolume)
        bgmVolumeText = findViewById(R.id.bgmVolume)

        //テキスト初期化
        seVolumeText?.text = seVolume.toString() //プレファレンスの値をセット
        bgmVolumeText?.text = bgmVolume.toString() //プレファレンスの値をセット

    }

    //シークバー初期化
    private fun iniSeekBar(){
        seSeekBar = findViewById(R.id.seSeekBar)
        bgmSeekBar = findViewById(R.id.bgmSeekBar)

        seSeekBar?.progress = seVolume //プレファレンスの値を初期値にセット
        seSeekBar?.max = 10

        bgmSeekBar?.progress = bgmVolume //プレファレンスの値を初期値にセット
        bgmSeekBar?.max = 10

        seSeekBar?.setOnSeekBarChangeListener(
            object:SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    playSound(seekSE)
                    seVolumeText?.text = progress.toString()
                    seVolume=progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val editor= pref!!.edit()
                    editor.putInt("seVolume",seVolume).commit()
                }
            }
        )

        bgmSeekBar?.setOnSeekBarChangeListener(
            object:SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    playSound(seekSE)
                    bgmVolumeText?.text = progress.toString()
                    bgmVolume=progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val editor= pref!!.edit()
                    editor.putInt("bgmVolume",bgmVolume).commit()
                }
            }
        )
    }

    private fun iniPreference(){
        //共有プリファレンス
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        seVolume =pref!!.getInt("seVolume",0)
        Log.d("gobblet2", "prefSE${pref?.getInt("seVolume",0)}")
        bgmVolume =pref!!.getInt("bgmVolume",0)
    }

    private fun iniSoundPool(){
        //soundPool
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
        //使う効果音を準備
        cancelSE = sp.load(this, R.raw.cancel, 1)
        radioButtonSE = sp.load(this,R.raw.radio_button,1)
        seekSE=sp.load(this,R.raw.select_se,1)
    }

    fun playSound(status: Int){
        if (seVolume>0){
            when(status){
                cancelSE -> sp.play(cancelSE, seVolume*0.1f, seVolume*0.1f, 1, 0, 1.0f)
                radioButtonSE -> sp.play(radioButtonSE,seVolume*0.1f,seVolume*0.1f,1,0,1.0f)
                seekSE -> sp.play(seekSE,seVolume*0.1f,seVolume*0.1f,1,0,1.0f)
            }
        }
    }

    fun changeVolumeText(){

    }
}