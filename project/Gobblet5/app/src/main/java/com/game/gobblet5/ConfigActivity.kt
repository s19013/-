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

    private lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){ visibleSorryText() }

        iniTextView()
        iniSeekBar()
        iniAD()

        privacyPolicyButton.setOnClickListener {
            val uri: Uri = Uri.parse("https://docs.google.com/document/d/e/2PACX-1vSgFvW_CcThfCzhFJauIcnTIdICrCmFbNZGjIk5CivgpCbwut1fgWTv8R4tcDIu_dtKwqrd2wymmwpW/pub")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    //テキスト初期化
    private fun iniTextView(){
        seVolumeText = findViewById(R.id.seVolume)
        bgmVolumeText = findViewById(R.id.bgmVolume)

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
                    //mediaPlayer?.setVolume(bgmVolume*0.1f,bgmVolume*0.1f)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val editor= pref!!.edit()
                    editor.putInt("bgmVolume",bgmVolume).apply()
                }
            }
        )
    }

    private fun iniAD(){
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun visibleSorryText(){
        findViewById<LinearLayout>(R.id.SEConfigBox).visibility= View.INVISIBLE
        findViewById<LinearLayout>(R.id.MusicConfigBox).visibility= View.INVISIBLE
        findViewById<TextView>(R.id.sorryText).visibility= View.VISIBLE
    }



}
