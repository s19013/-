package com.example.gobblet5

import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.gobblet5.HowToOperateFragment.HowToOperateFragment2_1
import com.example.gobblet5.HowToOperateFragment.HowToOperateFragment2_2
import com.example.gobblet5.HowToOperateFragment.HowToOperateFragment2_3
import kotlinx.android.synthetic.main.activity_how_to_operate1.*
import kotlinx.android.synthetic.main.activity_how_to_operate2.*
import kotlinx.android.synthetic.main.activity_how_to_operate2.backButton
import kotlinx.android.synthetic.main.activity_how_to_operate2.currentPageText
import kotlinx.android.synthetic.main.activity_how_to_operate2.maxPageText
import kotlinx.android.synthetic.main.activity_how_to_operate2.nextButton
import kotlinx.android.synthetic.main.activity_how_to_operate2.preButton
import kotlinx.android.synthetic.main.activity_how_to_operate2.tutorialText

class HowToOperateActivity2 : BaseClass() {
    val maxPage = 3
    var Page:Int = 1

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

    fun changeElements(){
        playSound(pageSE)
        changeText()
        changeImg()
        changeCurrentPage()
    }

    fun countUpPage() {
        if (Page < maxPage) {
            Page += 1
            changeElements()
        }
        if (Page > maxPage) { Page = maxPage }

    }

    fun countDownPage() {
        if (Page > 1) {
            Page -= 1
            changeElements()
        }

        if (Page < 1) { Page = 1 }
    }

    fun changeText() {
        when (Page) {
            1 -> tutorialText.text = getString(R.string.HowToOperateTutorialText2_1)
            2 -> tutorialText.text = getString(R.string.HowToOperateTutorialText2_2)
            3 -> tutorialText.text = getString(R.string.HowToOperateTutorialText2_3)
        }
    }

    fun changeCurrentPage() { currentPageText.text = Page.toString() }

    fun changeImg() {
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