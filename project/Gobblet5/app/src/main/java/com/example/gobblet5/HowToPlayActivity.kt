package com.example.gobblet5

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_how_to_play.*


class HowToPlayActivity : AppCompatActivity() {
    private var page =1
    var tutorialText="sample text"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_play)
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

        fun changeFragment(){
            when(page){
                1->bundle.putString("SampleText","page1")
                2->bundle.putString("SampleText","page2")
                3->bundle.putString("SampleText","page3")
                4->bundle.putString("SampleText","page4")
                5->bundle.putString("SampleText","page5")
            }
            val textFragment = TutorialTextFragment()
            textFragment.arguments=bundle//フラグメントにargumentsをわたす
            val fragmentManager = this.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.TextContainer,textFragment)
                .addToBackStack(null)
                .commit()
        }

        preButton.setOnClickListener {
            if (page>0){
                page-=1
                Log.d("gobblet2","page:${page}")
                changeFragment()
            }
        }

        nextButton.setOnClickListener {
            if (page<5){
                page+=1
                Log.d("gobblet2","page:${page}")
                changeFragment()
            }
        }
    }

    override fun onStart() {
        super.onStart()

    }


}
