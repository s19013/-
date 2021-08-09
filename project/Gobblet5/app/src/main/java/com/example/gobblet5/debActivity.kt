package com.example.gobblet5


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.activity_deb.*
import kotlinx.android.synthetic.main.fragment_how_to_play2.*


class debActivity : AppCompatActivity() {
    val millisecond:Long=100
    var time = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deb)

        startHowToPlay2Timer()
    }

    fun startHowToPlay2Timer(){
        val handler = Handler()
        val timer = object :Runnable{
            override fun run() {
                when(time){
                    1000L -> {
                        Log.d("gobblet2", "call")
                        val fragment = HowToPlayFragment2()
                        val fragmentManager = supportFragmentManager
                        val fragmentTransaction  = fragmentManager.beginTransaction()
                        fragment.visiblSmall()
                        fragmentTransaction.replace(R.id.fragmentBox,fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
                time += millisecond
                handler.postDelayed(this,millisecond)
                Log.d("gobblet2", "timer_def:${time}")
                if (time==3000L){
                    handler.removeCallbacks(this)
                    time = 0L
                }
            }
        }
        handler.post(timer)
    }


}