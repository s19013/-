package com.game.gobblet5

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
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
import java.util.*

open class GameBaseClass : AppCompatActivity() {
    open var thisAct=0 //今のアクティビティ
    val actID=ActivityID()

    //効果音
    val sound = Sound()

    //曲
    private var mediaPlayer: MediaPlayer?=null
    private var bgmLooping = false

    //共有プリファレンス
    val save = preferences()

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
    val p1Temochi = P1Temochi()
    //緑
    val p2Temochi = P2Temochi()
    
    //ボード宣言
    val bord = Bord()

    //popup
    private var configPopup: PopupWindow?=null
    private var resultPopup: PopupWindow?=null

    //ゲームに必要なもの
    protected var turn = 0 //後でちゃんと設定する 1p = 1,2p =-1 ,終わり=0
    private var havingPieceSize = 0 //今持っているコマの大きさ
    private var winner : String?=null
    protected var movingSource : Any? = null //出発元
    private var destination : Mas? = null //行き先
    protected var finished = false
    private var pickupDone= false //取り上げ終わりフラグ
    
    //表示に使う物　(箱を用意している状態)
    private var view: ImageView? = null

    private val gameImages= com.game.gobblet5.GameBaseDrawable()

    //一部ボタン
    protected var resultButton:View?=null

    //画面の大きさ
    private var width  = 0
    private var height = 0

    ////持ちての表示に関する関数
    //持ちてにコマを表示
    private fun havingDisplay(){
        sound.playSound(sound.selectSE,save.seVolume)

        if (turn == p1Piece){
            view = findViewById(R.id.having1p)
            when (havingPieceSize){
                bigPiece    -> { view?.setImageDrawable(gameImages.komaRedBigD) }
                middlePiece -> { view?.setImageDrawable(gameImages.komaRedMiddleD) }
                smallPiece  -> { view?.setImageDrawable(gameImages.komaRedSmallD) }
            }
        } else if (turn == p2Piece){
            view = findViewById(R.id.having2p)
            when (havingPieceSize){
                bigPiece    -> { view?.setImageDrawable(gameImages.komaGreenBigD) }
                middlePiece -> { view?.setImageDrawable(gameImages.komaGreenMiddleD) }
                smallPiece  -> { view?.setImageDrawable(gameImages.komaGreenSmallD) }
            }
        }
    }

    //持ちてのコマをなにも持ってない状態にもどす
    private fun resetHavingDisplay(){
        when(turn){
            p1Piece -> {
                view = findViewById(R.id.having1p)
                view?.setImageDrawable(gameImages.masImag)
            }
            p2Piece -> {
                view = findViewById(R.id.having2p)
                view?.setImageDrawable(gameImages.masImag)
            }
        }
    }

    //各マスの描写に関する関数
    private fun bordDisplay(location: Mas?) {

        val size  = location?.funcForDisplay()?.get(0) //そのマスの内一番外側のコマの大きさを調べる
        val color = location?.funcForDisplay()?.get(1) //色をしらべる

        //赤いコマを描写
        fun redSet(){
            //大きさを判断
            when (size) {
                bigPiece    -> { view?.setImageDrawable(gameImages.komaRedBigD) }
                middlePiece -> { view?.setImageDrawable(gameImages.komaRedMiddleD) }
                smallPiece  -> { view?.setImageDrawable(gameImages.komaRedSmallD) }
            }
        }

        //緑のコマを描写
        fun greenSet(){
            //大きさを判断
            when (size) {
                bigPiece    -> { view?.setImageDrawable(gameImages.komaGreenBigD) }
                middlePiece -> { view?.setImageDrawable(gameImages.komaGreenMiddleD) }
                smallPiece  -> { view?.setImageDrawable(gameImages.komaGreenSmallD) }
            }
        }

        //空マスを描写
        fun empSet(){ view?.setImageDrawable(gameImages.masImag) }

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
        if (mas.mPickup(turn) != empty) {
            setSMP(mas.mPickup(turn), mas) //取り出したコマの情報を保存
            mas.resetList(havingPieceSize) //
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
        if (temochi.returnInf() == empty) {
            temochi.getButtonView()!!.visibility = View.INVISIBLE
            temochi.getTextView()!!.visibility = View.INVISIBLE
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
        sound.playSound(sound.putSE,save.seVolume)
        destination = location

        Log.d("gobbletdeb","destination${destination?.nameGetter()}")
    }

    private fun resetD(){ destination = null }

    private fun judge(){
        fun commonfunc(l:Line){
            finished=true
            turn = 0
            flashBackground(l.listGetter())
            //ジングルを鳴らす
            if (save.seVolume > 0){
                if (thisAct == actID.gameWithMan){sound.playSound(sound.winSE,save.seVolume)}
                if (thisAct== actID.gameWithCom){
                    when(winner){
                        "1p" -> sound.playSound(sound.winSE,save.seVolume)
                        "2p" -> sound.playSound(sound.loosSE,save.seVolume)
                    }
                }
            }
            resultButton!!.visibility= View.VISIBLE
            nowDoingTimerID = resultTimerId
            handler.post(resultTimer)
        }


        for (l in bord.allLine){
            when(l.judge()){
                 4 -> {
                     winner="1p"
                     commonfunc(l)
                     break
                    }
                -4 -> {
                    winner="2p"
                    commonfunc(l)
                    break
                }
            }
        }
    }

    private fun flashBackground(list: List<Mas>){
        for (d in list){
            view = d.getView()
            view?.setBackgroundColor(resources.getColor(R.color.lineUP))
        }
    }

    ////ポップアップ
    //ポップアップが使う画面遷移
    private fun changeActivity(id:Int){
        var intent:Intent?=null
        when(id){
            actID.main-> intent = Intent(this, MainActivity::class.java)
            actID.gameWithMan -> intent = Intent(this, GameWithManActivity::class.java)
            actID.gameWithCom -> intent = Intent(this, GameWithComActivity::class.java)
            actID.preGameWithMan -> intent = Intent(this, preGameWithManActivity::class.java)
            actID.preGameWithCom -> intent = Intent(this, preGameWithComActivity::class.java)
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
            sound.playSound(sound.cancelSE,save.seVolume)
            changeActivity(actID.main)
            resultPopup!!.dismiss()
        }

        //もう一度やる
        popupView.findViewById<View>(R.id.retryButtton).setOnClickListener {
            sound.playSound(sound.gameStartSE,save.seVolume)
            changeActivity(thisAct)
            resultPopup!!.dismiss()
        }

        //設定へ
        popupView.findViewById<View>(R.id.backPrebutton).setOnClickListener {
            sound.playSound(sound.cancelSE,save.seVolume)
            changeActivity(thisAct)
            resultPopup!!.dismiss()
        }

        //盤面を見る
        popupView.findViewById<View>(R.id.backButton).setOnClickListener {
            if (resultPopup!!.isShowing) {
                sound.playSound(sound.closeSE,save.seVolume)
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

        seVolumeText?.text = save.seVolume.toString() //プレファレンスの値をセット
        bgmVolumeText?.text = save.bgmVolume.toString() //プレファレンスの値をセット


        //動かす前の値を記録
        val recordbgmVolume = save.bgmVolume

        //シークバー初期化
        val seSeekBar = popupView.findViewById<SeekBar>(R.id.seSeekBar)
        val bgmSeekBar = popupView.findViewById<SeekBar>(R.id.bgmSeekBar)

        seSeekBar?.progress = save.seVolume //プレファレンスの値を初期値にセット
        seSeekBar?.max = 10

        bgmSeekBar?.progress = save.bgmVolume //プレファレンスの値を初期値にセット
        bgmSeekBar?.max = 10

        seSeekBar?.setOnSeekBarChangeListener(
            object:SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    seVolumeText?.text = progress.toString()
                    save.seVolume=progress
                    sound.playSound(sound.seekSE,save.seVolume)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val editor= save.pref!!.edit()
                    editor.putInt("seVolume",save.seVolume)
                    editor.apply()
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
                    sound.playSound(sound.seekSE,save.seVolume)
                    bgmVolumeText?.text = progress.toString()
                    save.bgmVolume=progress
                    mediaPlayer?.setVolume(save.bgmVolume*0.1f,save.bgmVolume*0.1f)

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val editor= save.pref!!.edit()
                    editor.putInt("bgmVolume",save.bgmVolume)
                    editor.apply()
                }
            }
        )


        //タイトルへ戻るボタン
        popupView.findViewById<View>(R.id.BackToTitleButton).setOnClickListener {
            sound.playSound(sound.cancelSE,save.seVolume)
            configPopup!!.dismiss()
            changeActivity(actID.main)
        }

        //盤面へ戻るボタン
        popupView.findViewById<View>(R.id.backButton).setOnClickListener {
            if (configPopup!!.isShowing) {
                sound.playSound(sound.closeSE,save.seVolume)
                configPopup!!.dismiss()
            }
        }

        //やり直し
        popupView.findViewById<View>(R.id.retryButtton).setOnClickListener {
            sound.playSound(sound.gameStartSE,save.seVolume)
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
        sound.playSound(sound.cannotDoItSE,save.seVolume)
    }

    //入れられない
    private fun toastCanNotInsert(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.cannotInsertDialogText))
            .setNeutralButton(getString(R.string.OkText)) { _, _ -> }
            .show()
        sound.playSound(sound.cannotDoItSE,save.seVolume)
    }

    //自分のターンでないのになにかしようとしている
    protected fun toastNotYourTurn(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.notYourTurnDialogText))
            .setNeutralButton(getString(R.string.OkText)) { _, _ -> }
            .show()
        sound.playSound(sound.cannotDoItSE,save.seVolume)
    }

    //初期化に関する関数

    //標準的な初期化処理
    protected fun iniStandard(){
        bord.iniLines()
        iniFullscreen()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            save.iniPreference(applicationContext)
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

    private fun iniSoundPool(){ sound.iniSoundPool(applicationContext) }

    private fun iniMediaPlayer(){
        //mediaPlayer
        mediaPlayer=MediaPlayer.create(applicationContext,R.raw.okashi_time)
        mediaPlayer?.setVolume(save.bgmVolume*0.1f,save.bgmVolume*0.1f)
        mediaPlayer?.isLooping=true
        playMusic()
    }

    //先攻後攻設定
    private fun iniWhichIsFirst(){
        if (save.playFirst != 0){ turn = save.playFirst }
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
        view=findViewById(R.id.buttonA1) //適当にダミーを入れてるだけ
        gameImages.ini(resources)

        //各マスに描写する場所を教える?
        with(bord) {
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
        }

        //手持ちのボタンの場所を教える
        p1Temochi.big.setButtonView(findViewById(R.id.buttonTemochiRedBig))
        p1Temochi.middle.setButtonView(findViewById(R.id.buttonTemochiRedMiddle))
        p1Temochi.small.setButtonView(findViewById(R.id.buttonTemochiRedSmall))
        p2Temochi.big.setButtonView(findViewById(R.id.buttonTemochiGreenBig))
        p2Temochi.middle.setButtonView(findViewById(R.id.buttonTemochiGreenMiddle))
        p2Temochi.small.setButtonView(findViewById(R.id.buttonTemochiGreenSmall))

        //手持ちの文字
        p1Temochi.big.setTextView(findViewById(R.id.textTemochiRedBig))
        p1Temochi.middle.setTextView(findViewById(R.id.textTemochiRedMiddle))
        p1Temochi.small.setTextView(findViewById(R.id.textTemochiRedSmall))
        p2Temochi.big.setTextView(findViewById(R.id.textTemochiGreenBig))
        p2Temochi.middle.setTextView(findViewById(R.id.textTemochiGreenMiddle))
        p2Temochi.small.setTextView(findViewById(R.id.textTemochiGreenSmall))

        resultButton=findViewById(R.id.resaltButton)
    }


    //viewを取得?
    open fun iniView(){}

    private fun playMusic(){
        if (save.bgmVolume > 0){
            bgmLooping=true
            mediaPlayer?.setVolume(save.bgmVolume*0.1f,save.bgmVolume*0.1f)
            mediaPlayer?.start()
        }
    }

    private fun stopMusic(){ mediaPlayer?.pause() }

    private val resultTimer: Runnable = object : Runnable{
        override fun run() {
            time += millisecond
            when(time){
                200L -> mediaPlayer?.setVolume(save.bgmVolume*0.1f*0.8f,save.bgmVolume*0.1f*0.8f)
                400L -> mediaPlayer?.setVolume(save.bgmVolume*0.1f*0.6f,save.bgmVolume*0.1f*0.6f)
                600L -> mediaPlayer?.setVolume(save.bgmVolume*0.1f*0.4f,save.bgmVolume*0.1f*0.4f)
                800L -> mediaPlayer?.setVolume(save.bgmVolume*0.1f*0.2f,save.bgmVolume*0.1f*0.2f)
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
            if (save.bgmVolume > 0) { mediaPlayer?.start() }
            when (nowDoingTimerID){resultTimerId -> handler.post(resultTimer) }
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if (save.bgmVolume > 0){ mediaPlayer?.pause() }
            handler.removeCallbacks(resultTimer)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            sound.sp!!.release()
            mediaPlayer?.release()
            handler.removeCallbacks(resultTimer)
        }
    }


}