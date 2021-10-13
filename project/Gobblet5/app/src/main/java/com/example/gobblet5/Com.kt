package com.example.gobblet5

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.stream.Collectors
import java.util.stream.Stream

class Com {
    //マジックナンバー防止
    private val stringLineA="LA"
    private val stringLineB="LB"
    private val stringLineC="LC"
    private val stringLineD="LD"
    private val stringLine1="L1"
    private val stringLine2="L2"
    private val stringLine3="L3"
    private val stringLine4="L4"
    private val stringLineS="LS"
    private val stringLineBS="LBS"

    //
    private var temochiBig:Temochi? = null
    private var temochiMiddle:Temochi? = null
    private var temochiSmall:Temochi? = null
    private var line1:MutableList<Mas> = mutableListOf()
    private var line2:MutableList<Mas> = mutableListOf()
    private var line3:MutableList<Mas> = mutableListOf()
    private var line4:MutableList<Mas> = mutableListOf()
    private var EnemyReach:MutableList<String> = mutableListOf()
    private var ComReach:MutableList<String> = mutableListOf()
    private var bord:MutableList<MutableList<Mas>> = mutableListOf() //[縦列][横列]　例:B3 -> [3][2]
    private var lineAllAtOnce:MutableList<Mas> = mutableListOf() //すべてのマスクラスに対して色々やる時に使うリスト
    private var judgeList:MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0)
    private var nameList:MutableList<String> = mutableListOf(stringLineA,stringLineB,stringLineC,stringLineD,
        stringLine1,stringLine2,stringLine3,stringLine4,stringLineS,stringLineBS)
//    private var judgeList:MutableList<ScoreOfLine> = mutableListOf(
//        ScoreOfLine("LA"),ScoreOfLine("LB"),ScoreOfLine("LC"),ScoreOfLine("LD"),
//        ScoreOfLine("L1"),ScoreOfLine("L2"),ScoreOfLine("L3"),ScoreOfLine("L4"),
//        ScoreOfLine("LS"),ScoreOfLine("LBS")
//    )
    //これでリーチになりそうなものをしらべる?(マップは使えないのでリストを使う)




    fun iniLines(line1:MutableList<Mas>,line2:MutableList<Mas>,line3:MutableList<Mas>,line4:MutableList<Mas>){
        this.line1 = line1
        this.line2 = line2
        this.line3 = line3
        this.line4 = line4
        iniConcatLine()
//        Log.d("Gobblet2Com","list1[0]:${line1[0].nameGetter()}")
    }


    //マスが空かどうかしらべる
    fun checkEmptyMas(){
        for (L in lineAllAtOnce){
            if (L.returnLastElement() == 0){
                L.scoreAdd(10) //マスの中が空だったら評価値10を加える
            }
        }
    }

    //各マスに何が入っているのかしらべて評価値をつける
    fun checkWhatIsInTheMas(){
        for (L in lineAllAtOnce){
            if (L.returnLastElement() == 0) {continue}
            val rv= L.funcForDisplay() //帰り値
            when{
                rv[0] == 3 ->{L.scoreAdd(-50)} //相手の大コマか自分の大コマが置かれている
                rv[1] == 1 && rv[0] == 2 -> {L.scoreAdd(-32)} //相手の中コマが置かれている
                rv[1] == 1 && rv[0] == 1 -> {L.scoreAdd(-11)} //相手の小コマが置かれている
                rv[1] == -1 && rv[0] == 2 -> {L.scoreAdd(-8)} //自分の中コマが置かれている
                rv[1] == -1 && rv[0] == 1 -> {L.scoreAdd(-9)} //自分の小コマが置かれている
            }
        }
    }

    fun checkEnemyReach(){

    }

    fun checkComReach(){

    }

    fun judge(){
        for(i in 0..3){
            judgeList[0] = bord[0][i].returnLastElement()
        }
        for(i in 0..3){
            judgeList[1] = bord[1][i].returnLastElement()
        }
        for(i in 0..3){
            judgeList[2] = bord[2][i].returnLastElement()
        }
        for(i in 0..3){
            judgeList[3] = bord[3][i].returnLastElement()
        }
        for(i in 0..3){
            judgeList[4] = bord[i][0].returnLastElement()
        }
        for(i in 0..3){
            judgeList[5] = bord[i][1].returnLastElement()
        }
        for(i in 0..3){
            judgeList[6] = bord[i][2].returnLastElement()
        }
        for(i in 0..3){
            judgeList[7] = bord[i][3].returnLastElement()
        }
        for(i in 0..3){
            judgeList[8] = bord[3-i][3-i].returnLastElement()
        }
        for(i in 0..3){
            judgeList[9] = bord[i][i].returnLastElement()
        }
    }

    //

    fun resetScore(){
        //すべてのマスクラスの評価値を0にする
        for (L in lineAllAtOnce){
            L.resetScore()
        }
    }

    fun iniConcatLine(){ //一旦関数にしないとエラーになるので関数化
        lineAllAtOnce.addAll(line1)
        lineAllAtOnce.addAll(line2)
        lineAllAtOnce.addAll(line3)
        lineAllAtOnce.addAll(line4)

        //
        bord.add(line1)
        bord.add(line2)
        bord.add(line3)
        bord.add(line4)
    }

    fun deb(){
        Log.d("Gobblet2Com","list1:{${line1[0].funcForDisplay()},${line1[1].funcForDisplay()},${line1[2].funcForDisplay()},${line1[3].funcForDisplay()}}")
        Log.d("Gobblet2Com","list2:{${line2[0].funcForDisplay()},${line2[1].funcForDisplay()},${line2[2].funcForDisplay()},${line2[3].funcForDisplay()}}")
        Log.d("Gobblet2Com","list3:{${line3[0].funcForDisplay()},${line3[1].funcForDisplay()},${line3[2].funcForDisplay()},${line3[3].funcForDisplay()}}")
        Log.d("Gobblet2Com","list4:{${line4[0].funcForDisplay()},${line4[1].funcForDisplay()},${line4[2].funcForDisplay()},${line4[3].funcForDisplay()}}")
        Log.d("Gobblet2Com","\n")
    }

    fun debScore(){
        Log.d("Gobblet2Com","list1:{${line1[0].scoreGetter()},${line1[1].scoreGetter()},${line1[2].scoreGetter()},${line1[3].scoreGetter()}}")
        Log.d("Gobblet2Com","list2:{${line2[0].scoreGetter()},${line2[1].scoreGetter()},${line2[2].scoreGetter()},${line2[3].scoreGetter()}}")
        Log.d("Gobblet2Com","list3:{${line3[0].scoreGetter()},${line3[1].scoreGetter()},${line3[2].scoreGetter()},${line3[3].scoreGetter()}}")
        Log.d("Gobblet2Com","list4:{${line4[0].scoreGetter()},${line4[1].scoreGetter()},${line4[2].scoreGetter()},${line4[3].scoreGetter()}}")
        Log.d("Gobblet2Com","\n")
        //Log.d("Gobblet2Com","${bord[0][0]}")
    }

}