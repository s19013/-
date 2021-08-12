package com.example.gobblet5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_select_tutorial.*


class SelectTutorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_tutorial)

        val howToControlButton = findViewById<Button>(R.id.howToControlButton)
        val howToPlayButton = findViewById<Button>(R.id.howToPlayButton)
        val backButton = findViewById<Button>(R.id.backButton)


        // 操作説明画面に遷移する処理
        howToControlButton.setOnClickListener {
            val intent = Intent(this, HowToControlActivity::class.java)
            startActivity(intent)
        }

        // ルール説明画面に遷移する処理
        howToPlayButton.setOnClickListener {
            val intent = Intent(this, HowToPlayActivity::class.java)
            startActivity(intent)
        }

        // タイトル画面に戻る処理
        backButton.setOnClickListener { finish() }
    }
}

/*
ボタンのid.setOnClickListener { }

ボタンのid.setOnClickListener { }

ボタンのid.setOnClickListener {
	作業したいこと､呼び出したい関数とか
}
*/