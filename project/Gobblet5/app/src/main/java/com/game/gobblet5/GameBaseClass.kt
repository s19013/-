package com.game.gobblet5

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
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
    protected val activityIdGameWithCom = 1
    protected val activityIdGameWithMan = 2
    private val activityIdPreGameWithCom = 3
    private val activityIdPreGameWithMan = 4
    private val activityIdMain = 5

    //マジックナンバー防止
    private val p1Piece =  1
    private val p2Piece = -1
    private val empty = 0

    private val bigPiece   = 3
    private val middlePiece= 2
    private val smallPiece = 1

    //タイマー関係
    protected val millisecond:Long=200
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
    
    //文字列
    protected val stringTemochiRedBig = temochiRedBig.nameGetter()
    protected val stringTemochiRedMiddle = temochiRedMiddle.nameGetter()
    protected val stringTemochiRedSmall = temochiRedSmall.nameGetter()
    protected val stringTemochiGreenBig = temochiGreenBig.nameGetter()
    protected val stringTemochiGreenMiddle = temochiGreenMiddle.nameGetter()
    protected val stringTemochiGreenSmall = temochiGreenSmall.nameGetter()

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
    protected var openSE = 0
    private var closeSE = 0
    private var winSE = 0
    private var loosSE = 0
    private var seekSE = 0
    //ゲームに必要なもの
    protected var turn = 0 //後でちゃんと設定する 1p = 1,2p =-1 ,終わり=0
    private var havingPieceSize = 0 //今持っているコマの大きさ
    private var winner : String?=null
    protected var movingSource : Any? = null
    private var destination : Mas? = null
    protected var finished = false
    private var pickupDone= false

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
    private   var seVolume  = 0
    private   var bgmVolume = 0
    protected var playFirst = 1
    //画面の大きさ
    private var width = 0
    private var height = 0

    ////持ちての表示に関する関数
    //持ちてにコマを表示
    private fun havingDisplay(){
        playSound(selectSE)

        if (turn == p1Piece){
            view = findViewById(R.id.having1p)
            when (havingPieceSize){
                bigPiece    -> { view?.setImageDrawable(komaRedBigD) }
                middlePiece -> { view?.setImageDrawable(komaRedMiddleD) }
                smallPiece  -> { view?.setImageDrawable(komaRedSmallD) }
            }
        } else if (turn == p2Piece){
            view = findViewById(R.id.having2p)
            when (havingPieceSize){
                bigPiece    -> { view?.setImageDrawable(komaGreenBigD) }
                middlePiece -> { view?.setImageDrawable(komaGreenMiddleD) }
                smallPiece  -> { view?.setImageDrawable(komaGreenSmallD) }
            }
        }
    }

    //持ちてのコマをなにも持ってない状態にもどす
    private fun resetHavingDisplay(){
        when(turn){
            p1Piece -> {
                view = findViewById(R.id.having1p)
                view?.setImageDrawable(masImag)
            }
            p2Piece -> {
                view = findViewById(R.id.having2p)
                view?.setImageDrawable(masImag)
            }
        }
    }

    //各マスの描写に関する関数
    private fun bordDisplay(location: Mas?) {

        var size  = location?.funcForDisplay()?.get(0) //そのマスの内一番外側のコマの大きさを調べる
        var color = location?.funcForDisplay()?.get(1) //色をしらべる

        //赤いコマを描写
        fun redSet(){
            //大きさを判断
            when (size) {
                bigPiece    -> { view?.setImageDrawable(komaRedBigD) }
                middlePiece -> { view?.setImageDrawable(komaRedMiddleD) }
                smallPiece  -> { view?.setImageDrawable(komaRedSmallD) }
            }
        }

        //緑のコマを描写
        fun greenSet(){
            //大きさを判断
            when (size) {
                bigPiece    -> { view?.setImageDrawable(komaGreenBigD) }
                middlePiece -> { view?.setImageDrawable(komaGreenMiddleD) }
                smallPiece  -> { view?.setImageDrawable(komaGreenSmallD) }
            }
        }

        //空マスを描写
        fun empSet(){ view?.setImageDrawable(masImag) }

        //場所を判断
        fun whereISLocation(){ view = location?.getView() }

        //マスが空がどうか
        fun isMasEmpty():Boolean{
            if (location!!.funcForDisplay()[1] == 0){
                empSet()
                return true
            } else  {return false}
        }

        //マス入っているコマの色を判断
        fun whatISColor(){
            when (color) {
                p1Piece -> { redSet() }
                p2Piece -> { greenSet() }
            }
        }


        whereISLocation()
        if (isMasEmpty()){return}
        whatISColor()
    }

    //手持ちボタンを押した時の作業1
    protected fun pickupTemochi(temochi: Temochi){
        setSMP(temochi.returnInf(), temochi)
        havingDisplay()
    }

    ////マスのボタンをおした時の作業
    //一旦ここを通して分岐
    protected fun pushedMasButton(mas: Mas){
        //ゲームが続いていたら
        if (!finished){
            //取り出し作業
            if (!pickupDone) { return pickup(mas) }

            //マスの中に入れる
            if (pickupDone && insert(mas)) { //ちゃんとマスの中に入った時だけ再描画する
                bordDisplay(destination)//コマの移動先のマスを再描画
                endTurn() //ターン終了作業に移る
            }
        }
        // if(!returnValue) { } //ルール上指定した場所にコマを入れられなかった場合 特に何もしなくて良いわざわざかく必要もない
    }

    //移動元のマスからコマを取り出す
    private fun pickup(mas: Mas){
        //マスの中になにか入っていれば取り出す
        if (mas?.mPickup(turn) != empty) {
            setSMP(mas!!.mPickup(turn), mas) //取り出したコマの情報を保存
            mas?.resetList(havingPieceSize) //
            havingDisplay()
            bordDisplay(mas)
            judge()//ここでしたが相手のコマで一列そろってしまったときは相手のかちにする
            return
        }

        toastCanNotPickup() //取り出せるものが無い時の動き
    }

    //駒を入れる
    private fun insert(mas: Mas?):Boolean{
        Log.d("gobbletdeb","called insert")
//        人間の時はループ?
        if (mas?.mInsert(havingPieceSize, turn) == true){
            setD(mas)
            return true
        }

        toastCanNotInsert()//トースト表示でおけないことを知らせる
        return false
    }

    //手持ちやりなおし
    protected fun resetTemochi(){
        resetSMP()
        resetHavingDisplay()
    }

    //ターン開始の処理
    //オーバーライドする
    open fun startTurn(){}

    //ターン終了時の処理に関すること
    private fun endTurn() {
        if (movingSource is Temochi){ resetForEndTemochi() } //移動元が手持ちだったら手持ちの数をへらす
        resetHavingDisplay()
        judge()
        resetSMP()
        resetD()
        turn*=-1
        startTurn()
    }

    //移動元が手持ちだったときのリセットしょり?
    private fun resetForEndTemochi() {
        //移動元を正しく表示する
        val temochi = movingSource as? Temochi
        temochi?.usePiece()
        temochi?.getTextView()!!.text = "${temochi.returnCount()}"
        //0になったら消す
        if (temochi?.returnInf() == empty) {
            temochi?.getButtonView()!!.visibility = View.INVISIBLE
            temochi?.getTextView()!!.visibility = View.INVISIBLE
        }

    }

    private fun setSMP(s: Int, m: Any?){
        havingPieceSize=s
        movingSource=m
        pickupDone=true

        if (m is Temochi){ Log.d("gobbletdeb","havingPieceSize:${havingPieceSize},movingSource:${m.nameGetter()}") }
        if (m is Mas    ){ Log.d("gobbletdeb","havingPieceSize:${havingPieceSize},movingSource:${m.nameGetter()}") }
    }

    private fun resetSMP(){
        havingPieceSize=empty
        movingSource=null
        pickupDone=false
    }

    private fun setD(location: Mas?) {
        playSound(putSE)
        destination = location

        Log.d("gobbletdeb","destination${destination?.nameGetter()}")
    }

    private fun resetD(){ destination = null }

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
            flashBackground()
            //ジングルを鳴らす
            if (seVolume > 0){
                if (thisAct == activityIdGameWithMan){playSound(winSE)}
                if (thisAct== activityIdGameWithCom){
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
    }

    fun flashBackground(){
        fun commonfunc(list: List<Mas>){
            for (d in list){
                view = d.getView()
                view?.setBackgroundColor(resources.getColor(R.color.lineUP))
            }
        }

        if (judgeMap["lineA"]==4 || judgeMap["lineA"]==-4){ commonfunc(listOf(A1,A2,A3,A4))
            return}
        if (judgeMap["lineB"]==4 || judgeMap["lineB"]==-4){ commonfunc(listOf(B1,B2,B3,B4))
            return}
        if (judgeMap["lineC"]==4 || judgeMap["lineC"]==-4){ commonfunc(listOf(C1,C2,C3,C4))
            return}
        if (judgeMap["lineD"]==4 || judgeMap["lineD"]==-4){ commonfunc(listOf(D1,D2,D3,D4))
            return}
        if (judgeMap["line1"]==4 || judgeMap["line1"]==-4){ commonfunc(listOf(A1,B1,C1,D1))
            return}
        if (judgeMap["line2"]==4 || judgeMap["line2"]==-4){ commonfunc(listOf(A2,B2,C2,D2))
            return}
        if (judgeMap["line3"]==4 || judgeMap["line3"]==-4){ commonfunc(listOf(A3,B3,C3,D3))
            return}
        if (judgeMap["line4"]==4 || judgeMap["line4"]==-4){ commonfunc(listOf(A4,B4,C4,D4))
            return}
        if (judgeMap["lineS"]==4 || judgeMap["lineS"]==-4){ commonfunc(listOf(A4,B3,C2,D1))
            return}
        if (judgeMap["lineBS"]==4 || judgeMap["lineBS"]==-4){ commonfunc(listOf(A1,B2,C3,D4))
            return}
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

        startActivity(intent)
        intent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

    //結果ポップアップ
    @SuppressLint("InflateParams")
    protected fun showResultPopup(){

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
        } else {
            when(winner){
                "1p" -> resultImage.setImageResource(R.drawable.win1p_en)
                "2p" -> resultImage.setImageResource(R.drawable.win2p_en)
            }
        }

        //タイトルへ戻る
        popupView.findViewById<View>(R.id.BackToTitleButton).setOnClickListener {
            playSound(cancelSE)
            changeActivity(activityIdMain)
            resultPopup!!.dismiss()
        }

        //もう一度やる
        popupView.findViewById<View>(R.id.retryButtton).setOnClickListener {
            playSound(gameStartSE)
            changeActivity(thisAct)
            resultPopup!!.dismiss()
        }

        //設定へ
        popupView.findViewById<View>(R.id.backPrebutton).setOnClickListener {
            playSound(cancelSE)
            changeActivity(thisAct)
            resultPopup!!.dismiss()
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

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            popupView.findViewById<LinearLayout>(R.id.SEConfigBox).visibility= View.INVISIBLE
            popupView.findViewById<LinearLayout>(R.id.MusicConfigBox).visibility= View.INVISIBLE
            popupView.findViewById<TextView>(R.id.sorryText).visibility= View.VISIBLE
        }

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
//                    if (recordBgmVolume <= 0 || bgmVolume > 0  ) {playMusic()} // 音楽再開
//                    if (recordBgmVolume > 0 || bgmVolume <= 0  ) {stopMusic()} // 音楽停止
                }
            }
        )


        //タイトルへ戻るボタン
        popupView.findViewById<View>(R.id.BackToTitleButton).setOnClickListener {
            playSound(cancelSE)
            configPopup!!.dismiss()
            changeActivity(activityIdMain)
        }

        //盤面へ戻るボタン
        popupView.findViewById<View>(R.id.backButton).setOnClickListener {
            if (configPopup!!.isShowing) {
                playSound(closeSE)
                configPopup!!.dismiss()
            }
        }

        //やり直し
        popupView.findViewById<View>(R.id.retryButtton).setOnClickListener {
            playSound(gameStartSE)
            configPopup!!.dismiss()
            changeActivity(thisAct)
        }
    }

    //トースト関係(ホントはダイアログだけど面倒だからそのまま使う)
    //取り出せない
    private fun toastCanNotPickup(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.cannotPickupDialogText))
            .setNeutralButton(getString(R.string.OkText)) { _, _ -> }
            .show()
        playSound(cannotDoItSE)
    }

    //入れられない
    private fun toastCanNotInsert(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.cannotInsertDialogText))
            .setNeutralButton(getString(R.string.OkText)) { _, _ -> }
            .show()
        playSound(cannotDoItSE)
    }

    //自分のターンでないのになにかしようとしている
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
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            iniPreference()
            iniSoundPool()
            iniMediaPlayer()
        }
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
        seVolume  =pref!!.getInt("seVolume",5)
        bgmVolume =pref!!.getInt("bgmVolume",5)
        playFirst =pref!!.getInt("playFirst", 1)
    }

    private fun iniSoundPool(){
        //soundPool
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            val audioAttributes =
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            sp = SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(1)
                .build()
        }
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
        seekSE=sp!!.load(this,R.raw.seekbar,1)
    }

    private fun iniMediaPlayer(){
        //mediaPlayer
        mediaPlayer=MediaPlayer.create(applicationContext,R.raw.okashi_time)
        mediaPlayer?.setVolume(bgmVolume*0.1f,bgmVolume*0.1f)
        mediaPlayer?.isLooping=true
        playMusic()
    }

    //先攻後攻設定
    private fun iniWhichIsFirst(){
        if (playFirst != 0){ turn = playFirst }
        else {
            when((1..2).random()){
                1 -> {turn = p1Piece }
                2 -> {turn = p2Piece }
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

        //各マスに描写する場所を教える?
        A1.setView(findViewById(R.id.buttonA1))
        A2.setView(findViewById(R.id.buttonA2))
        A3.setView(findViewById(R.id.buttonA3))
        A4.setView(findViewById(R.id.buttonA4))

        B1.setView(findViewById(R.id.buttonB1))
        B2.setView(findViewById(R.id.buttonB2))
        B3.setView(findViewById(R.id.buttonB3))
        B4.setView(findViewById(R.id.buttonB4))

        C1.setView(findViewById(R.id.buttonC1))
        C2.setView(findViewById(R.id.buttonC2))
        C3.setView(findViewById(R.id.buttonC3))
        C4.setView(findViewById(R.id.buttonC4))

        D1.setView(findViewById(R.id.buttonD1))
        D2.setView(findViewById(R.id.buttonD2))
        D3.setView(findViewById(R.id.buttonD3))
        D4.setView(findViewById(R.id.buttonD4))

        //手持ちのボタンの場所を教える
        temochiGreenBig.setButtonView(findViewById(R.id.buttonTemochiGreenBig))
        temochiGreenMiddle.setButtonView(findViewById(R.id.buttonTemochiGreenMiddle))
        temochiGreenSmall.setButtonView(findViewById(R.id.buttonTemochiGreenSmall))
        temochiRedBig.setButtonView(findViewById(R.id.buttonTemochiRedBig))
        temochiRedMiddle.setButtonView(findViewById(R.id.buttonTemochiRedMiddle))
        temochiRedSmall.setButtonView(findViewById(R.id.buttonTemochiRedSmall))

        //手持ちの文字
        temochiGreenBig.setTextView(findViewById(R.id.textTemochiGreenBig))
        temochiGreenMiddle.setTextView(findViewById(R.id.textTemochiGreenMiddle))
        temochiGreenSmall.setTextView(findViewById(R.id.textTemochiGreenSmall))
        temochiRedBig.setTextView(findViewById(R.id.textTemochiRedBig))
        temochiRedMiddle.setTextView(findViewById(R.id.textTemochiRedMiddle))
        temochiRedSmall.setTextView(findViewById(R.id.textTemochiRedSmall))
    }


    //viewを取得?
    open fun iniView(){}

    //音を鳴らす処理
    protected fun playSound(status: Int){
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
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if (bgmVolume > 0) { mediaPlayer?.start() }
            when (nowDoingTimerID){resultTimerId -> handler.post(resultTimer) }
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if (bgmVolume > 0){ mediaPlayer?.pause() }
            handler.removeCallbacks(resultTimer)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            sp!!.release()
            mediaPlayer?.release()
            handler.removeCallbacks(resultTimer)
        }
    }


}