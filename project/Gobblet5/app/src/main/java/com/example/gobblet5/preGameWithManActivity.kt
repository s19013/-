package com.example.gobblet5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_pre_game_with_man.*


class preGameWithManActivity : BaseClass() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game_with_man)

        var playFirst=pref?.getInt("playFirst", 1)

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
            val editor=pref!!.edit()
            editor.putInt("playFirst",playFirst!!).commit()
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
}