package com.example.gobblet5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class MainActivity : BaseClass() {
    private val activityIDPreGameWithCom = 1
    private val activityIDPreGameWithMan = 2
    private val activityIDConfig = 3
    private val activityIDSelectTutorial = 4

    private lateinit var mAdView : AdView
    private lateinit var logo:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}
        iniAD()
        iniLogo()


        goPreGameWithComBtn.setOnClickListener { changeActivity(activityIDPreGameWithCom) }
        goPreGameWithManBtn.setOnClickListener { changeActivity(activityIDPreGameWithMan) }
        goTutorialBtn.setOnClickListener { changeActivity(activityIDSelectTutorial) }
        goConfigBtn.setOnClickListener { changeActivity(activityIDConfig) }

    }

    private fun changeActivity(act:Int){
        playSound(menuSelectSE)
        var intent:Intent?=null

        when(act){
            activityIDPreGameWithCom -> intent = Intent(this,preGameWithComActivity::class.java)
            activityIDPreGameWithMan -> intent = Intent(this,preGameWithManActivity::class.java)
            activityIDSelectTutorial -> intent = Intent(this,SelectTutorialActivity::class.java)
            activityIDConfig -> intent = Intent(this,ConfigActivity::class.java)
        }
        startActivity(intent)
    }

    private fun iniLogo(){
        logo  = findViewById(R.id.logoImg)
        if (Locale.getDefault().equals(Locale.JAPAN)){
            logo.setImageResource(R.drawable.logo_jp)
            Log.d("gobblet2", "lang:jp")
        } else {
            logo.setImageResource(R.drawable.logo_en)
            Log.d("gobblet2", "lang:en")
        }
    }

    private fun iniAD(){
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

    }
}