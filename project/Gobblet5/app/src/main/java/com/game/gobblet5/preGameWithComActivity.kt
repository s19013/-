package com.game.gobblet5

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.activity_pre_game_with_man.*

class preGameWithComActivity : BaseClass() {
    var radio:RadioGroup? =null
    var playFirst:Int?= null
    private lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game_with_com)

        iniPlayFirst()
        iniRadioButtons()
        iniAD()

        backButton.setOnClickListener {
            sound.playSound(sound.cancelSE,save.seVolume)
            val intent = Intent(this,MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        gameStartBtn.setOnClickListener {
            sound.playSound(sound.gameStartSE,save.seVolume)
            val intent = Intent(this, GameWithComActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniPlayFirst(){
        playFirst=save.playFirst
        when(playFirst){
            1 -> {findViewById<RadioButton>(R.id.Button1p).isChecked = true}
            -1 -> {findViewById<RadioButton>(R.id.Button2p).isChecked = true}
            0 -> {findViewById<RadioButton>(R.id.ButtonRandom).isChecked = true}
        }
    }

    private fun iniRadioButtons(){
        radio = findViewById<RadioGroup>(R.id.RadioGroup)
        radio!!.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.Button1p->{playFirst=1}
                R.id.Button2p->{playFirst= -1}
                R.id.ButtonRandom->{playFirst=0}
            }
            sound.playSound(sound.radioSE,save.seVolume)
            val editor= save.pref!!.edit()
            editor.putInt("playFirst",playFirst!!)
            editor.apply()
        }
    }

    private fun iniAD(){
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }
}