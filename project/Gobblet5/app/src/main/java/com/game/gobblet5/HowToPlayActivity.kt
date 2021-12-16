package com.game.gobblet5

import android.os.Handler
import android.os.Looper
import kotlinx.android.synthetic.main.activity_tutorial.*

class HowToPlayActivity : baseTutorial() {
    override val actID: Int = 1
    override val maxPage = 12
    override var Page:Int = 1
    //タイマー関係
    private val millisecond:Long=400
    private var time = 0L
    val handler = Handler(Looper.getMainLooper())
    private var nowDoingTimerID = 0

    override fun iniActName() { textWhereIsHere.text = getString(R.string.HowToPlayActivity) }

    override fun changeText() {
        when (Page) {
            1  -> tutorialText.text = getString(R.string.HowToPlayTutorialText1)
            2  -> tutorialText.text = getString(R.string.HowToPlayTutorialText2)
            3  -> tutorialText.text = getString(R.string.HowToPlayTutorialText3)
            4  -> tutorialText.text = getString(R.string.HowToPlayTutorialText4)
            5  -> tutorialText.text = getString(R.string.HowToPlayTutorialText5)
            6  -> tutorialText.text = getString(R.string.HowToPlayTutorialText6)
            7  -> tutorialText.text = getString(R.string.HowToPlayTutorialText7)
            8  -> tutorialText.text = getString(R.string.HowToPlayTutorialText8)
            9  -> tutorialText.text = getString(R.string.HowToPlayTutorialText9)
            10 -> tutorialText.text = getString(R.string.HowToPlayTutorialText10)
            11 -> tutorialText.text = getString(R.string.HowToPlayTutorialText11)
            12 -> tutorialText.text = getString(R.string.HowToPlayTutorialText12)
            13 -> tutorialText.text = getString(R.string.HowToPlayTutorialText13)
        }
    }

    override fun changeImg() {
        when (Page) {
            1 -> { tutorialImg.setImageResource(R.drawable.tu1) }
            2 -> { tutorialImg.setImageResource(R.drawable.tu2) }
            3 -> { tutorialImg.setImageResource(R.drawable.tu2) }
            4 -> { tutorialImg.setImageResource(R.drawable.tu4) }
            5 -> { tutorialImg.setImageResource(R.drawable.tu5) }
            6 -> {
                handler.removeCallbacks(fragment7)
                time = 0L
                nowDoingTimerID = 0
                tutorialImg.setImageResource(R.drawable.tu6)
            }
            7 -> {
                handler.post(fragment7)
                nowDoingTimerID = 7
            }
            8 -> {
                handler.removeCallbacks(fragment7)
                handler.removeCallbacks(fragment9)
                time = 0L
                nowDoingTimerID = 0
                tutorialImg.setImageResource(R.drawable.tu8)
            }
            9 -> {
                handler.removeCallbacks(fragment7)
                handler.removeCallbacks(fragment10)
                time = 0L
                handler.post(fragment9)
                nowDoingTimerID = 9
            }
            10 -> {
                handler.removeCallbacks(fragment9)
                time = 0L
                handler.post(fragment10)
                nowDoingTimerID = 10
            }
            11 -> {
                handler.removeCallbacks(fragment10)
                handler.removeCallbacks(fragment12)
                time = 0L
                nowDoingTimerID = 0
                tutorialImg.setImageResource(R.drawable.tu10_2)
            }
            12 -> {
                handler.post(fragment12)
                time = 0L
                nowDoingTimerID = 12

            }
            13 -> {
                handler.removeCallbacks(fragment12)
                time = 0L
                nowDoingTimerID = 0
                tutorialImg.setImageResource(R.drawable.tu11_2)
            }

        }

    }

    private val fragment7: Runnable = object : Runnable{
        override fun run() {
            when(time){
                0L -> { tutorialImg.setImageResource(R.drawable.tu7_1) }
                800L -> { tutorialImg.setImageResource(R.drawable.tu7_2) }
            }
            time += millisecond
            handler.postDelayed(this,millisecond)
            if (time>1000L){
                handler.removeCallbacks(this)
                time = 0L
                nowDoingTimerID = 0
            }
        }
    }

    private val fragment9: Runnable = object : Runnable{
        override fun run() {
            when(time){
                0L -> { tutorialImg.setImageResource(R.drawable.tu8) }
                800L -> { tutorialImg.setImageResource(R.drawable.tu9) }
            }
            time += millisecond
            handler.postDelayed(this,millisecond)
            if (time>1000L){
                handler.removeCallbacks(this)
                time = 0L
                nowDoingTimerID = 0
            }
        }
    }

    private val fragment10: Runnable = object : Runnable{
        override fun run() {
            when(time){
                0L -> {
                    tutorialImg.setImageResource(R.drawable.tu10_1)
                }
                800L -> {
                    tutorialImg.setImageResource(R.drawable.tu10_2)
                }
            }
            time += millisecond
            handler.postDelayed(this,millisecond)
            if (time>1000L){
                handler.removeCallbacks(this)
                time = 0L
                nowDoingTimerID = 0
            }
        }
    }

    private val fragment12: Runnable = object : Runnable{
        override fun run() {
            when(time){
                0L -> {
                    tutorialImg.setImageResource(R.drawable.tu11_1)
                }
                800L -> {
                    tutorialImg.setImageResource(R.drawable.tu11_2)
                }
            }
            time += millisecond
            handler.postDelayed(this,millisecond)
            if (time>1000L){
                handler.removeCallbacks(this)
                time = 0L
                nowDoingTimerID = 0
            }
        }
    }

    override fun onResume() {
        super.onResume()
        when (nowDoingTimerID){
            7 -> handler.post(fragment7)
            9 -> handler.post(fragment9)
            10 -> handler.post(fragment10)
            12 -> handler.post(fragment12)
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(fragment7)
        handler.removeCallbacks(fragment9)
        handler.removeCallbacks(fragment10)
        handler.removeCallbacks(fragment12)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(fragment7)
        handler.removeCallbacks(fragment9)
        handler.removeCallbacks(fragment10)
        handler.removeCallbacks(fragment12)
    }
}


