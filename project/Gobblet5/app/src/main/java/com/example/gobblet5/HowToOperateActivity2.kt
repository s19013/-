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

class HowToOperateActivity2 : AppCompatActivity() {
    val maxPage = 3
    var Page:Int = 1
    //音関係
    private lateinit var sp: SoundPool
    private var cancelSE = 0
    private var pageSE = 0
    //プリファレンス関係
    var pref: SharedPreferences? = null
    var SE = false
    var BGM =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_operate2)

        iniPreference()
        iniSoundPool()

        nextButton.setOnClickListener {
            countUpPage()
        }

        preButton.setOnClickListener {
            countDownPage()
        }

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
        if (Page > maxPage) {
            Page = maxPage
        }

    }

    fun countDownPage() {
        if (Page > 1) {
            Page -= 1
            changeElements()
        }

        if (Page < 1) {
            Page = 1
        }
    }

    fun changeText() {
        when (Page) {
            1 -> tutorialText.text = getString(R.string.HowToOperateTutorialText2_1)
            2 -> tutorialText.text = getString(R.string.HowToOperateTutorialText2_2)
            3 -> tutorialText.text = getString(R.string.HowToOperateTutorialText2_3)
        }
    }

    fun changeCurrentPage() {
        currentPageText.text = Page.toString()
    }

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

    fun playSound(status: Int){
        Log.d("gobblet2", "status:${status}")
        if (SE){
            when(status){
                cancelSE -> sp.play(cancelSE, 1.0f, 1.0f, 1, 0, 1.0f)
                pageSE -> sp.play(pageSE, 1.0f, 1.0f, 1, 0, 1.0f)
            }
        }
    }

    private fun iniPreference(){
        //共有プリファレンス
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        SE = pref!!.getBoolean("SEOnOff", true)
        BGM =pref!!.getBoolean("BGMOnOff", true)
    }

    private fun iniSoundPool(){
        //soundPool
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
    }
}