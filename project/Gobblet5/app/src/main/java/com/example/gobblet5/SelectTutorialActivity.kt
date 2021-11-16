package com.example.gobblet5

import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_select_tutorial.*


class SelectTutorialActivity : BaseClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_tutorial)

        // 操作説明画面に遷移する処理
        howToControlButton.setOnClickListener {
            playSound(menuSelectSE)
            val intent = Intent(this,SelectHowToOperateActivity::class.java)
            startActivity(intent)
        }

        // ルール説明画面に遷移する処理
        howToPlayButton.setOnClickListener {
            playSound(menuSelectSE)
            val intent = Intent(this, HowToPlayActivity::class.java)
            startActivity(intent)
        }

        // タイトル画面に戻る処理
        backButton.setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}