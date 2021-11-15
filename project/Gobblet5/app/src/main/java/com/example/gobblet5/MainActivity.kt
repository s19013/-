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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iniall()

        val logo  = findViewById<ImageView>(R.id.logoImg)


        if (Locale.getDefault().equals(Locale.JAPAN)){
            logo.setImageResource(R.drawable.logo_jp)
            Log.d("gobblet2", "jp")
        } else {
            logo.setImageResource(R.drawable.logo_en)
            Log.d("gobblet2", "en")
        }


        goPreGameWithComBtn.setOnClickListener{
            playSound(menuSelectSE)
            val intent = Intent(this,preGameWithComActivity::class.java)
            startActivity(intent)
        }

        goPreGameWithManBtn.setOnClickListener {
            playSound(menuSelectSE)
            val intent = Intent(this,preGameWithManActivity::class.java)
            startActivity(intent)
        }

        goConfigBtn.setOnClickListener {
            playSound(menuSelectSE)
            val intent = Intent(this,ConfigActivity::class.java)
            startActivity(intent)
        }

        goTutorialBtn.setOnClickListener {
            playSound(menuSelectSE)
            val intent = Intent(this,SelectTutorialActivity::class.java)
            startActivity(intent)
        }

    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        Log.d("gobblet2", "KeyEvent:${event}")
        val code = event?.keyCode
        return super.dispatchKeyEvent(event)
    }
}