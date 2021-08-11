package com.example.gobblet5


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.activity_deb.*


class debActivity : AppCompatActivity() {
    val millisecond:Long=100
    var time = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deb)



    }

    fun startHowToPlay3Timer(){
        val handler = Handler()
        val timer = object :Runnable{
            override fun run() {
                when(time){
                    1000L -> {
                        val fragment = HowToPlayFragment3_2()
                        val fragmentManager = supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.fragmentBox,fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    2000L -> {
                        val fragment = HowToPlayFragment3_3()
                        val fragmentManager = supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.fragmentBox,fragment)
                            .addToBackStack(null)
                            .commit()
                        }
                    3000L -> {
                        val fragment = HowToPlayFragment3_4()
                        val fragmentManager = supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.fragmentBox,fragment)
                            .addToBackStack(null)
                            .commit()
                        }
                    }
                time += millisecond
                handler.postDelayed(this,millisecond)
                Log.d("gobbl et2", "timer_def:${time}")
                if (time==4000L){
                    handler.removeCallbacks(this)
                    time = 0L
                }
            }
        }
        handler.post(timer)
    }


}