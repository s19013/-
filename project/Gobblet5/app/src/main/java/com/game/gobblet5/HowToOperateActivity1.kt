package com.game.gobblet5

import com.game.gobblet5.HowToOperateFragment.*
import kotlinx.android.synthetic.main.activity_tutorial.*

class HowToOperateActivity1 : baseTutorial() {
    override val maxPage = 3
    override var Page:Int = 1

    override fun iniActName() { textWhereIsHere.text = getString(R.string.MovePieceFromHandToSquare) }

    override fun changeText() {
        when (Page) {
            1 -> tutorialText.text = getString(R.string.HowToOperateTutorialText1_1)
            2 -> tutorialText.text = getString(R.string.HowToOperateTutorialText1_2)
            3 -> tutorialText.text = getString(R.string.HowToOperateTutorialText1_3)
        }
    }

    override fun changeImg() {
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