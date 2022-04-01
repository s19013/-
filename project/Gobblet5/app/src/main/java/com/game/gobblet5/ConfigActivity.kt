package com.game.gobblet5

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.activity_config.*

class ConfigActivity : BaseClass() {
    private var seSeekBar:SeekBar? = null
    private var bgmSeekBar:SeekBar? = null
    private var seVolumeText:TextView? = null
    private var bgmVolumeText:TextView? = null

    private lateinit var mAdView : AdView //広告

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        iniTextView()
        iniSeekBar()
        iniAD()

        privacyPolicyButton.setOnClickListener {
            val uri: Uri = Uri.parse("https://docs.google.com/document/d/e/2PACX-1vSgFvW_CcThfCzhFJauIcnTIdICrCmFbNZGjIk5CivgpCbwut1fgWTv8R4tcDIu_dtKwqrd2wymmwpW/pub")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            sound.playSound(sound.cancelSE,save.seVolume)
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    //テキスト初期化
    private fun iniTextView(){
        seVolumeText = findViewById(R.id.seVolume)
        bgmVolumeText = findViewById(R.id.bgmVolume)



        seVolumeText?.text = save.seVolume.toString() //プレファレンスの値をセット
        bgmVolumeText?.text = save.bgmVolume.toString()

    }

    //シークバー初期化
    private fun iniSeekBar(){
        seSeekBar = findViewById(R.id.seSeekBar)
        bgmSeekBar = findViewById(R.id.bgmSeekBar)

        seSeekBar?.progress = save.seVolume //プレファレンスの値を初期値にセット
        seSeekBar?.max = 10

        bgmSeekBar?.progress = save.bgmVolume //プレファレンスの値を初期値にセット
        bgmSeekBar?.max = 10

        seSeekBar?.setOnSeekBarChangeListener(
            object:SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    seVolumeText?.text = progress.toString()
                    save.seVolume=progress
                    sound.playSound(sound.seekSE,save.seVolume)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val editor= save.pref!!.edit()
                    editor.putInt("seVolume",save.seVolume)
                    editor.apply()
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
                    sound.playSound(sound.seekSE,save.seVolume)
                    bgmVolumeText?.text = progress.toString()
                    save.bgmVolume=progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val editor= save.pref!!.edit()
                    editor.putInt("bgmVolume",save.bgmVolume)
                    editor.apply()
                }
            }
        )
    }

    private fun iniAD(){
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

}
