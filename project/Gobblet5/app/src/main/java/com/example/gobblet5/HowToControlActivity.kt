package com.example.gobblet5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_how_to_control.*

class HowToControlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_control)

        val preButton = findViewById<Button>(R.id.preButton)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val backButton = findViewById<Button>(R.id.backButton)


        preButton.setOnClickListener { countDownPage() }

        nextButton.setOnClickListener { countUpPage() }

        backButton.setOnClickListener { finish() }
    }

    val maxPage = 10
    var page: Int = 1


    private fun countUpPage() {
        page += 1
        if (page > maxPage) {
            changeText()
            changeImg()
            changeCurrentPage()
        }
        else {
            page = maxPage
            changeText()
            changeImg()
            changeCurrentPage()
        }
    }

    private fun countDownPage() {
        page -= 1
        if (page < 1) {
            changeText()
            changeImg()
            changeCurrentPage()
        }
        else {
            page = 1
            changeText()
            changeImg()
            changeCurrentPage()
        }
    }

    private fun changeText() {
        when {
            page == 1 -> tutorialText.text = getString(R.string.HowToOperateText)  // HowToOperateText1
            page == 2 -> tutorialText.text = getString(R.string.HowToOperateText)  // HowToOperateText2
            page == 3 -> tutorialText.text = getString(R.string.HowToOperateText)  // HowToOperateText3
        }
    }


    private fun changeImg() { }


    private fun changeCurrentPage() {
        currentPageText.text = page.toString()
    }

}