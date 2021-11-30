package com.example.gobblet5

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
                    movingSource==stringTemochiRedSmall){
                    pickupTemochi(temochiRedBig)
                }
            }
            else{toastNotYourTurn()}
        }

        buttonTemochiRedMiddle!!.setOnClickListener {
            if (turn == 1 && !finished){
                if (movingSource==stringTemochiRedMiddle){
                    resetTemochi()
                }
                else if (movingSource== null ||
                    movingSource==stringTemochiRedBig ||
                    movingSource==stringTemochiRedSmall){
                    pickupTemochi(temochiRedMiddle)
                }
            }
            else{toastNotYourTurn()}
        }

        buttonTemochiRedSmall!!.setOnClickListener {
            if (turn == 1 && !finished){
                if (movingSource==stringTemochiRedSmall){
                    resetTemochi()
                }
                else if (movingSource== null ||
                    movingSource==stringTemochiRedBig ||
                    movingSource==stringTemochiRedMiddle){
                    pickupTemochi(temochiRedSmall)
                }
            }
            else{toastNotYourTurn()}
        }

        buttonTemochiGreenBig!!.setOnClickListener {
            if (turn == -1 && !finished){
                if (movingSource==stringTemochiGreenBig){
                    resetTemochi()
                }
                else if (movingSource== null ||
                    movingSource==stringTemochiGreenMiddle||
                    movingSource==stringTemochiGreenSmall) {
                    pickupTemochi(temochiGreenBig)
                }
            }
            else{toastNotYourTurn()}
        }

        buttonTemochiGreenMiddle!!.setOnClickListener {
            if (turn == -1 && !finished){
                if (movingSource==stringTemochiGreenMiddle){
                    resetTemochi()
                }
                else if (movingSource== null ||
                    movingSource==stringTemochiGreenBig ||
                    movingSource==stringTemochiGreenSmall){
                    pickupTemochi(temochiGreenMiddle)
                }
            }
            else{toastNotYourTurn()}
        }

        buttonTemochiGreenSmall!!.setOnClickListener {
            if (turn == -1 && !finished){
                if (movingSource==stringTemochiGreenSmall){
                    resetTemochi()
                }
                else if (movingSource==null||
                    movingSource==stringTemochiGreenBig ||
                    movingSource==stringTemochiGreenMiddle){
                    pickupTemochi(temochiGreenSmall)
                }
            }
            else{toastNotYourTurn()}
        }
      ////マスを触ったとき
        buttonA1.setOnClickListener {
            //ゲームが終わったらさわれないようにする,相手のターン中に触れないようにするためにこんなif文を書く
            if (turn != 0 && !finished){ pushedMasButton(A1) } //nameGetterを使ってマスの名前を入れる
            else{toastNotYourTurn()}
        }

        buttonA2.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(A2) }
            else{toastNotYourTurn()}
        }

        buttonA3.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(A3) }
            else{toastNotYourTurn()}
        }

        buttonA4.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(A4) }
            else{toastNotYourTurn()}
        }

        buttonB1.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(B1) }
            else{toastNotYourTurn()}
        }

        buttonB2.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(B2) }
            else{toastNotYourTurn()}
        }

        buttonB3.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(B3) }
            else{toastNotYourTurn()}
        }

        buttonB4.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(B4) }
            else{toastNotYourTurn()}
        }

        buttonC1.setOnClickListener {
            if (turn != 0 && !finished){pushedMasButton(C1) }
            else{toastNotYourTurn()}
        }

        buttonC2.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(C2) }
            else{toastNotYourTurn()}
        }

        buttonC3.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(C3) }
            else{toastNotYourTurn()}
        }

        buttonC4.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(C4) }
            else{toastNotYourTurn()}
        }

        buttonD1.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(D1) }
            else{toastNotYourTurn()}
        }

        buttonD2.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(D2) }
            else{toastNotYourTurn()}
        }

        buttonD3.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(D3) }
            else{toastNotYourTurn()}
        }

        buttonD4.setOnClickListener {
            if (turn != 0 && !finished){ pushedMasButton(D4) }
            else{toastNotYourTurn()}
        }

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