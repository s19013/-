package com.example.gobblet5

import android.content.Intent
import android.os.Bundle
import com.example.gobblet5.HowToOperateFragment.HowToOperateFragment2_1
import com.example.gobblet5.HowToOperateFragment.HowToOperateFragment2_2
import com.example.gobblet5.HowToOperateFragment.HowToOperateFragment2_3
import kotlinx.android.synthetic.main.activity_how_to_operate2.backButton
import kotlinx.android.synthetic.main.activity_how_to_operate2.currentPageText
import kotlinx.android.synthetic.main.activity_how_to_operate2.maxPageText
import kotlinx.android.synthetic.main.activity_how_to_operate2.nextButton
import kotlinx.android.synthetic.main.activity_how_to_operate2.preButton
import kotlinx.android.synthetic.main.activity_how_to_operate2.tutorialText

class HowToOperateActivity2 : BaseClass() {
    private val maxPage = 3
    private var Page:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_operate2)

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
            1 -> tutorialText.text = getString(R.string.HowToOperateTutorialText2_1)
            2 -> tutorialText.text = getString(R.string.HowToOperateTutorialText2_2)
            3 -> tutorialText.text = getString(R.string.HowToOperateTutorialText2_3)
        }
    }

    private fun changeCurrentPage() { currentPageText.text = Page.toString() }

    private fun changeImg() {
        when(Page) {
            1 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg, HowToOperateFragment2_1())
                    .addToBackStack(null)
                    .commit()
            }
            2 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg, HowToOperateFragment2_2())
                    .addToBackStack(null)
                    .commit()
            }
            3 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg, HowToOperateFragment2_3())
                    .addToBackStack(null)
                    .commit()
            }

        }

    }

}