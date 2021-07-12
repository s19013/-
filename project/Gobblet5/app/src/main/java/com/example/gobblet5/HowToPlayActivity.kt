package com.example.gobblet5

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_how_to_play.*


class HowToPlayActivity : AppCompatActivity() {
    private var page =1
    private var tutorialText="sample text"
    //bundle宣言
    val bundle=Bundle()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_play)

        fun changeText(){//テキストをかえる
            when(page){
                1->tutorialText="page1"
                2->tutorialText="page2"
                3->tutorialText="page3"
                4->tutorialText="page4"
                5->tutorialText="page5"
            }
        }

        fun changeImage(){//画像を変える
            //comming soon
        }

        fun changeFragment(){//フラグメントを変える
            bundle.putString("Text",tutorialText)//bundle保存
            val textFragment = TutorialTextFragment()
            textFragment.arguments=bundle//フラグメントにargumentsをわたす
            val fragmentManager = this.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.TextContainer,textFragment)
                .addToBackStack(null)
                .commit()
        }

        //アクティビティが呼び出されたときに1回呼ぶ
        changeText()
        changeImage()
        changeFragment()

        preButton.setOnClickListener {
            if (page>1){
                page-=1
                changeText()
                changeImage()
                changeFragment()
                Log.d("gobblet2","page:${page}")
            }
        }

        nextButton.setOnClickListener {
            if (page<5){
                page+=1
                changeText()
                changeImage()
                changeFragment()
                Log.d("gobblet2","page:${page}")
            }
        }


    }

}
