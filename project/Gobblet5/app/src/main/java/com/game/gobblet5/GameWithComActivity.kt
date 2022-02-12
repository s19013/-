package com.game.gobblet5

import android.os.Bundle
import android.graphics.Color
import kotlinx.android.synthetic.main.activity_game_with_com.*
import kotlinx.android.synthetic.main.activity_game_with_com.buttonA1
import kotlinx.android.synthetic.main.activity_game_with_com.buttonA2
import kotlinx.android.synthetic.main.activity_game_with_com.buttonA3
import kotlinx.android.synthetic.main.activity_game_with_com.buttonA4
import kotlinx.android.synthetic.main.activity_game_with_com.buttonB1
import kotlinx.android.synthetic.main.activity_game_with_com.buttonB2
import kotlinx.android.synthetic.main.activity_game_with_com.buttonB3
import kotlinx.android.synthetic.main.activity_game_with_com.buttonB4
import kotlinx.android.synthetic.main.activity_game_with_com.buttonC1
import kotlinx.android.synthetic.main.activity_game_with_com.buttonC2
import kotlinx.android.synthetic.main.activity_game_with_com.buttonC3
import kotlinx.android.synthetic.main.activity_game_with_com.buttonC4
import kotlinx.android.synthetic.main.activity_game_with_com.buttonD1
import kotlinx.android.synthetic.main.activity_game_with_com.buttonD2
import kotlinx.android.synthetic.main.activity_game_with_com.buttonD3
import kotlinx.android.synthetic.main.activity_game_with_com.buttonD4
import kotlinx.android.synthetic.main.activity_game_with_com.configButton
import kotlinx.android.synthetic.main.activity_game_with_com.telop1p
import kotlinx.android.synthetic.main.activity_game_with_com.telop2p
import kotlinx.android.synthetic.main.activity_game_with_man.*

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
                if (movingSource == stringTemochiRedBig){ resetTemochi() }
                //移動元が手持ちの場合のみコマを
                else if (movingSource== null || movingSource is Temochi ){ pickupTemochi(temochiRedBig) }
            }
            else if (finished){} //決着ついていたらなにもしない
            else {toastNotYourTurn()}
        }

        buttonTemochiRedMiddle!!.setOnClickListener {
            if (turn == 1 && !finished){
                if (movingSource==stringTemochiRedMiddle){ resetTemochi() }
                else if (movingSource== null || movingSource is Temochi ){ pickupTemochi(temochiRedMiddle) }
            }
            else if (finished){}
            else {toastNotYourTurn()}
        }

        buttonTemochiRedSmall!!.setOnClickListener {
            if (turn == 1 && !finished){
                if (movingSource==stringTemochiRedSmall){ resetTemochi() }
                else if (movingSource== null || movingSource is Temochi ){ pickupTemochi(temochiRedSmall) }
            }
            else if (finished){}
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
            sound.playSound(sound.openSE)
            showConfigPopup()
        }

        resultButton!!.setOnClickListener {
            sound.playSound(sound.openSE)
            showResultPopup()
        }

    }

    //コンピューター関係
    private fun iniCom(){
        //コンピューターにわたすよう?
        com.iniLines(
            mutableListOf(bord.A1, bord.B1, bord.C1, bord.D1),//l1
            mutableListOf(bord.A2, bord.B2, bord.C2, bord.D2),//l2
            mutableListOf(bord.A3, bord.B3, bord.C3, bord.D3),//l3
            mutableListOf(bord.A4, bord.B4, bord.C4, bord.D4),//l4
            mutableListOf(bord.A1, bord.A2, bord.A3, bord.A4),//lA
            mutableListOf(bord.B1, bord.B2, bord.B3, bord.B4),//lB
            mutableListOf(bord.C1, bord.C2, bord.C3, bord.C4),//lC
            mutableListOf(bord.D1, bord.D2, bord.D3, bord.D4),//lD
            mutableListOf(bord.A1, bord.B2, bord.C3, bord.D4),//lS
            mutableListOf(bord.A4, bord.B3, bord.C2, bord.D1),//lBS
        )
        com.iniConcatLine()
        com.iniTemochi(temochiGreenBig,temochiGreenMiddle,temochiGreenSmall)
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
        val rv = com.movingSourceGetter()

        when (rv) {
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

    //このアクティビティ内のviewを取得?

    override fun iniView() {
        //テキスト
        textTemochiRedBig=findViewById(R.id.textTemochiRedBig)
        textTemochiRedMiddle=findViewById(R.id.textTemochiRedMiddle)
        textTemochiRedSmall=findViewById(R.id.textTemochiRedSmall)
        textTemochiGreenBig=findViewById(R.id.textTemochiGreenBig)
        textTemochiGreenMiddle=findViewById(R.id.textTemochiGreenMiddle)
        textTemochiGreenSmall=findViewById(R.id.textTemochiGreenSmall)

        //一部ボタン
        buttonTemochiRedBig=findViewById(R.id.buttonTemochiRedBig)
        buttonTemochiRedMiddle=findViewById(R.id.buttonTemochiRedMiddle)
        buttonTemochiRedSmall=findViewById(R.id.buttonTemochiRedSmall)
        buttonTemochiGreenBig=findViewById(R.id.buttonTemochiGreenBig)
        buttonTemochiGreenMiddle=findViewById(R.id.buttonTemochiGreenMiddle)
        buttonTemochiGreenSmall=findViewById(R.id.buttonTemochiGreenSmall)
        resultButton=findViewById(R.id.resaltButton)
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