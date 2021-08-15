package com.example.gobblet5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_how_to_control.*

class HowToControlActivity : AppCompatActivity() {
    val maxPage = 11
    var Page:Int = 1
    //    val img =

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_control)

        nextButton.setOnClickListener {
            countUpPage()
        }

        preButton.setOnClickListener {
            countDownPage()
        }

        backButton.setOnClickListener {
            val intent = Intent(this,SelectTutorialActivity::class.java)
            startActivity(intent)
        }

        maxPageText.text = maxPage.toString()
    }

    fun changeText() {
        when {
            Page == 1 -> tutorialText.text = getString(R.string.HowToPlayTutorialText1)
            Page == 2 -> tutorialText.text = getString(R.string.HowToPlayTutorialText2)
            Page == 3 -> tutorialText.text = getString(R.string.HowToPlayTutorialText3)
            Page == 4 -> tutorialText.text = getString(R.string.HowToPlayTutorialText4)
            Page == 5 -> tutorialText.text = getString(R.string.HowToPlayTutorialText5)
            Page == 6 -> tutorialText.text = getString(R.string.HowToPlayTutorialText6)
            Page == 7 -> tutorialText.text = getString(R.string.HowToPlayTutorialText7)
            Page == 8 -> tutorialText.text = getString(R.string.HowToPlayTutorialText8)
            Page == 9 -> tutorialText.text = getString(R.string.HowToPlayTutorialText9)
            Page == 10 -> tutorialText.text = getString(R.string.HowToPlayTutorialText10)
        }
    }

    fun countUpPage() {
        Page += 1
        if (Page > maxPage) {
            Page = maxPage
        }
        changeText()
        changeImg()
        changeCurrentPage()
    }

    fun countDownPage() {
        Page -= 1
        if (Page < 1) {
            Page = 1
        }
        changeText()
        changeImg()
        changeCurrentPage()
    }

    fun changeCurrentPage() {
        currentPageText.text = Page.toString()
    }

    fun changeImg() {

    }

}