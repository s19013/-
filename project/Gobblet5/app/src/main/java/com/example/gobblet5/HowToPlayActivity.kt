package com.example.gobblet5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_how_to_play.*
import kotlinx.android.synthetic.main.fragment_tutorial_text.*

class HowToPlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_play)
//        fun setText(text:String){
//            TutorialText.text = text
//        }
        val fragment = tutorialText as? TutorialTextFragment
        fragment?.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
    }




}
