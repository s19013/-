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
    override var thisAct: Int = -1

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
        buttonA1.setOnClickListener { pushedMasButton(A1) } //nameGetterを使ってマスの名前を入れる

        buttonA2.setOnClickListener { pushedMasButton(A2) }

        buttonA3.setOnClickListener { pushedMasButton(A3) }

        buttonA4.setOnClickListener { pushedMasButton(A4) }

        buttonB1.setOnClickListener { pushedMasButton(B1) }

        buttonB2.setOnClickListener { pushedMasButton(B2) }

        buttonB3.setOnClickListener { pushedMasButton(B3) }

        buttonB4.setOnClickListener { pushedMasButton(B4) }

        buttonC1.setOnClickListener { pushedMasButton(C1) }

        buttonC2.setOnClickListener { pushedMasButton(C2) }

        buttonC3.setOnClickListener { pushedMasButton(C3) }

        buttonC4.setOnClickListener { pushedMasButton(C4) }

        buttonD1.setOnClickListener { pushedMasButton(D1) }

        buttonD2.setOnClickListener { pushedMasButton(D2) }

        buttonD3.setOnClickListener { pushedMasButton(D3) }

        buttonD4.setOnClickListener { pushedMasButton(D4) }

        // その他
        configButton.setOnClickListener {
            playSound(openSE)
            showConfigPopup()
        }

        resultButton!!.setOnClickListener {
            playSound(openSE)
            showResultPopup()
        }

    }

    //コンピューター関係
    private fun iniCom(){
        //コンピューターにわたすよう?
        com.iniLines(
            mutableListOf(A1, B1, C1, D1),//l1
            mutableListOf(A2, B2, C2, D2),//l2
            mutableListOf(A3, B3, C3, D3),//l3
            mutableListOf(A4, B4, C4, D4),//l4
            mutableListOf(A1, A2, A3, A4),//lA
            mutableListOf(B1, B2, B3, B4),//lB
            mutableListOf(C1, C2, C3, C4),//lC
            mutableListOf(D1, D2, D3, D4),//lD
            mutableListOf(A1, B2, C3, D4),//lS
            mutableListOf(A4, B3, C2, D1),//lBS
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