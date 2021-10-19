package com.example.gobblet5

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_game_with_man.*
import java.util.*


class GameWithComActivity : AppCompatActivity() {
    //コンピューター宣言
    val com:Com=Com()
    ////手持ち宣言
    //赤
    private val temochiRedBig = Temochi(3,"TemochiRedBig")
    private val temochiRedMiddle = Temochi(2,"TemochiRedMiddle")
    private val temochiRedSmall = Temochi(1,"TemochiRedSmall")
    //緑
    private val temochiGreenBig = Temochi(3,"TemochiGreenBig")
    private val temochiGreenMiddle = Temochi(2,"TemochiGreenMiddle")
    private val temochiGreenSmall = Temochi(1,"TemochiGreenSmall")

    //マス宣言
    private val A1 = Mas("A1")
    private val B1 = Mas("B1")
    private val C1 = Mas("C1")
    private val D1 = Mas("D1")
    private val A2 = Mas("A2")
    private val B2 = Mas("B2")
    private val C2 = Mas("C2")
    private val D2 = Mas("D2")
    private val A3 = Mas("A3")
    private val B3 = Mas("B3")
    private val C3 = Mas("C3")
    private val D3 = Mas("D3")
    private val A4 = Mas("A4")
    private val B4 = Mas("B4")
    private val C4 = Mas("C4")
    private val D4 = Mas("D4")

    ////文字列
    //手持ち
    private val stringTemochiRedBig=temochiRedBig.nameGetter()
    private val stringTemochiRedMiddle=temochiRedMiddle.nameGetter()
    private val stringTemochiRedSmall=temochiRedSmall.nameGetter()
    private val stringTemochiGreenBig=temochiGreenBig.nameGetter()
    private val stringTemochiGreenMiddle=temochiGreenMiddle.nameGetter()
    private val stringTemochiGreenSmall=temochiGreenSmall.nameGetter()
    //マス
    private val stringA1=A1.nameGetter()
    private val stringA2=A2.nameGetter()
    private val stringA3=A3.nameGetter()
    private val stringA4=A4.nameGetter()
    private val stringB1=B1.nameGetter()
    private val stringB2=B2.nameGetter()
    private val stringB3=B3.nameGetter()
    private val stringB4=B4.nameGetter()
    private val stringC1=C1.nameGetter()
    private val stringC2=C2.nameGetter()
    private val stringC3=C3.nameGetter()
    private val stringC4=C4.nameGetter()
    private val stringD1=D1.nameGetter()
    private val stringD2=D2.nameGetter()
    private val stringD3=D3.nameGetter()
    private val stringD4=D4.nameGetter()

    //popup
    private var configPopup:PopupWindow?=null
    private var resultPopup:PopupWindow?=null

    //mp
    private var mediaPlayer: MediaPlayer?=null
    private var bgmLooping = false

    //soundPool
    private var sp: SoundPool? = null
    private var putSE=0
    private var selectSE = 0
    private var cancelSE = 0
    private var menuSelectSE = 0
    private var cannotDoItSE = 0
    private var gameStartSE = 0
    private var radioButtonSE = 0
    private var openSE = 0
    private var closeSE = 0
    //ゲームに必要なもの
    private var turn = 0 //後でちゃんと設定する
    private var size = 0
    private var winner : String?=null
    private var movingSource : String? = null
    private var destination : String? = null
    private var finished = false
    private var pickupDone= false
    private var insetDone = false

    //ライン デバック用
    private var line1 = mutableListOf(0, 0, 0, 0)
    private var line2 = mutableListOf(0, 0, 0, 0)
    private var line3 = mutableListOf(0, 0, 0, 0)
    private var line4 = mutableListOf(0, 0, 0, 0)

    //勝敗を決めるのに使う
    private var judgeMap = mutableMapOf(
        "lineA" to 0, "lineB" to 0, "lineC" to 0, "lineD" to 0,
        "line1" to 0, "line2" to 0, "line3" to 0, "line4" to 0,
        "lineS" to 0, "lineBS" to 0
    )

    //表示に使う物　(箱を用意している状態)
    private var res: Resources? = null
    private var view: ImageView? = null
    //マス
    private var masImag: Drawable? = null
    //駒
    //赤
    private var komaRedBigD: Drawable? = null
    private var komaRedMiddleD: Drawable? = null
    private var komaRedSmallD: Drawable? = null
    //緑
    private var komaGreenBigD: Drawable? = null
    private var komaGreenMiddleD: Drawable? = null
    private var komaGreenSmallD: Drawable? = null

    //共有プリファレンス
    private var pref: SharedPreferences? =null
    private var SE =false
    private var BGM =false
    private var playFirst=1
    //画面の大きさ
    private var width = 0
    private var height = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_with_man)

        //初期化
        iniFullscreen()
        iniPreference()
        iniSoundPool()
        iniMediaPlayer()
        iniDrawable()
        iniCom()


        //先攻後攻設定
        if (playFirst != 0){ turn = playFirst }
        else {
            when((1..2).random()){
                1 -> {turn = 1}
                2 -> {turn = -1}
            }
        }
        startTurn()
        Log.d("gobblet2", "pF:${playFirst}")





//手持ちのボタンを触った時
        buttonTemochiRedBig.setOnClickListener {
            if (turn == 1){
                //movingSourceが同じときやり直しができる
                if (movingSource == stringTemochiRedBig){
                    resetTemochi()
                }
                //移動元が手持ちの場合のみコマを
                else if (movingSource== null ||
                    movingSource==stringTemochiRedMiddle||
                    movingSource==stringTemochiRedSmall){
                    pickupTemochi(stringTemochiRedBig)
                }
            }
            else{toastNotYourTurn()}
        }

        buttonTemochiRedMiddle.setOnClickListener {
            if (turn == 1){
                if (movingSource==stringTemochiRedMiddle){
                    resetTemochi()
                }
                else if (movingSource== null ||
                    movingSource==stringTemochiRedBig ||
                    movingSource==stringTemochiRedSmall){
                    pickupTemochi(stringTemochiRedMiddle)
                }
            }
            else{toastNotYourTurn()}
        }

        buttonTemochiRedSmall.setOnClickListener {
            if (turn == 1){
                if (movingSource==stringTemochiRedSmall){
                    resetTemochi()
                }
                else if (movingSource== null ||
                    movingSource==stringTemochiRedBig ||
                    movingSource==stringTemochiRedMiddle){
                    pickupTemochi(stringTemochiRedSmall)
                }
            }
            else{toastNotYourTurn()}
        }

        buttonTemochiGreenBig.setOnClickListener {
            if (turn == -1){
                if (movingSource==stringTemochiGreenBig){
                    resetTemochi()
                }
                else if (movingSource== null ||
                    movingSource==stringTemochiGreenMiddle||
                    movingSource==stringTemochiGreenSmall) {
                    pickupTemochi(stringTemochiGreenBig)
                }
            }
            else{toastNotYourTurn()}
        }

        buttonTemochiGreenMiddle.setOnClickListener {
            if (turn == -1){
                if (movingSource==stringTemochiGreenMiddle){
                    resetTemochi()
                }
                else if (movingSource== null ||
                    movingSource==stringTemochiGreenBig ||
                    movingSource==stringTemochiGreenSmall){
                    pickupTemochi(stringTemochiGreenMiddle)
                }
            }
            else{toastNotYourTurn()}
        }

        buttonTemochiGreenSmall.setOnClickListener {
            if (turn == -1){
                if (movingSource==stringTemochiGreenSmall){
                    resetTemochi()
                }
                else if (movingSource==null||
                    movingSource==stringTemochiGreenBig ||
                    movingSource==stringTemochiGreenMiddle){
                    pickupTemochi(stringTemochiGreenSmall)
                }
            }
            else{toastNotYourTurn()}
        }
        ////マスを触ったとき
        buttonA1.setOnClickListener {
            //ゲームが終わったらさわれないようにする,相手のターン中に触れないようにするためにこんなif文を書く
            if (turn != 0){ pushedMasButton(A1) } //nameGetterを使ってマスの名前を入れる
            else{toastNotYourTurn()}
        }

        buttonA2.setOnClickListener {
            if (turn != 0){ pushedMasButton(A2) }
            else{toastNotYourTurn()}
        }

        buttonA3.setOnClickListener {
            if (turn != 0){ pushedMasButton(A3) }
            else{toastNotYourTurn()}
        }

        buttonA4.setOnClickListener {
            if (turn != 0){ pushedMasButton(A4) }
            else{toastNotYourTurn()}
        }

        buttonB1.setOnClickListener {
            if (turn != 0){ pushedMasButton(B1) }
            else{toastNotYourTurn()}
        }

        buttonB2.setOnClickListener {
            if (turn != 0){ pushedMasButton(B2) }
            else{toastNotYourTurn()}
        }

        buttonB3.setOnClickListener {
            if (turn != 0){ pushedMasButton(B3) }
            else{toastNotYourTurn()}
        }

        buttonB4.setOnClickListener {
            if (turn != 0){ pushedMasButton(B4) }
            else{toastNotYourTurn()}
        }

        buttonC1.setOnClickListener {
            if (turn != 0){pushedMasButton(C1) }
            else{toastNotYourTurn()}
        }

        buttonC2.setOnClickListener {
            if (turn != 0){ pushedMasButton(C2) }
            else{toastNotYourTurn()}
        }

        buttonC3.setOnClickListener {
            if (turn != 0){ pushedMasButton(C3) }
            else{toastNotYourTurn()}
        }

        buttonC4.setOnClickListener {
            if (turn != 0){ pushedMasButton(C4) }
            else{toastNotYourTurn()}
        }

        buttonD1.setOnClickListener {
            if (turn != 0){ pushedMasButton(D1) }
            else{toastNotYourTurn()}
        }

        buttonD2.setOnClickListener {
            if (turn != 0){ pushedMasButton(D2) }
            else{toastNotYourTurn()}
        }

        buttonD3.setOnClickListener {
            if (turn != 0){ pushedMasButton(D3) }
            else{toastNotYourTurn()}
        }

        buttonD4.setOnClickListener {
            if (turn != 0){ pushedMasButton(D4) }
            else{toastNotYourTurn()}
        }

        // その他
        configButton.setOnClickListener {
            playSound(openSE)
            showConfigPopup()
        }

        resaltButton.setOnClickListener {
            playSound(openSE)
            showResultPopup()
        }

    }

    ////持ちての表示に関する関数
    //持ちてのコマを表示
    private fun havingDisplay(){
        playSound(selectSE)
        Log.d("gobblet2", "havingDisplay  size=${size},turn=${turn}")
        if (turn == 1){
            view = findViewById(R.id.having1p)
            when (size){
                3 -> { view?.setImageDrawable(komaRedBigD) }
                2 -> { view?.setImageDrawable(komaRedMiddleD) }
                1 -> { view?.setImageDrawable(komaRedSmallD) }
            }
        } else if (turn ==-1){
            view = findViewById(R.id.having2p)
            when (size){
                3 -> { view?.setImageDrawable(komaGreenBigD) }
                2 -> { view?.setImageDrawable(komaGreenMiddleD) }
                1 -> { view?.setImageDrawable(komaGreenSmallD) }
            }
        }
    }
    //持ちてのコマをなにも持ってない状態にもどす
    private fun resetHavingDisplay(){
        when(turn){
            1 -> {
                view = findViewById(R.id.having1p)
                view?.setImageDrawable(masImag)
            }
            -1 -> {
                view = findViewById(R.id.having2p)
                view?.setImageDrawable(masImag)
            }
        }
    }

    //各マスの描写に関する関数
    private fun bordDisplay(location: String?) {
        var box = mutableListOf(0, 0)
        //大きさを判断
        fun redSet(s: Int){
            when (s) {
                3 -> { view?.setImageDrawable(komaRedBigD) }
                2 -> { view?.setImageDrawable(komaRedMiddleD) }
                1 -> { view?.setImageDrawable(komaRedSmallD) }
            }
        }

        fun greenSet(s: Int){
            when (s) {
                3 -> { view?.setImageDrawable(komaGreenBigD) }
                2 -> { view?.setImageDrawable(komaGreenMiddleD) }
                1 -> { view?.setImageDrawable(komaGreenSmallD) }
            }
        }

        fun empSet(){ view?.setImageDrawable(masImag) }

        //場所を判断
        when (location) {
            stringA1 -> {
                view = findViewById(R.id.buttonA1)
                box = A1.funcForDisplay()
            }
            stringA2 -> {
                view = findViewById(R.id.buttonA2)
                box = A2.funcForDisplay()
            }
            stringA3 -> {
                view = findViewById(R.id.buttonA3)
                box = A3.funcForDisplay()
            }
            stringA4 -> {
                view = findViewById(R.id.buttonA4)
                box = A4.funcForDisplay()
            }
            stringB1 -> {
                view = findViewById(R.id.buttonB1)
                box = B1.funcForDisplay()
            }
            stringB2 -> {
                view = findViewById(R.id.buttonB2)
                box = B2.funcForDisplay()
            }
            stringB3 -> {
                view = findViewById(R.id.buttonB3)
                box = B3.funcForDisplay()
            }
            stringB4 -> {
                view = findViewById(R.id.buttonB4)
                box = B4.funcForDisplay()
            }
            stringC1 -> {
                view = findViewById(R.id.buttonC1)
                box = C1.funcForDisplay()
            }
            stringC2 -> {
                view = findViewById(R.id.buttonC2)
                box = C2.funcForDisplay()
            }
            stringC3 -> {
                view = findViewById(R.id.buttonC3)
                box = C3.funcForDisplay()
            }
            stringC4 -> {
                view = findViewById(R.id.buttonC4)
                box = C4.funcForDisplay()
            }
            stringD1 -> {
                view = findViewById(R.id.buttonD1)
                box = D1.funcForDisplay()
            }
            stringD2 -> {
                view = findViewById(R.id.buttonD2)
                box = D2.funcForDisplay()
            }
            stringD3 -> {
                view = findViewById(R.id.buttonD3)
                box = D3.funcForDisplay()
            }
            stringD4 -> {
                view = findViewById(R.id.buttonD4)
                box = D4.funcForDisplay()
            }
        }
        //色を判断
        when (box[1]) {
            1 -> { redSet(box[0]) }
            0 -> { empSet() }
            -1 -> { greenSet(box[0]) }
        }
    }

    //手持ちボタンを押した時の作業1
    private fun pickupTemochi(name: String){
        fun commonFunc(temochi: Temochi){
            setSMP(temochi.returnInf(), temochi.nameGetter())
            havingDisplay()
            debSMP()
        }
        when(name){
            stringTemochiRedBig -> { commonFunc(temochiRedBig) }
            stringTemochiRedMiddle -> { commonFunc(temochiRedMiddle) }
            stringTemochiRedSmall -> { commonFunc(temochiRedSmall) }
            stringTemochiGreenBig -> { commonFunc(temochiGreenBig) }
            stringTemochiGreenMiddle -> { commonFunc(temochiGreenMiddle) }
            stringTemochiGreenSmall -> { commonFunc(temochiGreenSmall) }
        }
    }

    ////マスのボタンをおした時の作業
    //一旦ここを通して分岐
    private fun pushedMasButton(mas: Mas){
        //取り出し作業
        if (!pickupDone) {
            pickup(mas.nameGetter())
            return
        }
        //マスの中に入れる
        val returnValue =  insert(mas.nameGetter())
        if(pickupDone && movingSource == mas.nameGetter()){
            resetMas(mas.nameGetter()) //考え直し
//            Log.d("gobblet2", "thinkAgain")
        } else if(!returnValue) {
            //ルール上指定した場所にコマを入れられなかった場合
            //特に何もしなくて良い
//            Log.d("gobblet2", "youCan'tPutHere")
        }
        else{
            bordDisplay(destination)//コマの移動先を再描画
            endTurn() //ターン終了作業に移る
        }
    }

    //コマを取り出す
    private fun pickup(name: String){
        fun commonFunc(mas: Mas){
            setSMP(mas.mPickup(turn), mas.nameGetter())
            debSMP()
            havingDisplay()
            mas.resetList(size)
            bordDisplay(mas.nameGetter())
            judge()//ここでしたが相手のコマで一列そろってしまったときは相手のかちにする
        }

        when(name){
            stringA1 -> {
                if (A1.mPickup(turn) != 0) {
                    commonFunc(A1)
                } else {
                    toastCanNotPickup()
                }//取り出せるものが無い時の動き
            }
            stringA2 -> {
                if (A2.mPickup(turn) != 0) {
                    commonFunc(A2)
                } else {
                    toastCanNotPickup()
                }
            }
            stringA3 -> {
                if (A3.mPickup(turn) != 0) {
                    commonFunc(A3)
                } else {
                    toastCanNotPickup()
                }
            }
            stringA4 -> {
                if (A4.mPickup(turn) != 0) {
                    commonFunc(A4)
                } else {
                    toastCanNotPickup()
                }
            }
            stringB1 -> {
                if (B1.mPickup(turn) != 0) {
                    commonFunc(B1)
                } else {
                    toastCanNotPickup()
                }
            }
            stringB2 -> {
                if (B2.mPickup(turn) != 0) {
                    commonFunc(B2)
                } else {
                    toastCanNotPickup()
                }
            }
            stringB3 -> {
                if (B3.mPickup(turn) != 0) {
                    commonFunc(B3)
                } else {
                    toastCanNotPickup()
                }
            }
            stringB4 -> {
                if (B4.mPickup(turn) != 0) {
                    commonFunc(B4)
                } else {
                    toastCanNotPickup()
                }
            }
            stringC1 -> {
                if (C1.mPickup(turn) != 0) {
                    commonFunc(C1)
                } else {
                    toastCanNotPickup()
                }
            }
            stringC2 -> {
                if (C2.mPickup(turn) != 0) {
                    commonFunc(C2)
                } else {
                    toastCanNotPickup()
                }
            }
            stringC3 -> {
                if (C3.mPickup(turn) != 0) {
                    commonFunc(C3)
                } else {
                    toastCanNotPickup()
                }
            }
            stringC4 -> {
                if (C4.mPickup(turn) != 0) {
                    commonFunc(C4)
                } else {
                    toastCanNotPickup()
                }
            }
            stringD1 -> {
                if (D1.mPickup(turn) != 0) {
                    commonFunc(D1)
                } else {
                    toastCanNotPickup()
                }
            }
            stringD2 -> {
                if (D2.mPickup(turn) != 0) {
                    commonFunc(D2)
                } else {
                    toastCanNotPickup()
                }
            }
            stringD3 -> {
                if (D3.mPickup(turn) != 0) {
                    commonFunc(D3)
                } else {
                    toastCanNotPickup()
                }
            }
            stringD4 -> {
                if (D4.mPickup(turn) != 0) {
                    commonFunc(D4)
                } else {
                    toastCanNotPickup()
                }
            }
        }
    }

    //駒を入れる
    private fun insert(name: String):Boolean{
        when(name){
            stringA1 -> {
                if (A1.mInsert(size, turn)) {
                    setD(stringA1)
                    return true
                }
            }
            stringA2 -> {
                if (A2.mInsert(size, turn)) {
                    setD(stringA2)
                    return true
                }
            }
            stringA3 -> {
                if (A3.mInsert(size, turn)) {
                    setD(stringA3)
                    return true
                }
            }
            stringA4 -> {
                if (A4.mInsert(size, turn)) {
                    setD(stringA4)
                    return true
                }
            }
            stringB1 -> {
                if (B1.mInsert(size, turn)) {
                    setD(stringB1)
                    return true
                }
            }
            stringB2 -> {
                if (B2.mInsert(size, turn)) {
                    setD(stringB2)
                    return true
                }
            }
            stringB3 -> {
                if (B3.mInsert(size, turn)) {
                    setD(stringB3)
                    return true
                }
            }
            stringB4 -> {
                if (B4.mInsert(size, turn)) {
                    setD(stringB4)
                    return true
                }
            }
            stringC1 -> {
                if (C1.mInsert(size, turn)) {
                    setD(stringC1)
                    return true
                }
            }
            stringC2 -> {
                if (C2.mInsert(size, turn)) {
                    setD(stringC2)
                    return true
                }
            }
            stringC3 -> {
                if (C3.mInsert(size, turn)) {
                    setD(stringC3)
                    return true
                }
            }
            stringC4 -> {
                if (C4.mInsert(size, turn)) {
                    setD(stringC4)
                    return true
                }
            }
            stringD1 -> {
                if (D1.mInsert(size, turn)) {
                    setD(stringD1)
                    return true
                }
            }
            stringD2 -> {
                if (D2.mInsert(size, turn)) {
                    setD(stringD2)
                    return true
                }
            }
            stringD3 -> {
                if (D3.mInsert(size, turn)) {
                    setD(stringD3)
                    return true
                }
            }
            stringD4 -> {
                if (D4.mInsert(size, turn)) {
                    setD(stringD4)
                    return true
                }
            }
        }
        toastCanNotInsert()//トースト表示でおけないことを知らせる
        return false
    }

    //手持ちやりなおし
    private fun resetTemochi(){
        resetSMP()
        resetHavingDisplay()
        debSMP()
    }

    //マスやり直し
    private fun resetMas(masName:String){
        bordDisplay(masName)
        resetSMP()
        resetHavingDisplay()
        debJudge()

    }

    //ターン開始の処理
    private fun startTurn(){
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

    //ターン終了時の処理に関すること
    private fun endTurn() {
        resetForEnd()
        resetHavingDisplay()
        judge()
        resetSMP()
        resetD()
        turn*=-1
        Log.d("gobblet2", "turnEnd")
        startTurn()
    }

    //移動元が手持ちだったときのリセットしょり?
    private fun resetForEnd() {
        when (movingSource) {//移動元を正しく表示する
            stringTemochiRedBig -> {
                temochiRedBig.usePiece()
                textTemochiRedBig.text = "${temochiRedBig.returnCount()}"
                if (temochiRedBig.returnInf() == 0) {
                    buttonTemochiRedBig.visibility = View.INVISIBLE
                    textTemochiRedBig.visibility = View.INVISIBLE
                }
            }
            stringTemochiRedMiddle -> {
                temochiRedMiddle.usePiece()
                textTemochiRedMiddle.text = "${temochiRedMiddle.returnCount()}"
                if (temochiRedMiddle.returnInf() == 0) {
                    buttonTemochiRedMiddle.visibility = View.INVISIBLE
                    textTemochiRedMiddle.visibility = View.INVISIBLE
                }
            }
            stringTemochiRedSmall -> {
                temochiRedSmall.usePiece()
                textTemochiRedSmall.text = "${temochiRedSmall.returnCount()}"
                if (temochiRedSmall.returnInf() == 0) {
                    buttonTemochiRedSmall.visibility = View.INVISIBLE
                    textTemochiRedSmall.visibility = View.INVISIBLE
                }
            }
            stringTemochiGreenBig -> {
                temochiGreenBig.usePiece()
                textTemochiGreenBig.text = "${temochiGreenBig.returnCount()}"
                if (temochiGreenBig.returnInf() == 0) {
                    buttonTemochiGreenBig.visibility = View.INVISIBLE
                    textTemochiGreenBig.visibility = View.INVISIBLE
                }
            }
            stringTemochiGreenMiddle -> {
                temochiGreenMiddle.usePiece()
                textTemochiGreenMiddle.text = "${temochiGreenMiddle.returnCount()}"
                if (temochiGreenMiddle.returnInf() == 0) {
                    buttonTemochiGreenMiddle.visibility = View.INVISIBLE
                    textTemochiGreenMiddle.visibility = View.INVISIBLE
                }
            }
            stringTemochiGreenSmall -> {
                temochiGreenSmall.usePiece()
                textTemochiGreenSmall.text = "${temochiGreenSmall.returnCount()}"
                if (temochiGreenSmall.returnInf() == 0) {
                    buttonTemochiGreenSmall.visibility = View.INVISIBLE
                    textTemochiGreenSmall.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun debSMP(){
        Log.d("gobblet2", "")
        Log.d("gobblet2", "size:${size}, movingSource:${movingSource}, pickupDone:${pickupDone}")
    }

    private fun setSMP(s: Int, m: String){
        size=s
        movingSource=m
        pickupDone=true
    }

    private fun resetSMP(){
        size=0
        movingSource=null
        pickupDone=false
    }

    private fun setD(location: String) {
        playSound(putSE)
        destination = location
        Log.d("gobblet2", "destination:${destination}")
    }

    private fun resetD(){
        destination = null
        Log.d("gobblet2", "destination:${destination}")
    }

    private fun judge(){
        judgeMap["lineA"]=A1.returnLastElement()+A2.returnLastElement()+A3.returnLastElement()+A4.returnLastElement()
        judgeMap["lineB"]=B1.returnLastElement()+B2.returnLastElement()+B3.returnLastElement()+B4.returnLastElement()
        judgeMap["lineC"]=C1.returnLastElement()+C2.returnLastElement()+C3.returnLastElement()+C4.returnLastElement()
        judgeMap["lineD"]=D1.returnLastElement()+D2.returnLastElement()+D3.returnLastElement()+D4.returnLastElement()
        judgeMap["line1"]=A1.returnLastElement()+B1.returnLastElement()+C1.returnLastElement()+D1.returnLastElement()
        judgeMap["line2"]=A2.returnLastElement()+B2.returnLastElement()+C2.returnLastElement()+D2.returnLastElement()
        judgeMap["line3"]=A3.returnLastElement()+B3.returnLastElement()+C3.returnLastElement()+D3.returnLastElement()
        judgeMap["line4"]=A4.returnLastElement()+B4.returnLastElement()+C4.returnLastElement()+D4.returnLastElement()
        judgeMap["lineS"]=A4.returnLastElement()+B3.returnLastElement()+C2.returnLastElement()+D1.returnLastElement()
        judgeMap["lineBS"]=A1.returnLastElement()+B2.returnLastElement()+C3.returnLastElement()+D4.returnLastElement()

        if (judgeMap.containsValue(4)) {
            finished=true
            winner="1p"
            //mapの中に4がある(1pが揃ったことを表す)場合ゲーム終了
        }

        if (judgeMap.containsValue(-4)){
            finished=true
            winner="2p"
            //mapの中に-4がある(2pが揃ったことを表す)場合ゲーム終了
        }

        if (finished){
            resaltButton.visibility=View.VISIBLE
            showResultPopup()
        }

        debJudge()
    }

    private fun debJudge(){
        Log.d("gobblet2", "")

        line1[0]=A1.returnLastElement()
        line1[1]=B1.returnLastElement()
        line1[2]=C1.returnLastElement()
        line1[3]=D1.returnLastElement()
        line2[0]=A2.returnLastElement()
        line2[1]=B2.returnLastElement()
        line2[2]=C2.returnLastElement()
        line2[3]=D2.returnLastElement()
        line3[0]=A3.returnLastElement()
        line3[1]=B3.returnLastElement()
        line3[2]=C3.returnLastElement()
        line3[3]=D3.returnLastElement()
        line4[0]=A4.returnLastElement()
        line4[1]=B4.returnLastElement()
        line4[2]=C4.returnLastElement()
        line4[3]=D4.returnLastElement()

        Log.d("gobblet2", "")
        Log.d("gobblet2", "line1:${line1}")
        Log.d("gobblet2", "line2:${line2}")
        Log.d("gobblet2", "line3:${line3}")
        Log.d("gobblet2", "line4:${line4}")
        Log.d("gobblet2", "finished:${finished},winner:${winner}")
    }

    //コンピューター関係
    fun iniCom(){
        //コンピューターにわたすよう?

        com.iniLines(
            mutableListOf<Mas>(A1, B1, C1, D1),//l1
            mutableListOf<Mas>(A2, B2, C2, D2),//l2
            mutableListOf<Mas>(A3, B3, C3, D3),//l3
            mutableListOf<Mas>(A4, B4, C4, D4),//l4
            mutableListOf<Mas>(A1, A2, A3, A4),//lA
            mutableListOf<Mas>(B1, B2, B3, B4),//lB
            mutableListOf<Mas>(C1, C2, C3, C4),//lC
            mutableListOf<Mas>(D1, D2, D3, D4),//lD
            mutableListOf<Mas>(A1, B2, C3, D4),//lS
            mutableListOf<Mas>(A4, B3, C2, D1),//lBS
        )
        com.iniConcatLine()
    }

    fun startCom(){
        //com.debBord()
        com.checkEmptyMas()
        com.checkWhatIsInTheMas()
        com.checkEachMas()
        com.reachChecker()
        com.checkCanIcheckmate()
        com.checkCanIBlockCheckmate()
        com.judge()
        com.debScore()
        com.resetScore() //仮でここにおいておく
        com.resetLists()


    }

    ////ポップアップ
    //結果ポップアップ
    @SuppressLint("InflateParams")
    private fun showResultPopup(){
        resultPopup = PopupWindow(this@GameWithComActivity)
        // レイアウト設定
        val popupView: View = layoutInflater.inflate(R.layout.popup_resalt, null)
        resultPopup!!.contentView = popupView
        // タップ時に他のViewでキャッチされないための設定
        resultPopup!!.isOutsideTouchable = true
        resultPopup!!.isFocusable = true

        // 表示サイズの設定
        resultPopup!!.width  = width*8/10
        resultPopup!!.height = height*8/10

        // 画面中央に表示
        resultPopup!!.showAtLocation(findViewById(R.id.configButton), Gravity.CENTER, 0, 0)

        //// 関数設定
        val resultImage = popupView.findViewById<ImageView>(R.id.resaltImage)


        //画像を設定する
        if (Locale.getDefault().equals(Locale.JAPAN)){
            when(winner){
                "1p" -> resultImage.setImageResource(R.drawable.win1p_jp)
                "2p" -> resultImage.setImageResource(R.drawable.win2p_jp)
            }
            Log.d("gobblet2", "lang:jp")
        } else {
            when(winner){
                "1p" -> resultImage.setImageResource(R.drawable.win1p_en)
                "2p" -> resultImage.setImageResource(R.drawable.win2p_en)
            }
            Log.d("gobblet2", "lang:en")
        }


        popupView.findViewById<View>(R.id.BackToTitleButton).setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        popupView.findViewById<View>(R.id.retryButtton).setOnClickListener {
            playSound(gameStartSE)
            val intent = Intent(this, GameWithComActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        popupView.findViewById<View>(R.id.backPrebutton).setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this, preGameWithComActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        popupView.findViewById<View>(R.id.backButton).setOnClickListener {
            if (resultPopup!!.isShowing) {
                playSound(closeSE)
                resultPopup!!.dismiss()
            }
        }
    }

    //設定ポップアップ
    @SuppressLint("InflateParams")
    private fun showConfigPopup(){
        configPopup = PopupWindow(this@GameWithComActivity)
        // レイアウト設定
        val popupView: View = layoutInflater.inflate(R.layout.popup_config, null)
        configPopup!!.contentView = popupView

        // タップ時に他のViewでキャッチされないための設定
        configPopup!!.isOutsideTouchable = true
        configPopup!!.isFocusable = true

        // 表示サイズの設定 今回は画面の半分
        configPopup!!.width  = width*8/10
        configPopup!!.height = height*4/10

        // 画面中央に表示
        configPopup!!.showAtLocation(findViewById(R.id.configButton), Gravity.CENTER, 0, 0)

        //ラジオボタン初期化
        val radioSE = popupView.findViewById<RadioGroup>(R.id.SEOnOff)
        when(SE){
            true -> {popupView.findViewById<RadioButton>(R.id.SEOn).isChecked = true}
            false -> {popupView.findViewById<RadioButton>(R.id.SEOff).isChecked = true}
        }

        val radioBGM = popupView.findViewById<RadioGroup>(R.id.BGMOnOff)
        when(BGM){
            true -> {popupView.findViewById<RadioButton>(R.id.BGMOn).isChecked = true}
            false -> {popupView.findViewById<RadioButton>(R.id.BGMOff).isChecked = true}
        }


        //// 関数設定
        radioSE.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.SEOn->{SE=true}
                R.id.SEOff->{SE=false}
            }
            playSound(radioButtonSE)
            val editor=pref!!.edit()
            editor.putBoolean("SEOnOff",SE).apply()
            //Log.d("gobblet2", "SE=${SE}")
        }

        radioBGM.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.BGMOn->{
                    BGM=true
                    mediaPlayer?.start()
                }
                R.id.BGMOff->{
                    BGM=false
                    mediaPlayer?.pause()
                }
            }
            playSound(radioButtonSE)
            val editor= pref!!.edit()
            editor.putBoolean("BGMOnOff",BGM).apply()
            //Log.d("gobblet2", "BGM=${BGM}")
        }

        popupView.findViewById<View>(R.id.BackToTitleButton).setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        popupView.findViewById<View>(R.id.backButton).setOnClickListener {
            if (configPopup!!.isShowing) {
                playSound(closeSE)
                configPopup!!.dismiss()
            }
        }
    }

    //トースト関係(ホントはダイアログだけど面倒だからそのまま使う)
    private fun toastCanNotPickup(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.cannotPickupDialogText))
            .setNeutralButton(getString(R.string.OkText)) { _, _ -> }
            .show()
        playSound(cannotDoItSE)
    }

    private fun toastCanNotInsert(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.cannotInsertDialogText))
            .setNeutralButton(getString(R.string.OkText)) { _, _ -> }
            .show()
        Log.d("gobblet2", "can't insert called")
        playSound(cannotDoItSE)
    }

    private fun toastNotYourTurn(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.notYourTurnDialogText))
            .setNeutralButton(getString(R.string.OkText)) { _, _ -> }
            .show()
        playSound(cannotDoItSE)
    }

    //初期化に関する関数
    private fun iniFullscreen(){
        //画面の大きさ
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(dm)
        width = dm.widthPixels
        height = dm.heightPixels
    }

    private fun iniPreference(){
        //共有プリファレンス
        pref = PreferenceManager.getDefaultSharedPreferences(this@GameWithComActivity)
        SE = pref!!.getBoolean("SEOnOff", true)
        BGM =pref!!.getBoolean("BGMOnOff", true)
        playFirst=pref!!.getInt("playFirst", 1)
    }

    private fun iniSoundPool(){
        //soundPool
        val audioAttributes = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
        } else { TODO("VERSION.SDK_INT < LOLLIPOP") }
        sp = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(1)
            .build()
        //使う効果音を準備
        cannotDoItSE= sp!!.load(this, R.raw.cannotdoit, 1)
        putSE= sp!!.load(this, R.raw.select_se, 1)
        selectSE = sp!!.load(this, R.raw.put, 1)
        cancelSE = sp!!.load(this, R.raw.cancel, 1)
        menuSelectSE = sp!!.load(this, R.raw.menu_selected, 1)
        gameStartSE = sp!!.load(this,R.raw.game_start_se,1)
        openSE = sp!!.load(this,R.raw.open,1)
        closeSE = sp!!.load(this,R.raw.close,1)
    }

    private fun iniMediaPlayer(){
        //mediaPlayer
        mediaPlayer=MediaPlayer.create(applicationContext,R.raw.okashi_time)
        mediaPlayer?.isLooping=true
        if (BGM){
            bgmLooping=true
            mediaPlayer?.start()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun iniDrawable(){
        //表示に使う物(空箱に実物を入れる)
        res=resources
        view=findViewById(R.id.buttonA1) //適当にダミーを入れてるだけ
        //マス
        masImag = res?.getDrawable(R.drawable.mass)
        //駒
        //赤
        komaRedBigD = res?.getDrawable(R.drawable.koma_red_big)
        komaRedMiddleD = res?.getDrawable(R.drawable.koma_red_middle)
        komaRedSmallD = res?.getDrawable(R.drawable.koma_red_small)
        //緑
        komaGreenBigD = res?.getDrawable(R.drawable.koma_green_big)
        komaGreenMiddleD = res?.getDrawable(R.drawable.koma_green_middle)
        komaGreenSmallD = res?.getDrawable(R.drawable.koma_green_small)
    }

    //音を鳴らす処理
    private fun playSound(status: Int){
        if (SE){
            when(status){
                cannotDoItSE -> sp!!.play(cannotDoItSE, 1.0f, 1.0f, 1, 0, 1.5f)
                putSE -> sp!!.play(putSE, 1.0f, 1.0f, 1, 0, 1.0f)
                selectSE -> sp!!.play(selectSE, 1.0f, 1.0f, 1, 0, 1.0f)
                cancelSE -> sp!!.play(cancelSE, 1.0f, 1.0f, 1, 0, 1.0f)
                menuSelectSE -> sp!!.play(menuSelectSE, 1.0f, 1.0f, 1, 0, 1.0f)
                gameStartSE -> sp!!.play(gameStartSE, 1.0f, 1.0f, 1, 0, 1.0f)
                radioButtonSE -> sp!!.play(radioButtonSE,1.0f,1.0f,1,0,1.0f)
                openSE -> sp!!.play(openSE,1.0f,1.0f,1,0,1.0f)
                closeSE -> sp!!.play(closeSE,1.0f,1.0f,1,0,1.0f)
            }
        }
    }

    //    全画面表示に関すること
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
    //ライフサイクル
    override fun onResume() {
        super.onResume()
        if (BGM) {
            mediaPlayer?.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (BGM){
            mediaPlayer?.pause()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        sp!!.release()
        mediaPlayer?.release()
        mediaPlayer=null
    }
}