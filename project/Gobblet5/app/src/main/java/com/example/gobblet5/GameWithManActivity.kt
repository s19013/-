package com.example.gobblet5

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_game_with_man.*
import java.util.*


class GameWithManActivity : AppCompatActivity() {
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
    private var radioButtonSE = 0
    private var openSE = 0
    private var closeSE = 0
    //ゲームに必要なもの
    private var finished = false
    private var winner="none"
    private var turn = 0 //後でちゃんと設定する
    private var size = 0
    private var movingSource = "none"
    private var destination = "none"
    private var pickupDone= false
    private var insetDone = false
  ////文字列
    //手持ち
    private val stringTemochiRedBig="TemochiRedBig"
    private val stringTemochiRedMiddle="TemochiRedMiddle"
    private val stringTemochiRedSmall="TemochiRedSmall"
    private val stringTemochiGreenBig="TemochiGreenBig"
    private val stringTemochiGreenMiddle="TemochiGreenMiddle"
    private val stringTemochiGreenSmall="TemochiGreenSmall"
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
  ////手持ち
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
    //ライン デバック用
    var line1 = mutableListOf<Int>(0, 0, 0, 0)
    var line2 = mutableListOf<Int>(0, 0, 0, 0)
    var line3 = mutableListOf<Int>(0, 0, 0, 0)
    var line4 = mutableListOf<Int>(0, 0, 0, 0)
    //勝敗を決めるのに使う
    var judgeMap = mutableMapOf<String, Int>(
        "lineA" to 0, "lineB" to 0, "lineC" to 0, "lineD" to 0,
        "line1" to 0, "line2" to 0, "line3" to 0, "line4" to 0,
        "lineS" to 0, "lineBS" to 0
    )

    //表示に使う物　(箱を用意している状態)
    private var res: Resources? = null
    private var view: ImageView? = null
    //マス
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
        setContentView(R.layout.activity_game_with_man)

        //初期化
        iniFullscrean()
        iniPreference()
        iniSoundPool()
        iniMediaPlayer()
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



//手持ちのボタンを触った時
        buttonTemochiRedBig.setOnClickListener {
            if (turn == 1){
                //movingSourceが同じときやり直しができる
                if (movingSource == stringTemochiRedBig){
                    resetTemochi()
                }
                //移動元が手持ちの場合のみコマを
                else if (movingSource=="none"||
                    movingSource==stringTemochiRedMiddle||
                    movingSource==stringTemochiRedSmall){
                    pickupTemochi(stringTemochiRedBig)
                }
            }
            else{toastNotyourturn()}
        }

        buttonTemochiRedMiddle.setOnClickListener {
            if (turn == 1){
                if (movingSource==stringTemochiRedMiddle){
                    resetTemochi()
                }
                else if (movingSource=="none"||
                    movingSource==stringTemochiRedBig ||
                    movingSource==stringTemochiRedSmall){
                    pickupTemochi(stringTemochiRedMiddle)
                }
            }
            else{toastNotyourturn()}
        }

        buttonTemochiRedSmall.setOnClickListener {
            if (turn == 1){
                if (movingSource==stringTemochiRedSmall){
                    resetTemochi()
                }
                else if (movingSource=="none"||
                    movingSource==stringTemochiRedBig ||
                    movingSource==stringTemochiRedMiddle){
                    pickupTemochi(stringTemochiRedSmall)
                }
            }
            else{toastNotyourturn()}
        }

        buttonTemochiGreenBig.setOnClickListener {
            if (turn == -1){
                if (movingSource==stringTemochiGreenBig){
                    resetTemochi()
                }
                else if (movingSource=="none"||
                    movingSource==stringTemochiGreenMiddle||
                    movingSource==stringTemochiGreenSmall) {
                    pickupTemochi(stringTemochiGreenBig)
                }
            }
            else{toastNotyourturn()}
        }

        buttonTemochiGreenMiddle.setOnClickListener {
            if (turn == -1){
                if (movingSource==stringTemochiGreenMiddle){
                    resetTemochi()
                }
                else if (movingSource=="none"||
                    movingSource==stringTemochiGreenBig ||
                    movingSource==stringTemochiGreenSmall){
                    pickupTemochi(stringTemochiGreenMiddle)
                }
            }
            else{toastNotyourturn()}
        }

        buttonTemochiGreenSmall.setOnClickListener {
            if (turn == -1){
                if (movingSource==stringTemochiGreenSmall){
                    resetTemochi()
                }
                else if (movingSource=="none"||
                    movingSource==stringTemochiGreenBig ||
                    movingSource==stringTemochiGreenMiddle){
                    pickupTemochi(stringTemochiGreenSmall)
                }
            }
            else{toastNotyourturn()}
        }
      ////マスを触ったとき
        buttonA1.setOnClickListener {
            if (turn != 0){ pushedMasButton(stringA1) }//ゲームが終わったらさわれないようにするためにこんなif文を書く
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
            playSound(openSE)
            showConfigPopup()
        }

        resaltButton.setOnClickListener {
            playSound(openSE)
            showResaltPopup()
        }

    }

  ////ポップアップ
    //結果ポップアップ
    private fun showResaltPopup(){
      resaltPopup = PopupWindow(this@GameWithManActivity)
      // レイアウト設定
      val popupView: View = layoutInflater.inflate(R.layout.popup_resalt, null)
      resaltPopup!!.contentView = popupView
      // タップ時に他のViewでキャッチされないための設定
      resaltPopup!!.isOutsideTouchable = true
      resaltPopup!!.isFocusable = true

      // 表示サイズの設定
      resaltPopup!!.width  = width*8/10
       resaltPopup!!.height = height*8/10

      // 画面中央に表示
      resaltPopup!!.showAtLocation(findViewById(R.id.configButton), Gravity.CENTER, 0, 0)

      //// 関数設定
      val resaltImage = popupView.findViewById<ImageView>(R.id.resaltImage)


      //画像を設定する
      if (Locale.getDefault().equals(Locale.JAPAN)){
          when(winner){
              "1p" -> resaltImage.setImageResource(R.drawable.win1p_jp)
              "2p" -> resaltImage.setImageResource(R.drawable.win2p_jp)
          }
          Log.d("gobblet2", "jp")
      } else {
          when(winner){
              "1p" -> resaltImage.setImageResource(R.drawable.win1p_en)
              "2p" -> resaltImage.setImageResource(R.drawable.win2p_en)
          }
          Log.d("gobblet2", "en")
      }


      popupView.findViewById<View>(R.id.BackToTitleButton).setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

      popupView.findViewById<View>(R.id.retryButtton).setOnClickListener {
            playSound(gameStartSE)
            val intent = Intent(this, GameWithManActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

      popupView.findViewById<View>(R.id.backPrebutton).setOnClickListener {
          playSound(cancelSE)
            val intent = Intent(this, preGameWithManActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

      popupView.findViewById<View>(R.id.backButton).setOnClickListener {
          if (resaltPopup!!.isShowing) {
              val toast =Toast.makeText(this, "", Toast.LENGTH_LONG)
              val customView = layoutInflater.inflate(R.layout.dammy, null)
              toast.view = customView
              toast.setGravity(Gravity.BOTTOM, 0,0 )
              toast.show()//これで無理やりナビゲーションバーを消す
              playSound(closeSE)
              resaltPopup!!.dismiss()
          }
      }
    }

    //設定ポップアップ
    private fun showConfigPopup(){
        configPopup = PopupWindow(this@GameWithManActivity)
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


        //// 関数設定
        RadioSE.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.SEOn->{SE=true}
                R.id.SEOff->{SE=false}
            }
            playSound(radioButtonSE)
            val editor=pref!!.edit()
            editor.putBoolean("SEOnOff",SE).apply()
            Log.d("gobblet2", "SE=${SE}")
        }

        RadioBGM.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.BGMOn->{
                    BGM=true
                    player?.start()
                }
                R.id.BGMOff->{
                    BGM=false
                    player?.pause()
                }
            }
            playSound(radioButtonSE)
            val editor= pref!!.edit()
            editor.putBoolean("BGMOnOff",BGM).apply()
            Log.d("gobblet2", "BGM=${BGM}")
        }

        popupView.findViewById<View>(R.id.BackToTitleButton).setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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
        playSound(cannotDoitSE)
    }

    private fun toastCanNotInsert(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.cannotInsertDialogText))
            .setNeutralButton(getString(R.string.OkText)) { _, _ -> }
            .show()
        playSound(cannotDoitSE)
    }

    private fun toastNotyourturn(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.notYourTurnDialogText))
            .setNeutralButton(getString(R.string.OkText)) { _, _ -> }
            .show()
        playSound(cannotDoitSE)
    }

    //持ちての表示に関する関数
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
    private fun bordDisplay(location: String) {
        var box = mutableListOf<Int>(0, 0)
        //大きさを判断
        fun RedSet(s: Int){
            when (s) {
                3 -> { view?.setImageDrawable(komaRedBigD) }
                2 -> { view?.setImageDrawable(komaRedMiddleD) }
                1 -> { view?.setImageDrawable(komaRedSmallD) }
            }
        }

        fun GreenSet(s: Int){
            when (s) {
                3 -> { view?.setImageDrawable(komaGreenBigD) }
                2 -> { view?.setImageDrawable(komaGreenMiddleD) }
                1 -> { view?.setImageDrawable(komaGreenSmallD) }
            }
        }

        fun EmpSet(){ view?.setImageDrawable(masImag) }

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
            1 -> { RedSet(box[0]) }
            0 -> { EmpSet() }
            -1 -> { GreenSet(box[0]) }
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
    private fun pushedMasButton(name: String){
        if (!pickupDone) {//取り出し作業
            pickup(name)
        } else{//マスの中に入れる
            insert(name)
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
    private fun insert(name: String){
        when(name){
            stringA1 -> {
                if (A1.mInsert(size, turn)) {
                    setD(stringA1)
                    return
                }
            }
            stringA2 -> {
                if (A2.mInsert(size, turn)) {
                    setD(stringA2)
                    return
                }
            }
            stringA3 -> {
                if (A3.mInsert(size, turn)) {
                    setD(stringA3)
                    return
                }
            }
            stringA4 -> {
                if (A4.mInsert(size, turn)) {
                    setD(stringA4)
                    return
                }
            }
            stringB1 -> {
                if (B1.mInsert(size, turn)) {
                    setD(stringB1)
                    return
                }
            }
            stringB2 -> {
                if (B2.mInsert(size, turn)) {
                    setD(stringB2)
                    return
                }
            }
            stringB3 -> {
                if (B3.mInsert(size, turn)) {
                    setD(stringB3)
                    return
                }
            }
            stringB4 -> {
                if (B4.mInsert(size, turn)) {
                    setD(stringB4)
                    return
                }
            }
            stringC1 -> {
                if (C1.mInsert(size, turn)) {
                    setD(stringC1)
                    return
                }
            }
            stringC2 -> {
                if (C2.mInsert(size, turn)) {
                    setD(stringC2)
                    return
                }
            }
            stringC3 -> {
                if (C3.mInsert(size, turn)) {
                    setD(stringC3)
                    return
                }
            }
            stringC4 -> {
                if (C4.mInsert(size, turn)) {
                    setD(stringC4)
                    return
                }
            }
            stringD1 -> {
                if (D1.mInsert(size, turn)) {
                    setD(stringD1)
                    return
                }
            }
            stringD2 -> {
                if (D2.mInsert(size, turn)) {
                    setD(stringD2)
                    return
                }
            }
            stringD3 -> {
                if (D3.mInsert(size, turn)) {
                    setD(stringD3)
                    return
                }
            }
            stringD4 -> {
                if (D4.mInsert(size, turn)) {
                    setD(stringD4)
                    return
                }
            }
        }
        toastCanNotInsert()//トースト表示でおけないことを知らせる

    }

    //手持ちやりなおし
    private fun resetTemochi(){
        Log.d("gobblet2", "resetTemochi")
        resetSMP()
        resetHavingDisplay()
        debSMP()
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
        movingSource = "none"
        destination = "none"
        size = 0
        pickupDone = false
        insetDone = false
        turn*=-1
        Log.d("gobblet2", "turnEnd")
        startTurn()
    }

    //移動元が手持ちだったときのリセットしょり?
    private fun resetForEnd() {
        bordDisplay(destination)
//        var Mas:mas = A1
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

    private fun debSMP(){
        Log.d("gobblet2", "")
        Log.d("gobblet2", "size:${size}, movingSource:${movingSource}, pickupDone:${pickupDone}")
    }

    private fun resetSMP(){
        size=0
        movingSource="none"
        pickupDone=false
    }

    private fun setSMP(s: Int, m: String){
        size=s
        movingSource=m
        pickupDone=true
    }

    private fun setD(location: String) {
        playSound(putSE)
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

    //初期化に関する関数
    private fun iniFullscrean(){
        //画面の大きさ
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(dm)
        width = dm.widthPixels
        height = dm.heightPixels
    }

    private fun iniPreference(){
        //共有プリファレンス
        pref = PreferenceManager.getDefaultSharedPreferences(this@GameWithManActivity)
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
        cannotDoitSE=sp.load(this, R.raw.cannotdoit, 1)
        putSE=sp.load(this, R.raw.select_se, 1)
        selectSE = sp.load(this, R.raw.put, 1)
        cancelSE = sp.load(this, R.raw.cancel, 1)
        menuSelectSE = sp.load(this, R.raw.menu_selected, 1)
        gameStartSE = sp.load(this,R.raw.game_start_se,1)
        openSE = sp.load(this,R.raw.open,1)
        closeSE = sp.load(this,R.raw.close,1)
    }

    private fun iniMediaPlayer(){
        //mediaPlayer
        player=MediaPlayer.create(applicationContext,R.raw.okashi_time)
        player?.isLooping=true
        if (BGM==true){
            bgmlooping=true
            player?.start()
        }
    }



    //音を鳴らす処理
    private fun playSound(status: Int){
        if (SE){
            when(status){
                cannotDoitSE -> sp.play(cannotDoitSE, 1.0f, 1.0f, 1, 0, 1.5f)
                putSE -> sp.play(putSE, 1.0f, 1.0f, 1, 0, 1.0f)
                selectSE -> sp.play(selectSE, 1.0f, 1.0f, 1, 0, 1.0f)
                cancelSE -> sp.play(cancelSE, 1.0f, 1.0f, 1, 0, 1.0f)
                menuSelectSE -> sp.play(menuSelectSE, 1.0f, 1.0f, 1, 0, 1.0f)
                gameStartSE -> sp.play(gameStartSE, 1.0f, 1.0f, 1, 0, 1.0f)
                radioButtonSE -> sp.play(radioButtonSE,1.0f,1.0f,1,0,1.0f)
                openSE -> sp.play(openSE,1.0f,1.0f,1,0,1.0f)
                closeSE -> sp.play(closeSE,1.0f,1.0f,1,0,1.0f)
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
            player?.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (BGM){
            player?.pause()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        sp.release()
        player?.release()
        player=null
    }
}