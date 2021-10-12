package com.example.gobblet5

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.stream.Collectors
import java.util.stream.Stream

class Com {
    private var temochiBig:Temochi? = null
    private var temochiMiddle:Temochi? = null
    private var temochiSmall:Temochi? = null
    private var line1 = mutableListOf<Mas>()
    private var line2 = mutableListOf<Mas>()
    private var line3 = mutableListOf<Mas>()
    private var line4 = mutableListOf<Mas>()
    private var lineAllAtOnce = mutableListOf<Mas>() //すべてのマスクラスに対して色々やる時に使うリスト
    private var scoreA1 = 0
    private var scoreA2 = 0
    private var scoreA3 = 0
    private var scoreA4 = 0
    private var scoreB1 = 0
    private var scoreB2 = 0
    private var scoreB3 = 0
    private var scoreB4 = 0
    private var scoreC1 = 0
    private var scoreC2 = 0
    private var scoreC3 = 0
    private var scoreC4 = 0
    private var scoreD1 = 0
    private var scoreD2 = 0
    private var scoreD3 = 0
    private var scoreD4 = 0


    fun iniLines(line1:MutableList<Mas>,line2:MutableList<Mas>,line3:MutableList<Mas>,line4:MutableList<Mas>){
        this.line1 = line1
        this.line2 = line2
        this.line3 = line3
        this.line4 = line4
        iniConcatLine()
//        Log.d("Gobblet2Com","list1[0]:${line1[0].nameGetter()}")
        deb()
        checkEmptyMas()
    }
    
    fun deb(){
        Log.d("Gobblet2Com","list1:{${line1[0].funcForDisplay()},${line1[1].funcForDisplay()},${line1[2].funcForDisplay()},${line1[3].funcForDisplay()}}")
        Log.d("Gobblet2Com","list2:{${line2[0].funcForDisplay()},${line2[1].funcForDisplay()},${line2[2].funcForDisplay()},${line2[3].funcForDisplay()}}")
        Log.d("Gobblet2Com","list1:{${line3[0].funcForDisplay()},${line3[1].funcForDisplay()},${line3[2].funcForDisplay()},${line3[3].funcForDisplay()}}")
        Log.d("Gobblet2Com","list1:{${line4[0].funcForDisplay()},${line4[1].funcForDisplay()},${line4[2].funcForDisplay()},${line4[3].funcForDisplay()}}")
    }


    fun checkEmptyMas(){
        Log.d("Gobblet2Com","concatLine:${lineAllAtOnce}")
        for (L in lineAllAtOnce){
            if (L.returnLastElement() == 0){
                L.scoreAdd(10)
            }
        }
        debScore()
    }
    
    fun debScore(){
        Log.d("Gobblet2Com","list1:{${line1[0].scoreGetter()},${line1[1].scoreGetter()},${line1[2].scoreGetter()},${line1[3].scoreGetter()}}")
        Log.d("Gobblet2Com","list2:{${line2[0].scoreGetter()},${line2[1].scoreGetter()},${line2[2].scoreGetter()},${line2[3].scoreGetter()}}")
        Log.d("Gobblet2Com","list1:{${line3[0].scoreGetter()},${line3[1].scoreGetter()},${line3[2].scoreGetter()},${line3[3].scoreGetter()}}")
        Log.d("Gobblet2Com","list1:{${line4[0].scoreGetter()},${line4[1].scoreGetter()},${line4[2].scoreGetter()},${line4[3].scoreGetter()}}")
    }

    fun resetScore(){
        //すべてのマスクラスの評価値を0にする


    }

    fun iniConcatLine(){ //一旦関数にしないとエラーになるので関数化
        lineAllAtOnce.addAll(line1)
        lineAllAtOnce.addAll(line2)
        lineAllAtOnce.addAll(line3)
        lineAllAtOnce.addAll(line4)
    }
}