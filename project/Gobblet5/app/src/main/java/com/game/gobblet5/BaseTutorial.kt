package com.game.gobblet5

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tutorial.*

open class BaseTutorial: BaseClass() {
    open val maxPage = 0
    open var page:Int = 0
    open val actID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        nextButton.setOnClickListener { countUpPage() }

        preButton.setOnClickListener { countDownPage() }

        backButton.setOnClickListener {
            sound.playSound(sound.cancelSE,save.seVolume)
            var intent:Intent? = null

            if (actID == 1){ intent = Intent(this,SelectTutorialActivity::class.java) }
            else {intent = Intent(this,SelectHowToOperateActivity::class.java)}
            startActivity(intent)
        }

        maxPageText.text = maxPage.toString()
        changeText()
        changeImg()
        iniActName()
    }

    open fun iniActName(){  }

    private fun changeElements(){
        sound.playSound(sound.pageSE,save.seVolume)
        changeText()
        changeImg()
        changeCurrentPage()
    }

    open fun changeText() {}

    private fun changeCurrentPage() { currentPageText.text = page.toString() }

    open fun changeImg() {}

    private fun countUpPage() {
        if (page < maxPage) {
            page += 1
            changeElements()
        }
        if (page > maxPage) { page = maxPage }
    }

    private fun countDownPage() {
        if (page > 1) {
            page -= 1
            changeElements()
        }

        if (page < 1) { page = 1 }
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