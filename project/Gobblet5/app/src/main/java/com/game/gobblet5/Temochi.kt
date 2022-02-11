package com.game.gobblet5


import android.widget.ImageView
import android.widget.TextView

class Temochi(val size:Int,private val name:String) {
    private var count:Int=3

    private var textView: TextView? = null //テキスト描画する場所
    private var buttonView: ImageView? = null //テキスト描画する場所

    fun setTextView(v:TextView){ textView = v}
    fun getTextView():TextView?{ return textView }

    fun setButtonView(v:ImageView){ buttonView = v}
    fun getButtonView():ImageView?{return buttonView}

    fun nameGetter():String{ return name }

    fun sizeGetter():Int{return size}


    //使ったから1つ減らす
    fun usePiece(){ count-=1 }

    fun plus(){
        if (count<3){ count+=1 }
    }


    //まだ使えるか教える
    fun returnInf():Int{
        if (count>0){ return size }
        return 0
    }

    fun returnCount():Int{ return count }


}