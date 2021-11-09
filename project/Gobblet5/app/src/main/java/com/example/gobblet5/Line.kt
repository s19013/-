package com.example.gobblet5

import android.util.Log

class Line(private val name:String) {
    private var list:MutableList<Mas> = mutableListOf()
    private val comPiece = -1
    private val humanPiece = 1

    fun nameGetter():String{
        return name
    }

    fun listSetter(mutableList: MutableList<Mas>){
        this.list = mutableList
    }

    fun listGetter():MutableList<Mas>{
        return list
    }

    //ライン上にあるコンピューターのコマの数を数える
    fun comPieceCounter():Int{
        var counter=0
        for (mas in list){
            if (mas.returnLastElement()==comPiece){
                counter+=1
            }
        }
        return counter
    }

    //ライン上にある人間のコマの数を数える
    fun humanPieceCounter():Int{
        var counter=0
        for (mas in list){
            if (mas.returnLastElement()==humanPiece){
                counter+=1
            }
        }
        return counter
    }

    fun deb(){
        Log.d("gobblet2Com","${name}:${list[0].nameGetter()},${list[1].nameGetter()},${list[2].nameGetter()},${list[3].nameGetter()}")

    }

}