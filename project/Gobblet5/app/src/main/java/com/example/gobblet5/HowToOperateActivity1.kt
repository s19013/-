package com.example.gobblet5

import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.gobblet5.HowToOperateFragment.HowToOperateFragment1_1
import com.example.gobblet5.HowToOperateFragment.HowToOperateFragment1_2
import com.example.gobblet5.HowToOperateFragment.HowToOperateFragment1_3
import kotlinx.android.synthetic.main.activity_how_to_operate1.*

class HowToOperateActivity1 : AppCompatActivity() {
    val maxPage = 3
    var Page:Int = 1
    //音関係
    private lateinit var sp: SoundPool
    private var cancelSE = 0
    private var pageSE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_operate1)

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
            val intent = Intent(this,SelectHowToOperateActivity::class.java)
            startActivity(intent)
        }

        maxPageText.text = maxPage.toString()

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

    fun changeText() {
        when (Page) {
            1 -> tutorialText.text = getString(R.string.HowToOperateTutorialText1_1)
            2 -> tutorialText.text = getString(R.string.HowToOperateTutorialText1_2)
            3 -> tutorialText.text = getString(R.string.HowToOperateTutorialText1_3)
        }
    }

    fun changeCurrentPage() {
        currentPageText.text = Page.toString()
    }

    fun changeImg() {
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

}