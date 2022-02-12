package com.game.gobblet5

import android.graphics.Color
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_game_with_man.*


class GameWithManActivity : GameBaseClass() {
    override var thisAct: Int = actID.gameWithMan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_with_man)

        //初期化
        //共通な初期化
        iniStandard()

        //ゲームを始める
        startTurn()


//手持ちのボタンを触った時
        buttonTemochiRedBig!!.setOnClickListener {
            if (turn == 1 && !finished){
                //movingSourceが同じときやり直しができる
                if (movingSource == temochiRedBig){ resetTemochi() }
                //移動元が手持ちの場合のみコマを
                else if (movingSource== null || movingSource is Temochi){ pickupTemochi(temochiRedBig) }
            }
            else if (finished){return@setOnClickListener} //決着ついていたらなにもしない
            else {toastNotYourTurn()}
        }

        buttonTemochiRedMiddle!!.setOnClickListener {
            if (turn == 1 && !finished){
                if (movingSource==temochiRedMiddle){ resetTemochi() }
                else if (movingSource== null || movingSource is Temochi){ pickupTemochi(temochiRedMiddle) }
            }
            else if (finished){return@setOnClickListener}
            else {toastNotYourTurn()}
        }

        buttonTemochiRedSmall!!.setOnClickListener {
            if (turn == 1 && !finished){
                if (movingSource==temochiRedSmall){ resetTemochi() }
                else if (movingSource== null ||movingSource is Temochi){ pickupTemochi(temochiRedSmall) }
            }
            else if (finished){return@setOnClickListener}
            else {toastNotYourTurn()}
        }

        buttonTemochiGreenBig!!.setOnClickListener {
            if (turn == -1 && !finished){
                if (movingSource==temochiGreenBig){ resetTemochi() }
                else if (movingSource== null ||
                    movingSource==temochiGreenMiddle||
                    movingSource==temochiGreenSmall) { pickupTemochi(temochiGreenBig) }
            }
            else if (finished){return@setOnClickListener}
            else {toastNotYourTurn()}
        }

        buttonTemochiGreenMiddle!!.setOnClickListener {
            if (turn == -1 && !finished){
                if (movingSource==temochiGreenMiddle){ resetTemochi() }
                else if (movingSource== null || movingSource is Temochi){ pickupTemochi(temochiGreenMiddle) }
            }
            else if (finished){return@setOnClickListener}
            else {toastNotYourTurn()}
        }

        buttonTemochiGreenSmall!!.setOnClickListener {
            if (turn == -1 && !finished){
                if (movingSource==temochiGreenSmall){ resetTemochi() }
                else if (movingSource==null || movingSource is Temochi){ pickupTemochi(temochiGreenSmall) }
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
            sound.playSound(sound.openSE)
            showConfigPopup()
        }
        resultButton!!.setOnClickListener {
            sound.playSound(sound.openSE)
            showResultPopup()
        }

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
                telop1p.setBackgroundColor(Color.rgb(230, 230, 230))
                telop2p.setBackgroundColor(Color.rgb(188, 255, 173))
            }
            0 -> {
                telop1p.setBackgroundColor(Color.rgb(230, 230, 230))
                telop2p.setBackgroundColor(Color.rgb(230, 230, 230))
            }
        }
    }

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
}