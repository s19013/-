package com.game.gobblet5

import kotlinx.android.synthetic.main.activity_tutorial.*

class HowToOperateActivity1 : BaseTutorial() {
    override val maxPage = 3
    override var page:Int = 1
    override val actID = 1

    override fun iniActName() { textWhereIsHere.text = getString(R.string.MovePieceFromHandToSquare) }

    override fun changeText() {
        when (page) {
            1 -> tutorialText.text = getString(R.string.HowToOperateTutorialText1_1)
            2 -> tutorialText.text = getString(R.string.HowToOperateTutorialText1_2)
            3 -> tutorialText.text = getString(R.string.HowToOperateTutorialText1_3)
        }
    }

    override fun changeImg() {
        when(page) {
            1 -> { tutorialImg.setImageResource(R.drawable.temo_to_mas1) }
            2 -> { tutorialImg.setImageResource(R.drawable.temo_to_mas2) }
            3 -> { tutorialImg.setImageResource(R.drawable.temo_to_mas3) }
        }
    }
}