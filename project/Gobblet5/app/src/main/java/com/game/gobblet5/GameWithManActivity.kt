package com.game.gobblet5

import android.graphics.Color
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_game_with_man.*


class GameWithManActivity : GameBaseClass() {
    override var thisAct: Int = 1


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
                if (movingSource == stringTemochiRedBig){
                    resetTemochi()
                }
                //移動元が手持ちの場合のみコマを
                else if (movingSource== null ||
                    movingSource==stringTemochiRedMiddle||
                    movingSource==stringTemochiRedSmall){ pickupTemochi(temochiRedBig) }
            }
            else if (finished){} //決着ついていたらなにもしない
            else {toastNotYourTurn()}
        }

        buttonTemochiRedMiddle!!.setOnClickListener {
            if (turn == 1 && !finished){
                if (movingSource==stringTemochiRedMiddle){
                    resetTemochi()
                }
                else if (movingSource== null ||
                    movingSource==stringTemochiRedBig ||
                    movingSource==stringTemochiRedSmall){ pickupTemochi(temochiRedMiddle) }
            }
            else if (finished){}
            else {toastNotYourTurn()}
        }

        buttonTemochiRedSmall!!.setOnClickListener {
            if (turn == 1 && !finished){
                if (movingSource==stringTemochiRedSmall){
                    resetTemochi()
                }
                else if (movingSource== null ||
                    movingSource==stringTemochiRedBig ||
                    movingSource==stringTemochiRedMiddle){ pickupTemochi(temochiRedSmall) }
            }
            else if (finished){}
            else {toastNotYourTurn()}
        }

        buttonTemochiGreenBig!!.setOnClickListener {
            if (turn == -1 && !finished){
                if (movingSource==stringTemochiGreenBig){
                    resetTemochi()
                }
                else if (movingSource== null ||
                    movingSource==stringTemochiGreenMiddle||
                    movingSource==stringTemochiGreenSmall) { pickupTemochi(temochiGreenBig) }
            }
            else if (finished){}
            else {toastNotYourTurn()}
        }

        buttonTemochiGreenMiddle!!.setOnClickListener {
            if (turn == -1 && !finished){
                if (movingSource==stringTemochiGreenMiddle){
                    resetTemochi()
                }
                else if (movingSource== null ||
                    movingSource==stringTemochiGreenBig ||
                    movingSource==stringTemochiGreenSmall){ pickupTemochi(temochiGreenMiddle) }
            }
            else if (finished){}
            else {toastNotYourTurn()}
        }

        buttonTemochiGreenSmall!!.setOnClickListener {
            if (turn == -1 && !finished){
                if (movingSource==stringTemochiGreenSmall){
                    resetTemochi()
                }
                else if (movingSource==null||
                    movingSource==stringTemochiGreenBig ||
                    movingSource==stringTemochiGreenMiddle){ pickupTemochi(temochiGreenSmall) }
            }
            else if (finished){}
            else {toastNotYourTurn()}
        }
      ////マスを触ったとき
        buttonA1.setOnClickListener {
            //ゲームが終わったらさわれないようにするためにこんなif文を書く
            pushedMasButton(A1) //nameGetterを使ってマスの名前を入れる
        }

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