package com.example.gobblet5

import com.example.gobblet5.HowToOperateFragment.*
import kotlinx.android.synthetic.main.activity_tutorial.*

class HowToOperateActivity2 : baseTutorial() {
    override val maxPage = 3
    override var Page:Int = 1

    override fun iniActName() { textWhereIsHere.text = getString(R.string.MovePieceFromSquareToSquare) }

    override fun changeText() {
        when (Page) {
            1 -> tutorialText.text = getString(R.string.HowToOperateTutorialText2_1)
            2 -> tutorialText.text = getString(R.string.HowToOperateTutorialText2_2)
            3 -> tutorialText.text = getString(R.string.HowToOperateTutorialText2_3)
        }
    }

    override fun changeImg() {
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