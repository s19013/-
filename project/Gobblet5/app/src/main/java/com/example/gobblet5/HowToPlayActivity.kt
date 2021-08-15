package com.example.gobblet5

import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.gobblet5.HowToPlayFragment.*
import kotlinx.android.synthetic.main.activity_how_to_play.*



class HowToPlayActivity : AppCompatActivity() {
    private val maxPage = 12
    private var Page:Int = 1
    //タイマー関係
    private val millisecond:Long=100
    private var time = 0L
    val handler = Handler()
    //音関係
    private lateinit var sp: SoundPool
    private var cancelSE = 0
    private var pageSE = 0

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
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment1())
                    .addToBackStack(null)
                    .commit()
            }
            Page == 2 -> {
                handler.removeCallbacks(fragment3)
                time = 0L
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment2())
                    .addToBackStack(null)
                    .commit()
            }
            Page == 3 -> {
                handler.post(fragment3)
            }
            Page == 4 -> {
                handler.removeCallbacks(fragment3)
                time = 0L
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment4())
                    .addToBackStack(null)
                    .commit()
            }
            Page == 5 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment5())
                    .addToBackStack(null)
                    .commit()
            }
            Page == 6 -> {
                handler.removeCallbacks(fragment7)
                time = 0L
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment6())
                    .addToBackStack(null)
                    .commit()
            }
            Page == 7 -> {
                handler.post(fragment7)
            }
            Page == 8 -> {
                handler.removeCallbacks(fragment7)
                handler.removeCallbacks(fragment9)
                time = 0L
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment8_1())
                    .addToBackStack(null)
                    .commit()
            }
            Page == 9 -> {
                handler.removeCallbacks(fragment7)
                time = 0L
                handler.post(fragment9)
            }
            Page == 10 -> {
                handler.post(fragment10)
            }
            Page == 11 -> {
                handler.removeCallbacks(fragment10)
                time = 0L
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment9_2())
                    .addToBackStack(null)
                    .commit()
            }
            Page == 12 -> {
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
                    Log.d("gobblet2", "0L call")
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment3_1())
                        .addToBackStack(null)
                        .commit()
                }
                800L -> {
                    Log.d("gobblet2", "1000L call")
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment3_2())
                        .addToBackStack(null)
                        .commit()
                }
                1600L -> {
                    Log.d("gobblet2", "2000L call")
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment3_3())
                        .addToBackStack(null)
                        .commit()
                }
                2400L -> {
                    Log.d("gobblet2", "3000L call")
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,HowToPlayFragment3_4())
                        .addToBackStack(null)
                        .commit()

                }
            }
            time += millisecond
            handler.postDelayed(this,millisecond)
            Log.d("gobblet2", "timer_def:${time}")
            if (time==2500L){
                handler.removeCallbacks(this)
                time = 0L
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
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.tutorialImg,fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
            time += millisecond
            handler.postDelayed(this,millisecond)
            Log.d("gobblet2", "timer_def:${time}")
            if (time==1000L){
                handler.removeCallbacks(this)
                time = 0L
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
            Log.d("gobblet2", "timer_def:${time}")
            if (time==1000L){
                handler.removeCallbacks(this)
                time = 0L
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
            Log.d("gobblet2", "timer_def:${time}")
            if (time==1000L){
                handler.removeCallbacks(this)
                time = 0L
            }
        }
    }


}


