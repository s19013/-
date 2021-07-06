package com.example.gobblet5

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.media.AudioAttributes
import android.os.AsyncTask
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService


class CustomToast : AsyncTask<String?, Int?, Int>(), Runnable {
    private var toast: Toast? = null
    private var duration: Long = 0
    private val handler = Handler()

    var width =0
    var height = 0


    fun setWH(w:Int,h:Int){
        width = w
        height = h
    }


    fun show() {
        if (duration > 2000) {
            var i = 0
            while (i < duration - 2000) {
                handler.postDelayed(this, i.toLong())
                i += 2000
            }
            handler.postDelayed(this, duration - 2000)
        } else {
            this.execute()
        }
    }



    override fun run() {
        toast!!.show()
    }

    protected override fun doInBackground(vararg params: String?): Int? {
        try {
            Thread.sleep(duration)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return 0
    }

    override fun onPreExecute() {
//        toast!!.setGravity(Gravity.CENTER,width/2,height/2)
        toast!!.show()
    }

    override fun onPostExecute(i: Int) {
        toast!!.cancel()
    }

    companion object {
        fun makeText(context: Context?, resId: Int, duration: Long,w:Int,h:Int): CustomToast {
            val ct = CustomToast()
            ct.setWH(w, h)
            ct.toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT)
            ct.duration = duration
            return ct
        }



        fun makeText(context: Context?, text: CharSequence?, duration: Long,w:Int,h:Int): CustomToast {
            val ct = CustomToast()
            ct.setWH(w,h)
            ct.toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
            val audioAttributes = if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q) {
                ct.toast!!.setGravity(Gravity.CENTER,DisplayMetrics().widthPixels/2, DisplayMetrics().heightPixels/2)
            } else { }
            ct.duration = duration
            return ct
        }

    }
}