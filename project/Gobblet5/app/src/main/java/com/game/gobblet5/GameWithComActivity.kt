package com.game.gobblet5

import android.os.Bundle
import android.graphics.Color
import kotlinx.android.synthetic.main.activity_game_with_com.*


class GameWithComActivity : GameBaseClass() {
    override var thisAct: Int = actID.gameWithCom

    private val insertTimerId = "insertTimer"

    //コンピューター宣言
    val com:Com=Com()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_with_com)

        //初期化
        //共通な初期化
        iniStandard()
        //コンピューターの初期化
        iniCom()

        //ゲームを始める
        startTurn()

        //手持ちのボタンを触った時
        buttonTemochiRedBig!!.setOnClickListener {
            if (turn == 1 && !finished){
                //movingSourceが同じときやり直しができる
                if (movingSource == p1Temochi.big){ resetTemochi() }
                //移動元が手持ちの場合のみコマを
                else if (movingSource== null || movingSource is Temochi ){ pickupTemochi(p1Temochi.big) }
            }
            else if (finished){return@setOnClickListener} //決着ついていたらなにもしない
            else {toastNotYourTurn()}
        }

        buttonTemochiRedMiddle!!.setOnClickListener {
            if (turn == 1 && !finished){
                if (movingSource==p1Temochi.middle){ resetTemochi() }
                else if (movingSource== null || movingSource is Temochi ){ pickupTemochi(p1Temochi.middle) }
            }
            else if (finished){return@setOnClickListener}
            else {toastNotYourTurn()}
        }

        buttonTemochiRedSmall!!.setOnClickListener {
            if (turn == 1 && !finished){
                if (movingSource==p1Temochi.small){ resetTemochi() }
                else if (movingSource== null || movingSource is Temochi ){ pickupTemochi(p1Temochi.small) }
            }
            else if (finished){return@setOnClickListener}
            else {toastNotYourTurn()}
        }

        ////マスを触ったとき
        buttonA1.setOnClickListener { pushedMasButton(bord.A1) } //nameGetterを使ってマスの名前を入れる

        buttonA2.setOnClickListener { pushedMasButton(bord.A2) }

        buttonA3.setOnClickListener { pushedMasButton(bord.A3) }

        buttonA4.setOnClickListener { pushedMasButton(bord.A4) }

        buttonB1.setOnClickListener { pushedMasButton(bord.B1) }

        buttonB2.setOnClickListener { pushedMasButton(bord.B2) }

        buttonB3.setOnClickListener { pushedMasButton(bord.B3) }

        buttonB4.setOnClickListener { pushedMasButton(bord.B4) }

        buttonC1.setOnClickListener { pushedMasButton(bord.C1) }

        buttonC2.setOnClickListener { pushedMasButton(bord.C2) }

        buttonC3.setOnClickListener { pushedMasButton(bord.C3) }

        buttonC4.setOnClickListener { pushedMasButton(bord.C4) }

        buttonD1.setOnClickListener { pushedMasButton(bord.D1) }

        buttonD2.setOnClickListener { pushedMasButton(bord.D2) }

        buttonD3.setOnClickListener { pushedMasButton(bord.D3) }

        buttonD4.setOnClickListener { pushedMasButton(bord.D4) }

        // その他
        configButton.setOnClickListener {
            sound.playSound(sound.openSE,save.seVolume)
            showConfigPopup()
        }

        resultButton!!.setOnClickListener {
            sound.playSound(sound.openSE,save.seVolume)
            showResultPopup()
        }

    }

    //コンピューター関係
    private fun iniCom(){
        //コンピューターにわたすよう?
        com.iniLines(
            bord.line1,//l1
            bord.line2,//l2
            bord.line3,//l3
            bord.line4,//l4
            bord.lineA,//lA
            bord.lineB,//lB
            bord.lineC,//lC
            bord.lineD,//lD
            bord.lineS,//lS
            bord.lineBS//lBS
        )
        com.iniConcatLine()
        com.iniTemochi(p2Temochi.big,p2Temochi.middle,p2Temochi.small)
    }

    private fun startCom(){
        com.start()
        pickUpCom()
        nowDoingTimerID = insertTimerId
        if (turn != 0 && !finished) {
            handler.post(insertTimer)
            resetCom()
        }
    }

    private fun pickUpCom(){
        when (val rv = com.movingSourceGetter()) {
            is Mas -> { pushedMasButton(rv) } //移動元がマスだった場合
            is Temochi -> { pickupTemochi(rv) } //移動元が手持ちだった場合
        }
    }

    private fun insertCom(){ pushedMasButton(com.destinationGetter()!!) }

    private fun resetCom(){
        com.resetScore()
        com.resetLists()
    }

    //ターン開始の処理
    override fun startTurn(){
        if (finished){turn = 0}
        when(turn){
            1 -> {
                telop1p.setBackgroundColor(Color.rgb(255, 173, 173))
                telop2p.setBackgroundColor(Color.rgb(230, 230, 230))
            }
            -1 -> {
                startCom()
                telop1p.setBackgroundColor(Color.rgb(230, 230, 230))
                telop2p.setBackgroundColor(Color.rgb(188, 255, 173))
            }
            0 -> {
                telop1p.setBackgroundColor(Color.rgb(230, 230, 230))
                telop2p.setBackgroundColor(Color.rgb(230, 230, 230))
            }
        }
    }

    private val insertTimer: Runnable = object : Runnable{
        override fun run() {
            time += millisecond
            handler.postDelayed(this,millisecond)
            if (time>1000L){
                insertCom()
                handler.removeCallbacks(this)
                time = 0L
                nowDoingTimerID = null
            }
        }
    }

    //タイマー再開
    override fun onResume() {
        super.onResume()
        when (nowDoingTimerID){insertTimerId -> handler.post(insertTimer) }
    }

    //タイマーを止める
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(insertTimer)
    }

    //タイマーを止める
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(insertTimer)
    }
}