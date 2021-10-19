package com.example.gobblet5

import android.util.Log


class Mas(private val name:String, private val myVerticalValue: Int, private val myHorizontalValue: Int) {
    var list= mutableListOf<Int>(0,0,0) // [小､中､大]
    var score = 0 //コンピューターが使う評価値みたいなもの

    fun nameGetter():String{
        return name
    }

    fun scoreGetter():Int{
        return score
    }

    fun addScore(arg:Int){ //スコアに引数の値を足す
        score+=arg
    }

    fun mPickup(turn:Int):Int{
        when(list.lastIndexOf(turn)){
            0-> if (list[1]==0 && list[2]==0){ return 1}
            1-> if (list[2]==0){return 2}
            2-> return 3
            else-> return 0
        }
        return 0
    }

    fun mInsert(size:Int,turn: Int):Boolean{
        when(size){
            3->{
                if (list[2]==0) {list[2]=turn}
                else return false
            }
            2->{
                if (list[2] == 0 && list[1] == 0){ list[1]=turn }
                else return false
            }
            1->{
                if (list[2] == 0 && list[1] == 0 && list[0] == 0) {list[0]=turn }
                else return false
            }
        }
        return true
    }

    fun returnLastElement():Int{
        for (i in 2 downTo 0){
            //後ろからコマがあるかどうか調べる
            //とにかくこのマスの中にコマがあるか
            if (list[i]!=0){ return list[i] }
        }
        return 0 //マスの中に何もなかったら0と返す
    }

    fun resetList(size: Int){ //取り出して空にする
        list[size-1]=0
    }

    fun resetScore(){ //スコアを0に戻す
        score = 0
    }

    fun funcForDisplay():MutableList<Int>{
        for (i in 2 downTo 0){
            if (list[i]!=0){
                return mutableListOf(i+1,list[i]) //[大きさ､1pのか2pのか]
            } //大きさ 小:1 中:2 大:3
        } //1p:1 2p:-1
        return mutableListOf(0,0)
    }

    //    fun debugDisplay():MutableList<Int>{
//        return list
//    }
    fun debugDisplay(){
        Log.d("gobblet2","list:[${list[0]},${list[1]},${list[2]}]")
    }
}