package com.example.gobblet5

import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : BaseClass() {
    private val activityIDpreGameWithCom = 1
    private val activityIDpreGameWithMan = 2
    private val activityIDConfig = 3
    private val activityIDSelectTutorial = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logo  = findViewById<ImageView>(R.id.logoImg)


        if (Locale.getDefault().equals(Locale.JAPAN)){
            logo.setImageResource(R.drawable.logo_jp)
            Log.d("gobblet2", "lang:jp")
        } else {
            logo.setImageResource(R.drawable.logo_en)
            Log.d("gobblet2", "lang:en")
        }


        goPreGameWithComBtn.setOnClickListener { changeActivity(activityIDpreGameWithCom) }
        goPreGameWithManBtn.setOnClickListener { changeActivity(activityIDpreGameWithMan) }
        goTutorialBtn.setOnClickListener { changeActivity(activityIDSelectTutorial) }
        goConfigBtn.setOnClickListener { changeActivity(activityIDConfig) }

    }

    private fun changeActivity(act:Int){
        playSound(menuSelectSE)
        var intent:Intent?=null

        when(act){
            activityIDpreGameWithCom -> intent = Intent(this,preGameWithComActivity::class.java)
            activityIDpreGameWithMan -> intent = Intent(this,preGameWithManActivity::class.java)
            activityIDSelectTutorial -> intent = Intent(this,SelectTutorialActivity::class.java)
            activityIDConfig -> intent = Intent(this,ConfigActivity::class.java)
        }
        startActivity(intent)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        Log.d("gobblet2", "KeyEvent:${event}")
        val code = event?.keyCode
        return super.dispatchKeyEvent(event)
    }
}