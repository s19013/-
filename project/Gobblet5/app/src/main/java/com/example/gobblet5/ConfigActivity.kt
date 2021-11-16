package com.example.gobblet5

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_config.*

class ConfigActivity : BaseClass() {
    private var seSeekBar:SeekBar? = null
    private var bgmSeekBar:SeekBar? = null
    private var seVolumeText:TextView? = null
    private var bgmVolumeText:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

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
                    editor.putInt("seVolume",seVolume).apply()
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
                    editor.putInt("bgmVolume",bgmVolume).apply()
                }
            }
        )
    }

}
