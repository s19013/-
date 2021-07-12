package com.example.gobblet5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_how_to_play.*


class HowToPlayActivity : AppCompatActivity() {
    private var page =1
    var tutorialText="sample text"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_play)

    }

    override fun onStart() {
        super.onStart()
        //bundle宣言,保存
        val bundle=Bundle()
        bundle.putString("SampleText",tutorialText)
        //フラグメント生成
        val textFragment = TutorialTextFragment()
        textFragment.arguments=bundle//フラグメントにargumentsをわたす
        val fragmentManager = this.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.TextContainer,textFragment)
            .addToBackStack(null)
            .commit()
    }


}
