package com.example.gobblet5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_config.*

class ConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        backButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        //共有プリファレンス
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        var SE =pref.getBoolean("SEOnOff", true)
        var BGM =pref.getBoolean("BGMOnOff", true)

        val RadioSE = findViewById<RadioGroup>(R.id.SEOnOff)
        when(SE){
            true -> {findViewById<RadioButton>(R.id.SEOn).isChecked = true}
            false -> {findViewById<RadioButton>(R.id.SEOff).isChecked = true}
        }

        val RadioBGM = findViewById<RadioGroup>(R.id.BGMOnOff)
        when(BGM){
            true -> {findViewById<RadioButton>(R.id.BGMOn).isChecked = true}
            false -> {findViewById<RadioButton>(R.id.BGMOff).isChecked = true}
        }


        // 関数設定
        RadioSE.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.SEOn->{SE=true}
                R.id.SEOff->{SE=false}
            }
            val editor=pref.edit()
            editor.putBoolean("SEOnOff",SE).apply()
            Log.d("gobblet2", "SE=${SE}")
        }

        RadioBGM.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.BGMOn->{BGM=true}
                R.id.BGMOff->{BGM=false}
            }
            val editor=pref.edit()
            editor.putBoolean("BGMOnOff",BGM).apply()
            Log.d("gobblet2", "BGM=${BGM}")
        }

    }
}