package com.example.gobblet5

import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment1
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment2
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment3_1
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment3_2
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment3_3
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment3_4
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment4
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment5
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment6
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment7_1
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment7_2
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment8_1
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment8_2
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment9_1
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment9_2
import kotlinx.android.synthetic.main.activity_how_to_play.*


class HowToPlayActivity : AppCompatActivity() {
    private val maxPage = 12
    private var Page:Int = 1
    //タイマー関係
    private val millisecond:Long=100
    private var time = 0L
    //音関係
    private lateinit var sp: SoundPool
    private var cancelSE = 0
    private var pageSE = 0
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_play)

        //共有プリファレンス
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        var SE =pref.getBoolean("SEOnOff", true)
        var BGM =pref.getBoolean("BGMOnOff", true)


        val audioAttributes = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
        } else { TODO("VERSION.SDK_INT < LOLLIPOP") }
        sp = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(1)
            .build()

        cancelSE = sp.load(this, R.raw.cancel, 1)
        pageSE = sp.load(this,R.raw.page_sound,1)

        fun playSound(status: Int){
            Log.d("gobblet2", "status:${status}")
            if (SE){
                when(status){
                     cancelSE -> sp.play(cancelSE, 1.0f, 1.0f, 1, 0, 1.0f)
                     pageSE -> sp.play(pageSE, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }
        }

        nextButton.setOnClickListener {
            countUpPage()
            playSound(pageSE)
        }

        preButton.setOnClickListener {
            countDownPage()
            playSound(pageSE)
        }

        backButton.setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this,SelectTutorialActivity::class.java)
            startActivity(intent)
        }

        maxPageText.text = maxPage.toString()


    }

    private fun countUpPage() {
        Page += 1
        if (Page > maxPage) {
            Page = maxPage
        }
        changeText()
        changeImg()
        changeCurrentPage()
    }

    private fun countDownPage() {
        Page -= 1
        if (Page < 1) {
            Page = 1
        }
        changeText()
        changeImg()
        changeCurrentPage()
    }

    private fun changeCurrentPage() {
        currentPageText.text = Page.toString()
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
            Page == 11 -> tutorialText.text = getString(R.string.HowToPlayTutorialText11)
            Page == 12 -> tutorialText.text = getString(R.string.HowToPlayTutorialText12)
        }
    }

    private fun changeImg() {
        when {
            Page == 1 -> {
                val fragment = HowToPlayFragment1()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,fragment)
                    .addToBackStack(null)
                    .commit()
            }
            Page == 2 -> {
                val fragment = HowToPlayFragment2()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,fragment)
                    .addToBackStack(null)
                    .commit()
            }
            Page == 3 -> {
                val fragment = HowToPlayFragment3_1()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,fragment)
                    .addToBackStack(null)
                    .commit()
                fragment3()
            }
            Page == 4 -> {
                val fragment = HowToPlayFragment4()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,fragment)
                    .addToBackStack(null)
                    .commit()
            }
            Page == 5 -> {
                val fragment = HowToPlayFragment5()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,fragment)
                    .addToBackStack(null)
                    .commit()
            }
            Page == 6 -> {
                val fragment = HowToPlayFragment6()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,fragment)
                    .addToBackStack(null)
                    .commit()
            }
            Page == 7 -> {
                val fragment = HowToPlayFragment7_1()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,fragment)
                    .addToBackStack(null)
                    .commit()
                fragment7()
            }
            Page == 8 -> {
                val fragment = HowToPlayFragment8_1()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,fragment)
                    .addToBackStack(null)
                    .commit()
                fragment8()
            }
            Page == 9 -> {
                val fragment = HowToPlayFragment8_2()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,fragment)
                    .addToBackStack(null)
                    .commit()
            }
            Page == 10 -> {
                val fragment = HowToPlayFragment9_1()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,fragment)
                    .addToBackStack(null)
                    .commit()
                fragment9()
            }
            Page == 11 -> {
                val fragment = HowToPlayFragment9_2()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,fragment)
                    .addToBackStack(null)
                    .commit()
            }
            Page == 12 -> {
                val fragment = HowToPlayFragment9_2()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

    }

    private fun fragment3(){
//        val handler = Handler()
        val timer = object :Runnable{
            override fun run() {
                when(time){
                    1000L -> {
                        val fragment = HowToPlayFragment3_2()
                        val fragmentManager = supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.tutorialImg,fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    2000L -> {
                        val fragment = HowToPlayFragment3_3()
                        val fragmentManager = supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.tutorialImg,fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    3000L -> {
                        val fragment = HowToPlayFragment3_4()
                        val fragmentManager = supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.tutorialImg,fragment)
                            .addToBackStack(null)
                            .commit()

                    }
                }
                time += millisecond
                handler.postDelayed(this,millisecond)
                Log.d("gobblet2", "timer_def:${time}")
                if (time==3000L){
                    handler.removeCallbacks(this)
                    time = 0L
                }
            }
        }
        handler.post(timer)
    }

    private fun fragment7(){
//        val handler = Handler()
        val timer = object :Runnable{
            override fun run() {
                when(time){
                    1500L -> {
                        val fragment = HowToPlayFragment7_2()
                        val fragmentManager = supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.tutorialImg,fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
                time += millisecond
                handler.postDelayed(this,millisecond)
                Log.d("gobblet2", "timer_def:${time}")
                if (time==2000L){
                    handler.removeCallbacks(this)
                    time = 0L
                }
            }
        }
        handler.post(timer)
    }

    private fun fragment8(){
//        val handler = Handler()
        val timer = object :Runnable{
            override fun run() {
                when(time){
                    1500L -> {
                        val fragment = HowToPlayFragment8_2()
                        val fragmentManager = supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.tutorialImg,fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
                time += millisecond
                handler.postDelayed(this,millisecond)
                Log.d("gobblet2", "timer_def:${time}")
                if (time==2000L){
                    handler.removeCallbacks(this)
                    time = 0L
                }
            }
        }
        handler.post(timer)
    }

    private fun fragment9(){
//        val handler = Handler()
        val timer = object :Runnable{
            override fun run() {
                when(time){
                    1500L -> {
                        val fragment = HowToPlayFragment9_2()
                        val fragmentManager = supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.tutorialImg,fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
                time += millisecond
                handler.postDelayed(this,millisecond)
                Log.d("gobblet2", "timer_def:${time}")
                if (time==2000L){
                    handler.removeCallbacks(this)
                    time = 0L
                }
            }
        }
        handler.post(timer)
    }

}


