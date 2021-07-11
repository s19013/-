package com.example.gobblet5

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_how_to_play.*


class HowToPlayActivity : AppCompatActivity() {
    private var page =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_play)
        //bundle宣言
        val tutorialText="sample text"
        val bundle=Bundle()
        bundle.putString("SampleText",tutorialText)
        //フラグメント生成
        val textFragment = TutorialTextFragment()
        textFragment.arguments=bundle//フラグメントにargumentsをわたす
        val fragmentManager = this.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.containerOfTextFragment,textFragment)
            .addToBackStack(null)
            .commit()



    }


}
