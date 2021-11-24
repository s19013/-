package com.example.gobblet5

import android.content.Intent
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.activity_select_how_to_operate.*


class SelectHowToOperateActivity : BaseClass() {
    private lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_how_to_operate)

        iniAD()

        MovePieceFromHandToSquareButton.setOnClickListener {
            playSound(menuSelectSE)
            val intent = Intent(this, HowToOperateActivity1::class.java)
            startActivity(intent)
        }

        MovePieceSquareToSquareButton.setOnClickListener {
            playSound(menuSelectSE)
            val intent = Intent(this, HowToOperateActivity2::class.java)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this, SelectTutorialActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniAD(){
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }
}