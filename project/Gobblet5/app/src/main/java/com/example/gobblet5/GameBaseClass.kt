package com.example.gobblet5

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import java.util.*

open class GameBaseClass : AppCompatActivity() {
    open var thisAct=0 //今のアクティビティ
    //コンピューター対戦:-1
    //人間対戦:1
    private val activityIdGameWithCom = 1
    private val activityIdGameWithMan = 2
    private val activityIdPreGameWithCom = 3
    private val activityIdPreGameWithMan = 4
    private val activityIdMain = 5

    //タイマー関係
    protected val millisecond:Long=100
    protected var time = 0L
    protected val handler = Handler(Looper.getMainLooper()) // Looper.getMainLooper()を書くとクラッシュしにくいらしい
    protected var nowDoingTimerID:String? = null
    ////タイマーのid
    private val resultTimerId = "resultTimer"

    ////手持ち宣言
    //赤
    protected val temochiRedBig = Temochi(3,"TemochiRedBig")
    protected val temochiRedMiddle = Temochi(2,"TemochiRedMiddle")
    protected val temochiRedSmall = Temochi(1,"TemochiRedSmall")
    //緑
    protected val temochiGreenBig = Temochi(3,"TemochiGreenBig")
    protected val temochiGreenMiddle = Temochi(2,"TemochiGreenMiddle")
    protected val temochiGreenSmall = Temochi(1,"TemochiGreenSmall")

    //マス宣言
    protected val A1 = Mas("A1")
    protected val B1 = Mas("B1")
    protected val C1 = Mas("C1")
    protected val D1 = Mas("D1")
    protected val A2 = Mas("A2")
    protected val B2 = Mas("B2")
    protected val C2 = Mas("C2")
    protected val D2 = Mas("D2")
    protected val A3 = Mas("A3")
    protected val B3 = Mas("B3")
    protected val C3 = Mas("C3")
    protected val D3 = Mas("D3")
    protected val A4 = Mas("A4")
    protected val B4 = Mas("B4")
    protected val C4 = Mas("C4")
    protected val D4 = Mas("D4")

    ////文字列
    //手持ち
    protected val stringTemochiRedBig=temochiRedBig.nameGetter()
    protected val stringTemochiRedMiddle=temochiRedMiddle.nameGetter()
    protected val stringTemochiRedSmall=temochiRedSmall.nameGetter()
    protected val stringTemochiGreenBig=temochiGreenBig.nameGetter()
    protected val stringTemochiGreenMiddle=temochiGreenMiddle.nameGetter()
    protected val stringTemochiGreenSmall=temochiGreenSmall.nameGetter()
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
    private var configPopup: PopupWindow?=null
    private var resultPopup: PopupWindow?=null

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
    private var openSE = 0
    private var closeSE = 0
    private var winSE = 0
    private var loosSE = 0
    private var seekSE = 0
    //ゲームに必要なもの
    protected var turn = 0 //後でちゃんと設定する
    private var size = 0
    private var winner : String?=null
    protected var movingSource : String? = null
    private var destination : String? = null
    protected var finished = false
    private var pickupDone= false

    //ライン デバック用
    private var debLine1 = mutableListOf(0, 0, 0, 0)
    private var debLine2 = mutableListOf(0, 0, 0, 0)
    private var debLine3 = mutableListOf(0, 0, 0, 0)
    private var debLine4 = mutableListOf(0, 0, 0, 0)

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

    //テキスト
    protected var textTemochiRedBig:TextView?=null
    protected var textTemochiRedMiddle:TextView?=null
    protected var textTemochiRedSmall:TextView?=null
    protected var textTemochiGreenBig:TextView?=null
    protected var textTemochiGreenMiddle:TextView?=null
    protected var textTemochiGreenSmall:TextView?=null

    //一部ボタン
    protected var buttonTemochiRedBig:View?=null
    protected var buttonTemochiRedMiddle:View?=null
    protected var buttonTemochiRedSmall:View?=null
    protected var buttonTemochiGreenBig:View?=null
    protected var buttonTemochiGreenMiddle:View?=null
    protected var buttonTemochiGreenSmall:View?=null
    protected var resultButton:View?=null

    //共有プリファレンス
    private var pref: SharedPreferences? =null
    private var seVolume = 0
    private var bgmVolume = 0
    protected var playFirst=1
    //画面の大きさ
    private var width = 0
    private var height = 0

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
    protected fun pickupTemochi(temochi: Temochi){
        setSMP(temochi.returnInf(), temochi.nameGetter())
        havingDisplay()
    }

    ////マスのボタンをおした時の作業
    //一旦ここを通して分岐
    protected fun pushedMasButton(mas: Mas){
        //取り出し作業
        if (!pickupDone) { return pickup(mas.nameGetter()) }
        //マスの中に入れる
        insert(mas.nameGetter())
        if(pickupDone && movingSource == mas.nameGetter()){ resetMas(mas.nameGetter()) } //考え直し
        else{
            bordDisplay(destination)//コマの移動先を再描画
            endTurn() //ターン終了作業に移る
        }

        // if(!returnValue) { } //ルール上指定した場所にコマを入れられなかった場合 特に何もしなくて良いわざわざかく必要もない
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
            stringA1 -> { if (A1.mPickup(turn) != 0) {return commonFunc(A1) } }
            stringA2 -> { if (A2.mPickup(turn) != 0) {return commonFunc(A2) } }
            stringA3 -> { if (A3.mPickup(turn) != 0) {return commonFunc(A3) } }
            stringA4 -> { if (A4.mPickup(turn) != 0) {return commonFunc(A4) } }
            stringB1 -> { if (B1.mPickup(turn) != 0) {return commonFunc(B1) } }
            stringB2 -> { if (B2.mPickup(turn) != 0) {return commonFunc(B2) } }
            stringB3 -> { if (B3.mPickup(turn) != 0) {return commonFunc(B3) } }
            stringB4 -> { if (B4.mPickup(turn) != 0) {return commonFunc(B4) } }
            stringC1 -> { if (C1.mPickup(turn) != 0) {return commonFunc(C1) } }
            stringC2 -> { if (C2.mPickup(turn) != 0) {return commonFunc(C2) } }
            stringC3 -> { if (C3.mPickup(turn) != 0) {return commonFunc(C3) } }
            stringC4 -> { if (C4.mPickup(turn) != 0) {return commonFunc(C4) } }
            stringD1 -> { if (D1.mPickup(turn) != 0) {return commonFunc(D1) } }
            stringD2 -> { if (D2.mPickup(turn) != 0) {return commonFunc(D2) } }
            stringD3 -> { if (D3.mPickup(turn) != 0) {return commonFunc(D3) } }
            stringD4 -> { if (D4.mPickup(turn) != 0) {return commonFunc(D4) } }
        }

        toastCanNotPickup() //取り出せるものが無い時の動き
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
    protected fun resetTemochi(){
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
    //オーバーライドする
    open fun startTurn(){}

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
            stringTemochiRedBig ->
                {
                    temochiRedBig.usePiece()
                    textTemochiRedBig!!.text = "${temochiRedBig.returnCount()}"
                    if (temochiRedBig.returnInf() == 0) {
                        buttonTemochiRedBig!!.visibility = View.INVISIBLE
                        textTemochiRedBig!!.visibility = View.INVISIBLE
                    }
                }
            stringTemochiRedMiddle ->
                {
                    temochiRedMiddle.usePiece()
                    textTemochiRedMiddle!!.text = "${temochiRedMiddle.returnCount()}"
                    if (temochiRedMiddle.returnInf() == 0) {
                        buttonTemochiRedMiddle!!.visibility = View.INVISIBLE
                        textTemochiRedMiddle!!.visibility = View.INVISIBLE
                    }
                }
            stringTemochiRedSmall ->
                {
                    temochiRedSmall.usePiece()
                    textTemochiRedSmall!!.text = "${temochiRedSmall.returnCount()}"
                    if (temochiRedSmall.returnInf() == 0) {
                        buttonTemochiRedSmall!!.visibility = View.INVISIBLE
                        textTemochiRedSmall!!.visibility = View.INVISIBLE
                    }
                }
            stringTemochiGreenBig ->
                {
                    temochiGreenBig.usePiece()
                    textTemochiGreenBig!!.text = "${temochiGreenBig.returnCount()}"
                    if (temochiGreenBig.returnInf() == 0) {
                        buttonTemochiGreenBig!!.visibility = View.INVISIBLE
                        textTemochiGreenBig!!.visibility = View.INVISIBLE
                    }
                }
            stringTemochiGreenMiddle ->
                {
                    temochiGreenMiddle.usePiece()
                    textTemochiGreenMiddle!!.text = "${temochiGreenMiddle.returnCount()}"
                    if (temochiGreenMiddle.returnInf() == 0) {
                        buttonTemochiGreenMiddle!!.visibility = View.INVISIBLE
                        textTemochiGreenMiddle!!.visibility = View.INVISIBLE
                    }
                }
            stringTemochiGreenSmall ->
                {
                    temochiGreenSmall.usePiece()
                    textTemochiGreenSmall!!.text = "${temochiGreenSmall.returnCount()}"
                    if (temochiGreenSmall.returnInf() == 0) {
                        buttonTemochiGreenSmall!!.visibility = View.INVISIBLE
                        textTemochiGreenSmall!!.visibility = View.INVISIBLE
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
            turn = 0
            //ジングルを鳴らす
            if (seVolume > 0){
                if (thisAct==1){playSound(winSE)}
                if (thisAct==-1){
                    when(winner){
                        "1p" -> playSound(winSE)
                        "2p" -> playSound(loosSE)
                    }
                }
            }
            resultButton!!.visibility= View.VISIBLE
            nowDoingTimerID = resultTimerId
            handler.post(resultTimer)
            //showResultPopup()
        }

        debJudge()
    }

    private fun debJudge(){
        Log.d("gobblet2", "")

        debLine1[0]=A1.returnLastElement()
        debLine1[1]=B1.returnLastElement()
        debLine1[2]=C1.returnLastElement()
        debLine1[3]=D1.returnLastElement()
        debLine2[0]=A2.returnLastElement()
        debLine2[1]=B2.returnLastElement()
        debLine2[2]=C2.returnLastElement()
        debLine2[3]=D2.returnLastElement()
        debLine3[0]=A3.returnLastElement()
        debLine3[1]=B3.returnLastElement()
        debLine3[2]=C3.returnLastElement()
        debLine3[3]=D3.returnLastElement()
        debLine4[0]=A4.returnLastElement()
        debLine4[1]=B4.returnLastElement()
        debLine4[2]=C4.returnLastElement()
        debLine4[3]=D4.returnLastElement()

        Log.d("gobblet2", "")
        Log.d("gobblet2", "line1:${debLine1}")
        Log.d("gobblet2", "line2:${debLine2}")
        Log.d("gobblet2", "line3:${debLine3}")
        Log.d("gobblet2", "line4:${debLine4}")
        Log.d("gobblet2", "finished:${finished},winner:${winner}")
    }

    ////ポップアップ
    //ポップアップが使う画面遷移
    private fun changeActivity(id:Int){
        var intent:Intent?=null
        when(id){
            activityIdMain->intent = Intent(this, MainActivity::class.java)
            activityIdGameWithMan -> intent = Intent(this, GameWithManActivity::class.java)
            activityIdGameWithCom -> intent = Intent(this, GameWithComActivity::class.java)
            activityIdPreGameWithMan -> intent = Intent(this, preGameWithManActivity::class.java)
            activityIdPreGameWithCom -> intent = Intent(this, preGameWithComActivity::class.java)
        }

        intent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    //結果ポップアップ
    @SuppressLint("InflateParams")
    protected fun showResultPopup(){
        playSound(openSE)
        resultPopup = PopupWindow(this)
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

        //タイトルへ戻る
        popupView.findViewById<View>(R.id.BackToTitleButton).setOnClickListener {
            playSound(cancelSE)
            changeActivity(activityIdMain)
        }

        //もう一度やる
        popupView.findViewById<View>(R.id.retryButtton).setOnClickListener {
            playSound(gameStartSE)
            when(thisAct){
                -1 -> {changeActivity(activityIdGameWithCom)}
                 1 -> {changeActivity(activityIdGameWithMan)}
            }
        }

        //設定へ
        popupView.findViewById<View>(R.id.backPrebutton).setOnClickListener {
            playSound(cancelSE)
            when(thisAct){
                -1 -> {changeActivity(activityIdPreGameWithCom)}
                 1 -> {changeActivity(activityIdPreGameWithMan)}
            }
        }

        //盤面を見る
        popupView.findViewById<View>(R.id.backButton).setOnClickListener {
            if (resultPopup!!.isShowing) {
                playSound(closeSE)
                resultPopup!!.dismiss()
            }
        }
    }

    //設定ポップアップ
    @SuppressLint("InflateParams")
    protected fun showConfigPopup(){
        playSound(openSE)
        configPopup = PopupWindow(this)
        // レイアウト設定
        val popupView: View = layoutInflater.inflate(R.layout.popup_config, null)
        configPopup!!.contentView = popupView

        // タップ時に他のViewでキャッチされないための設定
        configPopup!!.isOutsideTouchable = true
        configPopup!!.isFocusable = true

        // 表示サイズの設定
        configPopup!!.width  = width  * 8/10
        configPopup!!.height = height * 6/10

        // 画面中央に表示
        configPopup!!.showAtLocation(findViewById(R.id.configButton), Gravity.CENTER, 0, 0)

        //テキスト初期化
        val seVolumeText = popupView.findViewById<TextView>(R.id.seVolume)
        val bgmVolumeText = popupView.findViewById<TextView>(R.id.bgmVolume)

        seVolumeText?.text = seVolume.toString() //プレファレンスの値をセット
        bgmVolumeText?.text = bgmVolume.toString() //プレファレンスの値をセット


        //動かす前の値を記録
        val recordBgmVolume = bgmVolume

        //シークバー初期化
        val seSeekBar = popupView.findViewById<SeekBar>(R.id.seSeekBar)
        val bgmSeekBar = popupView.findViewById<SeekBar>(R.id.bgmSeekBar)

        seSeekBar?.progress = seVolume //プレファレンスの値を初期値にセット
        seSeekBar?.max = 10

        bgmSeekBar?.progress = bgmVolume //プレファレンスの値を初期値にセット
        bgmSeekBar?.max = 10

        seSeekBar?.setOnSeekBarChangeListener(
            object:SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    seVolumeText?.text = progress.toString()
                    seVolume=progress
                    playSound(seekSE)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val editor= pref!!.edit()
                    editor.putInt("seVolume",seVolume).apply()
                }
            }
        )

        bgmSeekBar?.setOnSeekBarChangeListener(
            object:SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    playSound(seekSE)
                    bgmVolumeText?.text = progress.toString()
                    bgmVolume=progress
                    mediaPlayer?.setVolume(bgmVolume*0.1f,bgmVolume*0.1f)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val editor= pref!!.edit()
                    editor.putInt("bgmVolume",bgmVolume).apply()
                    if (recordBgmVolume <= 0 || bgmVolume > 0  ) {playMusic()} // 音楽再開
                    if (recordBgmVolume > 0 || bgmVolume <= 0  ) {stopMusic()} // 音楽停止
                }
            }
        )


        //タイトルへ戻るボタン
        popupView.findViewById<View>(R.id.BackToTitleButton).setOnClickListener {
            playSound(cancelSE)
            changeActivity(activityIdMain)
        }

        //盤面へ戻るボタン
        popupView.findViewById<View>(R.id.backButton).setOnClickListener {
            if (configPopup!!.isShowing) {
                playSound(closeSE)
                configPopup!!.dismiss()
            }
        }


        popupView.findViewById<View>(R.id.retryButtton).setOnClickListener {
            playSound(gameStartSE)
            when(thisAct){
                -1 -> {changeActivity(activityIdGameWithCom)}
                 1 -> {changeActivity(activityIdGameWithMan)}
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

    protected fun toastNotYourTurn(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.notYourTurnDialogText))
            .setNeutralButton(getString(R.string.OkText)) { _, _ -> }
            .show()
        playSound(cannotDoItSE)
    }

    //初期化に関する関数

    //標準的な初期化処理
    protected fun iniStandard(){
        iniFullscreen()
        iniPreference()
        iniSoundPool()
        iniMediaPlayer()
        iniDrawable()
        iniWhichIsFirst()
        iniView()
    }

    private fun iniFullscreen(){
        //画面の大きさ
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(dm)
        width = dm.widthPixels
        height = dm.heightPixels
    }

    private fun iniPreference(){
        //共有プリファレンス
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        seVolume =pref!!.getInt("seVolume",5)
        bgmVolume =pref!!.getInt("bgmVolume",5)
        playFirst=pref!!.getInt("playFirst", 1)
    }

    private fun iniSoundPool(){
        //soundPool
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()
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
        winSE = sp!!.load(this,R.raw.win_single,1)
        loosSE = sp!!.load(this,R.raw.loose_single,1)
        seekSE=sp!!.load(this,R.raw.select_se,1)
    }

    private fun iniMediaPlayer(){
        //mediaPlayer
        mediaPlayer=MediaPlayer.create(applicationContext,R.raw.okashi_time)
        mediaPlayer?.isLooping=true
        playMusic()
    }

    //先攻後攻設定
    private fun iniWhichIsFirst(){
        if (playFirst != 0){ turn = playFirst }
        else {
            when((1..2).random()){
                1 -> {turn = 1}
                2 -> {turn = -1}
            }
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

    //viewを取得?
    open fun iniView(){}

    //音を鳴らす処理
    private fun playSound(status: Int){
        if (seVolume > 0){ sp!!.play(status,seVolume*0.1f,seVolume*0.1f,1,0,1.0f) }
    }

    private fun playMusic(){
        if (bgmVolume > 0){
            bgmLooping=true
            mediaPlayer?.setVolume(bgmVolume*0.1f,bgmVolume*0.1f)
            mediaPlayer?.start()
        }
    }

    private fun stopMusic(){ mediaPlayer?.pause() }

    private val resultTimer: Runnable = object : Runnable{
        override fun run() {
            time += millisecond
            when(time){
                200L -> mediaPlayer?.setVolume(bgmVolume*0.1f*0.8f,bgmVolume*0.1f*0.8f)
                400L -> mediaPlayer?.setVolume(bgmVolume*0.1f*0.6f,bgmVolume*0.1f*0.6f)
                600L -> mediaPlayer?.setVolume(bgmVolume*0.1f*0.4f,bgmVolume*0.1f*0.4f)
                800L -> mediaPlayer?.setVolume(bgmVolume*0.1f*0.2f,bgmVolume*0.1f*0.2f)
            }
            handler.postDelayed(this,millisecond)
            if (time>1000L){
                stopMusic()
                showResultPopup()
                handler.removeCallbacks(this)
                time = 0L
                nowDoingTimerID = null
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

    //ライフサイクル
    override fun onResume() {
        super.onResume()
        if (bgmVolume > 0) { mediaPlayer?.start() }
        when (nowDoingTimerID){
            resultTimerId -> handler.post(resultTimer)
        }
    }

    override fun onPause() {
        super.onPause()
        if (bgmVolume > 0){ mediaPlayer?.pause() }
        handler.removeCallbacks(resultTimer) }

    override fun onDestroy() {
        super.onDestroy()
        sp!!.release()
        mediaPlayer?.release()
        handler.removeCallbacks(resultTimer)
        mediaPlayer=null
    }


}