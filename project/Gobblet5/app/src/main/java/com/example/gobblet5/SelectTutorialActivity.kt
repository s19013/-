package com.example.gobblet5

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_select_tutorial.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView


class SelectTutorialActivity : BaseClass() {
    private lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_tutorial)

        iniAD()

        // 操作説明画面に遷移する処理
        howToControlButton.setOnClickListener {
            playSound(buttonSE)
            val intent = Intent(this,SelectHowToOperateActivity::class.java)
            startActivity(intent)
        }

        // ルール説明画面に遷移する処理
        howToPlayButton.setOnClickListener {
            playSound(buttonSE)
            val intent = Intent(this, HowToPlayActivity::class.java)
            startActivity(intent)
        }

        // タイトル画面に戻る処理
        backButton.setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun iniAD(){
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }
}