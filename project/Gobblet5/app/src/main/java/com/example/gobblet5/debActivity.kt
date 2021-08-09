package com.example.gobblet5


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log


class debActivity : AppCompatActivity() {
    val millisecond:Long=100
    var time = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deb)

    }

    fun startHowToPlay2Timer(){
        val handler = Handler()
        val timer = object :Runnable{
            override fun run() {
                time += millisecond
                handler.postDelayed(this,millisecond)
                Log.d("gobbl et2", "timer_def:${time}")
                if (time==3000L){
                    handler.removeCallbacks(this)
                    time = 0L
                }
            }
        }
        handler.post(timer)
    }


}