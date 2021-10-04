package com.example.gobblet5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_game_with_man.*


class GameWithComActivity : AppCompatActivity() {
//mp
    var player: MediaPlayer?=null
    var bgmlooping = false
//popup
    private var configPopup:PopupWindow?=null
    private var resaltPopup:PopupWindow?=null
//soundpool
    private lateinit var sp: SoundPool
    private var putSE=0
    private var selectSE = 0
    private var cancelSE = 0
    private var menuSelectSE = 0
    private var cannotDoitSE = 0
    private var gameStartSE = 0
//
    private var finished = false
    private var winner="none"
    private var turn = 0 //後でちゃんと設定する
    private var size = 0
    private var movingSource = "none"
    private var destination = "none"
    private var pickupDone= false
    private var insetDone = false
//文字列
    //手持ち
    private val stringTemochiRedBig="tRB"
    private val stringTemochiRedMiddle="tRM"
    private val stringTemochiRedSmall="tRS"
    private val stringTemochiGreenBig="tGB"
    private val stringTemochiGreenMiddle="tGM"
    private val stringTemochiGreenSmall="tGS"
    //マス
    private val stringA1="A1"
    private val stringA2="A2"
    private val stringA3="A3"
    private val stringA4="A4"
    private val stringB1="B1"
    private val stringB2="B2"
    private val stringB3="B3"
    private val stringB4="B4"
    private val stringC1="C1"
    private val stringC2="C2"
    private val stringC3="C3"
    private val stringC4="C4"
    private val stringD1="D1"
    private val stringD2="D2"
    private val stringD3="D3"
    private val stringD4="D4"
    //手持ち
    private val temochiRedBig = Temochi(3,"tRB")
    private val temochiRedMiddle = Temochi(2,"tRM")
    private val temochiRedSmall = Temochi(1,"tRS")

    private val temochiGreenBig = Temochi(3,"tGB")
    private val temochiGreenMiddle = Temochi(2,"tGM")
    private val temochiGreenSmall = Temochi(1,"tGS")
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
//ライン デバック用
    var line1 = mutableListOf<Int>(0, 0, 0, 0)
    var line2 = mutableListOf<Int>(0, 0, 0, 0)
    var line3 = mutableListOf<Int>(0, 0, 0, 0)
    var line4 = mutableListOf<Int>(0, 0, 0, 0)

    var judgeMap = mutableMapOf<String, Int>(
        "lineA" to 0, "lineB" to 0, "lineC" to 0, "lineD" to 0,
        "line1" to 0, "line2" to 0, "line3" to 0, "line4" to 0,
        "lineS" to 0, "lineBS" to 0
    )

//表示に使う物
    private var res:Resources? = null
    private var view: ImageView? = null
    private var masImag = res?.getDrawable(R.drawable.mass)
//駒
    //赤
    private var komaRedBigD = res?.getDrawable(R.drawable.koma_red_big)
    private var komaRedMiddleD = res?.getDrawable(R.drawable.koma_red_middle)
    private var komaRedSmallD = res?.getDrawable(R.drawable.koma_red_small)
    //緑
    private var komaGreenBigD = res?.getDrawable(R.drawable.koma_green_big)
    private var komaGreenMiddleD = res?.getDrawable(R.drawable.koma_green_middle)
    private var komaGreenSmallD = res?.getDrawable(R.drawable.koma_green_small)
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
        setContentView(R.layout.activity_game_with_com)

        //画面の大きさ
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(dm)
        width = dm.widthPixels
        height = dm.heightPixels

        //共有プリファレンス
        pref = PreferenceManager.getDefaultSharedPreferences(this@GameWithComActivity)
        SE = pref!!.getBoolean("SEOnOff", true)
        BGM =pref!!.getBoolean("BGMOnOff", true)
        playFirst=pref!!.getInt("playFirst", 1)

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

        cannotDoitSE=sp.load(this, R.raw.cannotdoit, 1)
        putSE=sp.load(this, R.raw.select_se, 1)
        selectSE = sp.load(this, R.raw.put, 1)
        cancelSE = sp.load(this, R.raw.cancel, 1)
        menuSelectSE = sp.load(this, R.raw.menu_selected, 1)
        gameStartSE = sp.load(this,R.raw.game_start_se,1)

        //mediaPlayer
        player=MediaPlayer.create(applicationContext,R.raw.okashi_time)
        player?.isLooping=true
        if (BGM==true){
            bgmlooping=true
            player?.start()
        }
//表示
        res=resources
        view=findViewById(R.id.buttonA1)
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


    //結果ボタン
        resaltButton.visibility=View.INVISIBLE
//先攻後攻設定
        if (playFirst != 0){
            turn = playFirst
        } else {
            when((1..2).random()){
                1 -> {turn = 1}
                2 -> {turn = -1}
            }
        }
        Log.d("gobblet2", "pF:${playFirst}")
        startTurn()

//手持ち
        buttonTemochiRedBig.setOnClickListener {
            if (turn==1){
                if (movingSource=="none"||
                    movingSource==stringTemochiRedBig ||
                    movingSource==stringTemochiRedMiddle||
                    movingSource==stringTemochiRedSmall){
                    pickupTemochi(stringTemochiRedBig)
                }
            }
            else{toastNotyourturn()}
        }

        buttonTemochiRedMiddle.setOnClickListener {
            if (turn == 1){
                if (movingSource=="none"||
                    movingSource==stringTemochiRedBig ||
                    movingSource==stringTemochiRedMiddle||
                    movingSource==stringTemochiRedSmall){
                    pickupTemochi(stringTemochiRedMiddle)
                }
            }
            else{toastNotyourturn()}
        }

        buttonTemochiRedSmall.setOnClickListener {
            if (turn == 1){
                if (movingSource=="none"||
                    movingSource==stringTemochiRedBig ||
                    movingSource==stringTemochiRedMiddle||
                    movingSource==stringTemochiRedSmall){
                    pickupTemochi(stringTemochiRedSmall)
                }
            }
            else{toastNotyourturn()}
        }

        buttonTemochiGreenBig.setOnClickListener {
            if (turn == -1){
                if (movingSource=="none"||
                    movingSource==stringTemochiGreenBig ||
                    movingSource==stringTemochiGreenMiddle||
                    movingSource==stringTemochiGreenSmall) {
                    pickupTemochi(stringTemochiGreenBig)
                }
            }
            else{toastNotyourturn()}
        }

        buttonTemochiGreenMiddle.setOnClickListener {
            if (turn == -1){
                if (movingSource=="none"||
                    movingSource==stringTemochiGreenBig ||
                    movingSource==stringTemochiGreenMiddle||
                    movingSource==stringTemochiGreenSmall){
                    pickupTemochi(stringTemochiGreenMiddle)
                }
            }
            else{toastNotyourturn()}
        }

        buttonTemochiGreenSmall.setOnClickListener {
            if (turn == -1){
                if (movingSource=="none"||
                    movingSource==stringTemochiGreenBig ||
                    movingSource==stringTemochiGreenMiddle||
                    movingSource==stringTemochiGreenSmall){
                    pickupTemochi(stringTemochiGreenSmall)
                }
            }
            else{toastNotyourturn()}
        }
//マス
        buttonA1.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringA1) }
            else{toastNotyourturn()}
        }

        buttonA2.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringA2) }
            else{toastNotyourturn()}
        }

        buttonA3.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringA3) }
            else{toastNotyourturn()}
        }

        buttonA4.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringA4) }
            else{toastNotyourturn()}
        }

        buttonB1.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringB1) }
            else{toastNotyourturn()}
        }

        buttonB2.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringB2) }
            else{toastNotyourturn()}
        }

        buttonB3.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringB3) }
            else{toastNotyourturn()}
        }

        buttonB4.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringB4) }
            else{toastNotyourturn()}
        }

        buttonC1.setOnClickListener {
            if (turn != 0){pushedMasButton(stringC1) }
            else{toastNotyourturn()}
        }

        buttonC2.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringC2) }
            else{toastNotyourturn()}
        }

        buttonC3.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringC3) }
            else{toastNotyourturn()}
        }

        buttonC4.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringC4) }
            else{toastNotyourturn()}
        }

        buttonD1.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringD1) }
            else{toastNotyourturn()}
        }

        buttonD2.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringD2) }
            else{toastNotyourturn()}
        }

        buttonD3.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringD3) }
            else{toastNotyourturn()}
        }

        buttonD4.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringD4) }
            else{toastNotyourturn()}
        }

        // その他
        configButton.setOnClickListener {
            showConfigPopup()
        }

        resaltButton.setOnClickListener {
            showResaltPopup()
        }

    }

    //ポップアップ
    private fun showResaltPopup(){
        resaltPopup = PopupWindow(this@GameWithComActivity)
        // レイアウト設定
        val popupView: View = layoutInflater.inflate(R.layout.popup_resalt, null)
        resaltPopup!!.contentView = popupView

        // 関数設定
        popupView.findViewById<View>(R.id.backButton).setOnClickListener {
            if (resaltPopup!!.isShowing) {
                val toast =Toast.makeText(this, "", Toast.LENGTH_LONG)
                val customView = layoutInflater.inflate(R.layout.dammy, null)
                toast.view = customView
                toast.setGravity(Gravity.BOTTOM, 0,0 )
                toast.show()//これで無理やりナビゲーションバーを消す
                playSound("cancel")
                resaltPopup!!.dismiss()
            }
        }

        val resaltImage = popupView.findViewById<ImageView>(R.id.resaltImage)
        when(winner){

        }

        popupView.findViewById<View>(R.id.BackToTitleButton).setOnClickListener {
            playSound("cancel")
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        popupView.findViewById<View>(R.id.retryButtton).setOnClickListener {
            playSound("gameStart")
            val intent = Intent(this, GameWithManActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        popupView.findViewById<View>(R.id.backPrebutton).setOnClickListener {
            playSound("cancel")
            val intent = Intent(this, preGameWithManActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        // タップ時に他のViewでキャッチされないための設定
        resaltPopup!!.isOutsideTouchable = true
        resaltPopup!!.isFocusable = true

        // 表示サイズの設定
        resaltPopup!!.setWindowLayoutMode(
            width.toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        resaltPopup!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        resaltPopup!!.height = WindowManager.LayoutParams.WRAP_CONTENT

        // 画面中央に表示
        resaltPopup!!.showAtLocation(findViewById(R.id.configButton), Gravity.CENTER, 0, 0)
    }

    private fun showConfigPopup(){
        configPopup = PopupWindow(this@GameWithComActivity)
        // レイアウト設定
        val popupView: View = layoutInflater.inflate(R.layout.popup_config, null)
        configPopup!!.contentView = popupView
        //ラジオボタン初期化
        val RadioSE = popupView.findViewById<RadioGroup>(R.id.SEOnOff)
        when(SE){
            true -> {popupView.findViewById<RadioButton>(R.id.SEOn).isChecked = true}
            false -> {popupView.findViewById<RadioButton>(R.id.SEOff).isChecked = true}
        }

        val RadioBGM = popupView.findViewById<RadioGroup>(R.id.BGMOnOff)
        when(BGM){
            true -> {popupView.findViewById<RadioButton>(R.id.BGMOn).isChecked = true}
            false -> {popupView.findViewById<RadioButton>(R.id.BGMOff).isChecked = true}
        }


        // 関数設定
        RadioSE.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.SEOn->{SE=true}
                R.id.SEOff->{SE=false}
            }
            playSound("menuSelect")
            val editor=pref!!.edit()
            editor.putBoolean("SEOnOff",SE).apply()
            Log.d("gobblet2", "SE=${SE}")
        }

        RadioBGM.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.BGMOn->{
                    BGM=true
                    player?.start()
//                        if (!bgmlooping){
//
//                        }
                }
                R.id.BGMOff->{
                    BGM=false
                    player?.pause()
                }
            }
            playSound("menuSelect")
            val editor= pref!!.edit()
            editor.putBoolean("BGMOnOff",BGM).apply()
            Log.d("gobblet2", "BGM=${BGM}")
        }

        popupView.findViewById<View>(R.id.BackToTitleButton).setOnClickListener {
            playSound("cancel")
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        popupView.findViewById<View>(R.id.backButton).setOnClickListener {
            if (configPopup!!.isShowing) {
                val toast =Toast.makeText(this, "", Toast.LENGTH_LONG)
                val customView = layoutInflater.inflate(R.layout.dammy, null)
                toast.view = customView
                toast.setGravity(Gravity.BOTTOM, 0,0 )
                toast.show()//これで無理やりナビゲーションバーを消す
                playSound("cancel")
                configPopup!!.dismiss()
            }
        }


        // 背景設定
        configPopup!!.setBackgroundDrawable(resources.getDrawable(R.drawable.popup_background))

        // タップ時に他のViewでキャッチされないための設定
        configPopup!!.isOutsideTouchable = true
        configPopup!!.isFocusable = true

        // 表示サイズの設定 今回は幅300dp
//            val width =
//                TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_DIP,
//                    300f,
//                    resources.displayMetrics
//                )
        configPopup!!.setWindowLayoutMode(
            width.toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        configPopup!!.width =  WindowManager.LayoutParams.WRAP_CONTENT*1/2
        configPopup!!.height = WindowManager.LayoutParams.WRAP_CONTENT*1/2

        // 画面中央に表示
        configPopup!!.showAtLocation(findViewById(R.id.configButton), Gravity.CENTER, 0, 0)
    }

    //トースト関係
    private fun toastCanNotPickup(){
        val toast = CustomToast.makeText(applicationContext, "そこにあなたの駒はありません", 1000,width,height)
        toast.show()
        playSound("cannotDoit")
    }

    private fun toastCanNotInsert(){
        val toast = CustomToast.makeText(applicationContext, "そこにあなたの駒は置けません", 1000,width,height)
        toast.show()
        playSound("cannotDoit")
    }

    private fun toastNotyourturn(){
        val toast = CustomToast.makeText(applicationContext, "あなたのターンではありません", 1000,width,height)
        toast.show()
        playSound("cannotDoit")
    }

    //ターン開始の処理
    private fun startTurn(){
        if (finished){turn=0}
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

    //ターン終了時の処理に関すること
    private fun resetForEnd() {
        bordDisplay(destination)
        if (movingSource==stringTemochiRedBig||
            movingSource==stringTemochiRedMiddle||
            movingSource==stringTemochiRedSmall||
            movingSource==stringTemochiGreenBig||
            movingSource==stringTemochiGreenMiddle||
            movingSource==stringTemochiGreenSmall){
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
    }

    private fun endTurn() {
        resetForEnd()
        resetHavingDisplay()
        judge()
        movingSource = "none"
        destination = "none"
        size = 0
        pickupDone = false
        insetDone = false
        turn*=-1
        Log.d("gobblet2", "turnEnd")
        startTurn()
    }

    //持ちての表示に関する関数
    private fun havingDisplay(){
        playSound("select")
        Log.d("gobblet2", "havingDisplay  size=${size},turn=${turn}")
        if (turn == 1){
            view = findViewById(R.id.having1p)
            when (size){
                3 -> {
                    view?.setImageDrawable(komaRedBigD)
                }
                2 -> {
                    view?.setImageDrawable(komaRedMiddleD)
                }
                1 -> {
                    view?.setImageDrawable(komaRedSmallD)
                }
            }
        } else if (turn ==-1){
            view = findViewById(R.id.having2p)
            when (size){
                3 -> {
                    view?.setImageDrawable(komaGreenBigD)
                }
                2 -> {
                    view?.setImageDrawable(komaGreenMiddleD)
                }
                1 -> {
                    view?.setImageDrawable(komaGreenSmallD)
                }
            }
        }
    }

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
    private fun bordDisplay(location: String) {
        var box = mutableListOf<Int>(0, 0)
        fun RedSet(s: Int){
            when (s) {
                3 -> {
                    view?.setImageDrawable(komaRedBigD)
                }
                2 -> {
                    view?.setImageDrawable(komaRedMiddleD)
                }
                1 -> {
                    view?.setImageDrawable(komaRedSmallD)
                }
            }
        }

        fun GreenSet(s: Int){
            when (s) {
                3 -> {
                    view?.setImageDrawable(komaGreenBigD)
                }
                2 -> {
                    view?.setImageDrawable(komaGreenMiddleD)
                }
                1 -> {
                    view?.setImageDrawable(komaGreenSmallD)
                }
            }
        }

        fun EmpSet(){ view?.setImageDrawable(masImag) }

        when (location) {
            stringA1 -> {
                view = findViewById(R.id.buttonA1)
                A1.funcForDisplay()
                box = A1.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringA2 -> {
                view = findViewById(R.id.buttonA2)
                A2.funcForDisplay()
                box = A2.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringA3 -> {
                view = findViewById(R.id.buttonA3)
                A3.funcForDisplay()
                box = A3.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringA4 -> {
                view = findViewById(R.id.buttonA4)
                A4.funcForDisplay()
                box = A4.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringB1 -> {
                view = findViewById(R.id.buttonB1)
                B1.funcForDisplay()
                box = B1.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringB2 -> {
                view = findViewById(R.id.buttonB2)
                B2.funcForDisplay()
                box = B2.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringB3 -> {
                view = findViewById(R.id.buttonB3)
                B3.funcForDisplay()
                box = B3.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringB4 -> {
                view = findViewById(R.id.buttonB4)
                B4.funcForDisplay()
                box = B4.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringC1 -> {
                view = findViewById(R.id.buttonC1)
                C1.funcForDisplay()
                box = C1.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringC2 -> {
                view = findViewById(R.id.buttonC2)
                C2.funcForDisplay()
                box = C2.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringC3 -> {
                view = findViewById(R.id.buttonC3)
                C3.funcForDisplay()
                box = C3.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringC4 -> {
                view = findViewById(R.id.buttonC4)
                C4.funcForDisplay()
                box = C4.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringD1 -> {
                view = findViewById(R.id.buttonD1)
                D1.funcForDisplay()
                box = D1.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringD2 -> {
                view = findViewById(R.id.buttonD2)
                D2.funcForDisplay()
                box = D2.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringD3 -> {
                view = findViewById(R.id.buttonD3)
                D3.funcForDisplay()
                box = D3.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
            stringD4 -> {
                view = findViewById(R.id.buttonD4)
                D4.funcForDisplay()
                box = D4.funcForDisplay()
                when (box[1]) {
                    1 -> {
                        RedSet(box[0])
                    }
                    0 -> {
                        EmpSet()
                    }
                    -1 -> {
                        GreenSet(box[0])
                    }
                }
            }
        }
    }

    //マスのボタンをおした時の作業
    private fun pickup(name: String){
        when(name){
            stringA1 -> {
                if (A1.mPickup(turn) != 0) {
                    setSMP(A1.mPickup(turn), stringA1)
                    debSMP()
                    havingDisplay()
                    A1.resetList(size)
                    bordDisplay(stringA1)
                    judge()//ここでしたが相手のこまで一列そろってしまったときも相手のかちにする
                } else {
                    toastCanNotPickup()
                }//取り出せるものが無い時の動き
            }
            stringA2 -> {
                if (A2.mPickup(turn) != 0) {
                    setSMP(A2.mPickup(turn), stringA2)
                    debSMP()
                    havingDisplay()
                    A2.resetList(size)
                    bordDisplay(stringA2)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringA3 -> {
                if (A3.mPickup(turn) != 0) {
                    setSMP(A3.mPickup(turn), stringA3)
                    debSMP()
                    havingDisplay()
                    A3.resetList(size)
                    bordDisplay(stringA3)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringA4 -> {
                if (A4.mPickup(turn) != 0) {
                    setSMP(A4.mPickup(turn), stringA4)
                    debSMP()
                    havingDisplay()
                    A4.resetList(size)
                    bordDisplay(stringA4)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringB1 -> {
                if (B1.mPickup(turn) != 0) {
                    setSMP(B1.mPickup(turn), stringB1)
                    debSMP()
                    havingDisplay()
                    B1.resetList(size)
                    bordDisplay(stringB1)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringB2 -> {
                if (B2.mPickup(turn) != 0) {
                    setSMP(B2.mPickup(turn), stringB2)
                    debSMP()
                    havingDisplay()
                    B2.resetList(size)
                    bordDisplay(stringB2)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringB3 -> {
                if (B3.mPickup(turn) != 0) {
                    setSMP(B3.mPickup(turn), stringB3)
                    debSMP()
                    havingDisplay()
                    B3.resetList(size)
                    bordDisplay(stringB3)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringB4 -> {
                if (B4.mPickup(turn) != 0) {
                    setSMP(B4.mPickup(turn), stringB4)
                    debSMP()
                    havingDisplay()
                    B4.resetList(size)
                    bordDisplay(stringB4)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringC1 -> {
                if (C1.mPickup(turn) != 0) {
                    setSMP(C1.mPickup(turn), stringC1)
                    debSMP()
                    havingDisplay()
                    C1.resetList(size)
                    bordDisplay(stringC1)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringC2 -> {
                if (C2.mPickup(turn) != 0) {
                    setSMP(C2.mPickup(turn), stringC2)
                    debSMP()
                    havingDisplay()
                    C2.resetList(size)
                    bordDisplay(stringC2)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringC3 -> {
                if (C3.mPickup(turn) != 0) {
                    setSMP(C3.mPickup(turn), stringC3)
                    debSMP()
                    havingDisplay()
                    C3.resetList(size)
                    bordDisplay(stringC3)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringC4 -> {
                if (C4.mPickup(turn) != 0) {
                    setSMP(C4.mPickup(turn), stringC4)
                    debSMP()
                    havingDisplay()
                    C4.resetList(size)
                    bordDisplay(stringC4)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringD1 -> {
                if (D1.mPickup(turn) != 0) {
                    setSMP(D1.mPickup(turn), stringD1)
                    debSMP()
                    havingDisplay()
                    D1.resetList(size)
                    bordDisplay(stringD1)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringD2 -> {
                if (D2.mPickup(turn) != 0) {
                    setSMP(D2.mPickup(turn), stringD2)
                    debSMP()
                    havingDisplay()
                    D2.resetList(size)
                    bordDisplay(stringD2)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringD3 -> {
                if (D3.mPickup(turn) != 0) {
                    setSMP(D3.mPickup(turn), stringD3)
                    debSMP()
                    havingDisplay()
                    D3.resetList(size)
                    bordDisplay(stringD3)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
            stringD4 -> {
                if (D4.mPickup(turn) != 0) {
                    setSMP(D4.mPickup(turn), stringD4)
                    debSMP()
                    havingDisplay()
                    D4.resetList(size)
                    bordDisplay(stringD4)
                    judge()
                } else {
                    toastCanNotPickup()
                }
            }
        }
    }

    //駒を入れる
    private fun insert(name: String){
        when(name){
            stringA1 -> {
                when (A1.mInsert(size, turn)) {
                    true -> {
                        setD(stringA1)
                    }
                    false -> {
                        toastCanNotInsert()
                    }//トースト表示でおけないことを知らせる
                }
            }
            stringA2 -> {
                when (A2.mInsert(size, turn)) {
                    true -> {
                        setD(stringA2)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringA3 -> {
                when (A3.mInsert(size, turn)) {
                    true -> {
                        setD(stringA3)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringA4 -> {
                when (A4.mInsert(size, turn)) {
                    true -> {
                        setD(stringA4)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringB1 -> {
                when (B1.mInsert(size, turn)) {
                    true -> {
                        setD(stringB1)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringB2 -> {
                when (B2.mInsert(size, turn)) {
                    true -> {
                        setD(stringB2)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringB3 -> {
                when (B3.mInsert(size, turn)) {
                    true -> {
                        setD(stringB3)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringB4 -> {
                when (B4.mInsert(size, turn)) {
                    true -> {
                        setD(stringB4)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringC1 -> {
                when (C1.mInsert(size, turn)) {
                    true -> {
                        setD(stringC1)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringC2 -> {
                when (C2.mInsert(size, turn)) {
                    true -> {
                        setD(stringC2)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringC3 -> {
                when (C3.mInsert(size, turn)) {
                    true -> {
                        setD(stringC3)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringC4 -> {
                when (C4.mInsert(size, turn)) {
                    true -> {
                        setD(stringC4)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringD1 -> {
                when (D1.mInsert(size, turn)) {
                    true -> {
                        setD(stringD1)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringD2 -> {
                when (D2.mInsert(size, turn)) {
                    true -> {
                        setD(stringD2)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringD3 -> {
                when (D3.mInsert(size, turn)) {
                    true -> {
                        setD(stringD3)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
            stringD4 -> {
                when (D4.mInsert(size, turn)) {
                    true -> {
                        setD(stringD4)
                    }
                    false -> {
                        toastCanNotInsert()
                    }
                }
            }
        }
    }

    //一旦ここを通して分岐
    private fun pushedMasButton(name: String){
        if (!pickupDone) {//取り出し作業
            pickup(name)
        } else{//マスの中に入れる
            insert(name)
        }
    }

    //手持ちボタンを押した時の作業
    private fun pickupTemochi(name: String){
        var rv = 0
        when(name){
            stringTemochiRedBig -> {
                rv = temochiRedBig.returnInf()
                if (rv != 0) {
                    setSMP(rv, stringTemochiRedBig)
                    havingDisplay()
                    debSMP()
                }
            }
            stringTemochiRedMiddle -> {
                rv = temochiRedMiddle.returnInf()
                if (rv != 0) {
                    setSMP(rv, stringTemochiRedMiddle)
                    havingDisplay()
                    debSMP()
                }
            }
            stringTemochiRedSmall -> {
                rv = temochiRedSmall.returnInf()
                if (rv != 0) {
                    setSMP(rv, stringTemochiRedSmall)
                    havingDisplay()
                    debSMP()
                }
            }
            stringTemochiGreenBig -> {
                rv = temochiGreenBig.returnInf()
                if (rv != 0) {
                    setSMP(rv, stringTemochiGreenBig)
                    havingDisplay()
                    debSMP()
                }
            }
            stringTemochiGreenMiddle -> {
                rv = temochiGreenMiddle.returnInf()
                if (rv != 0) {
                    setSMP(rv, stringTemochiGreenMiddle)
                    havingDisplay()
                    debSMP()
                }
            }
            stringTemochiGreenSmall -> {
                rv = temochiGreenSmall.returnInf()
                if (rv != 0) {
                    setSMP(rv, stringTemochiGreenSmall)
                    havingDisplay()
                    debSMP()
                }
            }
        }
    }

    private fun playSound(status: String){
        if (SE){
            when(status){
                "cannotDoit" -> sp.play(cannotDoitSE, 1.0f, 1.0f, 1, 0, 1.5f)
                "put" -> sp.play(putSE, 1.0f, 1.0f, 1, 0, 1.0f)
                "select" -> sp.play(selectSE, 1.0f, 1.0f, 1, 0, 1.0f)
                "cancel" -> sp.play(cancelSE, 1.0f, 1.0f, 1, 0, 1.0f)
                "menuSelect" -> sp.play(menuSelectSE, 1.0f, 1.0f, 1, 0, 1.0f)
                "gameStart" -> sp.play(gameStartSE, 1.0f, 1.0f, 1, 0, 1.0f)
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

    private fun setD(location: String) {
        playSound("put")
        destination = location
        Log.d("gobblet2", "destination:${destination}")
        endTurn()
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
        }

        if (judgeMap.containsValue(-4)){
            finished=true
            winner="2p"
        }

        if (finished){
            resaltButton.visibility=View.VISIBLE
            showResaltPopup()
        }

        debjudge()
    }

    private fun debjudge(){
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
    }

    private fun hideSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            // Note that system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                Log.d("debug", "The system bars are visible")
            } else {
                Log.d("debug", "The system bars are NOT visible")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sp.release()
        player?.release()
        player=null
    }
}