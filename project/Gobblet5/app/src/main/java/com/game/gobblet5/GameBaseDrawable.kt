package com.game.gobblet5

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.Drawable

class GameBaseDrawable {
    //表示に使う物　(箱を用意している状態)
    private var res: Resources? = null
    //マス
     var masImag: Drawable? = null
    //駒
    //赤
     var komaRedBigD: Drawable? = null
     var komaRedMiddleD: Drawable? = null
     var komaRedSmallD: Drawable? = null
    //緑
     var komaGreenBigD: Drawable? = null
     var komaGreenMiddleD: Drawable? = null
     var komaGreenSmallD: Drawable? = null
    
    @SuppressLint("UseCompatLoadingForDrawables")
    fun ini(arg:Resources){
        res=arg
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
    }
    
}