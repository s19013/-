package com.example.gobblet5

import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_select_how_to_operate.*


class SelectHowToOperateActivity : BaseClass() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_how_to_operate)

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
}