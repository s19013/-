package com.example.gobblet5

import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_pre_game_with_man.*


class preGameWithManActivity : AppCompatActivity() {
    private lateinit var sp: SoundPool
    private var cancelSE = 0
    private var gameStartSE = 0
    private var radioButtonSE = 0

    private var res: Resources? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game_with_man)

//        backButton.getViewTreeObserver()
//            .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
//                override fun onGlobalLayout() {
//                    val width = converPxToDp(backButton.width) // 横幅が返却される
//                    val height = converPxToDp(backButton.height)
//                    backButton.setCompoundDrawablesWithIntrinsicBounds(UturnIcon, 0, 0, 0)
//                   backButton.setCompoundDrawables(UturnIcon, null, null, null)
//                    backButton.getViewTreeObserver().removeOnGlobalLayoutListener(this)
//                }
//            })
        
//        backButton.post {
//            val width = converPxToDp(backButton.width)
//            val height = converPxToDp(backButton.height)
//
//            backButton.setCompoundDrawables(UturnIcon,null,null,null)
//        }

        //共有プリファレンス
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        var SE =pref.getBoolean("SEOnOff", true)
        var playFirst=pref.getInt("playFirst", 1)

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

        cancelSE = sp.load(this, R.raw.cancel, 1)
        gameStartSE = sp.load(this,R.raw.game_start_se,1)
        radioButtonSE = sp.load(this,R.raw.radio_button,1)

        fun playSound(status: Int){
            if (SE){
                when(status){
                    cancelSE -> sp.play(cancelSE, 1.0f, 1.0f, 1, 0, 1.0f)
                    radioButtonSE -> sp.play(radioButtonSE,1.0f,1.0f,1,0,1.0f)
                    gameStartSE -> sp.play(gameStartSE, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }
        }

        val Radio = findViewById<RadioGroup>(R.id.RadioGroup)
        when(playFirst){
            1 -> {findViewById<RadioButton>(R.id.Button1p).isChecked = true}
            -1 -> {findViewById<RadioButton>(R.id.Button2p).isChecked = true}
            0 -> {findViewById<RadioButton>(R.id.ButtonRandom).isChecked = true}
        }

        Radio.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.Button1p->{playFirst=1}
                R.id.Button2p->{playFirst= -1}
                R.id.ButtonRandom->{playFirst=0}
            }
            playSound(radioButtonSE)
            val editor=pref.edit()
            editor.putInt("playFirst",playFirst).apply()
            Log.d("gobblet2", "${playFirst}")
        }

        backButton.setOnClickListener {
            playSound(cancelSE)
            val intent = Intent(this,MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        gameStartBtn.setOnClickListener {
            playSound(gameStartSE)
            val intent = Intent(this, GameWithManActivity::class.java)
            startActivity(intent)
        }


    }

    //pxをdpに変換
    fun converPxToDp(px: Int): Float {
        return px / this.getResources().getDisplayMetrics().density
    }


    override fun onDestroy() {
        super.onDestroy()
        sp.release()
    }
}