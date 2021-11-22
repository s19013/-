package com.example.gobblet5

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.gobblet5.HowToOperateFragment.HowToOperateFragment1_1
import com.example.gobblet5.HowToOperateFragment.HowToOperateFragment1_2
import com.example.gobblet5.HowToOperateFragment.HowToOperateFragment1_3
import kotlinx.android.synthetic.main.activity_how_to_operate1.*

class HowToOperateActivity1 : BaseClass() {
    private val maxPage = 3
    private var Page:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_operate1)

        nextButton.setOnClickListener { countUpPage() }

        preButton.setOnClickListener { countDownPage() }

        backButton.setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this,SelectHowToOperateActivity::class.java)
            startActivity(intent)
        }

        maxPageText.text = maxPage.toString()

    }

    private fun changeElements(){
        playSound(pageSE)
        changeText()
        changeImg()
        changeCurrentPage()
    }

    private fun countUpPage() {
        if (Page < maxPage) {
            Page += 1
            changeElements()
        }
        if (Page > maxPage) { Page = maxPage }
    }

    private fun countDownPage() {
        if (Page > 1) {
            Page -= 1
            changeElements()
        }

        if (Page < 1) { Page = 1 }
    }


    private fun changeText() {
        when (Page) {
            1 -> tutorialText.text = getString(R.string.HowToOperateTutorialText1_1)
            2 -> tutorialText.text = getString(R.string.HowToOperateTutorialText1_2)
            3 -> tutorialText.text = getString(R.string.HowToOperateTutorialText1_3)
        }
    }

    private fun changeCurrentPage() { currentPageText.text = Page.toString() }

    private fun changeImg() {
        when(Page) {
            1 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg, HowToOperateFragment1_1())
                    .addToBackStack(null)
                    .commit()
            }
            2 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg, HowToOperateFragment1_2())
                    .addToBackStack(null)
                    .commit()
            }
            3 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg, HowToOperateFragment1_3())
                    .addToBackStack(null)
                    .commit()
            }

        }

    }

    //    全画面表示に関すること
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }

}