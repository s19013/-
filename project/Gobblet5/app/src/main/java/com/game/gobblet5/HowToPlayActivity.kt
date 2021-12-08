package com.game.gobblet5

import android.os.Handler
import android.os.Looper
import com.game.gobblet5.HowToPlayFragment.*
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
        }
    }

    override fun changeImg() {
        when (Page) {
            1 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment1())
                    .addToBackStack(null)
                    .commit()
            }
            2 -> {
                handler.removeCallbacks(fragment3)
                time = 0L
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment2())
                    .addToBackStack(null)
                    .commit()
            }
            3 -> {
                handler.post(fragment3)
                nowDoingTimerID = 3
            }
            4 -> {
                handler.removeCallbacks(fragment3)
                time = 0L
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment4())
                    .addToBackStack(null)
                    .commit()
            }
            5 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment5())
                    .addToBackStack(null)
                    .commit()
            }
            6 -> {
                handler.removeCallbacks(fragment7)
                time = 0L
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment6())
                    .addToBackStack(null)
                    .commit()
            }
            7 -> {
                handler.post(fragment7)
                nowDoingTimerID = 7
            }
            8 -> {
                handler.removeCallbacks(fragment7)
                handler.removeCallbacks(fragment9)
                time = 0L
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment8_1())
                    .addToBackStack(null)
                    .commit()
            }
            9 -> {
                handler.removeCallbacks(fragment7)
                time = 0L
                handler.post(fragment9)
                nowDoingTimerID = 9
            }
            10 -> {
                handler.removeCallbacks(fragment9)
                time = 0L
                handler.post(fragment10)
                nowDoingTimerID = 9
            }
            11 -> {
                handler.removeCallbacks(fragment10)
                time = 0L
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment9_2())
                    .addToBackStack(null)
                    .commit()
            }
            12 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment9_2())
                    .addToBackStack(null)
                    .commit()
            }
        }

    }

    private val fragment3: Runnable = object : Runnable {
        override fun run() {
            when(time){
                0L ->{
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment3_1())
                        .addToBackStack(null)
                        .commit()
                }
                800L -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment3_2())
                        .addToBackStack(null)
                        .commit()
                }
                1600L -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment3_3())
                        .addToBackStack(null)
                        .commit()
                }
                2400L -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment3_4())
                        .addToBackStack(null)
                        .commit()

                }
            }
            time += millisecond
            handler.postDelayed(this,millisecond)
            if (time>2500L){
                handler.removeCallbacks(this)
                time = 0L
                nowDoingTimerID = 0
            }
        }
    }

    private val fragment7: Runnable = object : Runnable{
        override fun run() {
            when(time){
                0L -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment7_1())
                        .addToBackStack(null)
                        .commit()
                }
                800L -> {
                    val fragment = HowToPlayFragment7_2()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,fragment)
                        .addToBackStack(null)
                        .commit()
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

    private val fragment9: Runnable = object : Runnable{
        override fun run() {
            when(time){
                0L -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment8_1())
                        .addToBackStack(null)
                        .commit()
                }
                800L -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment8_2())
                        .addToBackStack(null)
                        .commit()
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

    private val fragment10: Runnable = object : Runnable{
        override fun run() {
            when(time){
                0L -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment9_1())
                        .addToBackStack(null)
                        .commit()
                }
                800L -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment9_2())
                        .addToBackStack(null)
                        .commit()
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
            3 -> handler.post(fragment3)
            7 -> handler.post(fragment7)
            9 -> handler.post(fragment9)
            10 -> handler.post(fragment10)
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(fragment3)
        handler.removeCallbacks(fragment7)
        handler.removeCallbacks(fragment9)
        handler.removeCallbacks(fragment10)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(fragment3)
        handler.removeCallbacks(fragment7)
        handler.removeCallbacks(fragment9)
        handler.removeCallbacks(fragment10)
    }
}


