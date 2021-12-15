package com.game.gobblet5

import com.game.gobblet5.HowToOperateFragment.*
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
            1 -> { tutorialImg.setImageResource(R.drawable.mas_to_mas1) }
            2 -> { tutorialImg.setImageResource(R.drawable.mas_to_mas2) }
            3 -> { tutorialImg.setImageResource(R.drawable.mas_to_mas3) }
        }
    }
}