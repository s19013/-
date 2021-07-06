package com.example.gobblet5

import android.util.Log


class mas(val name:String) {
    var list= mutableListOf<Int>(0,0,0)

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

    fun resetList(size: Int){
        list[size-1]=0
    }

    fun returnName():String{
        return name
    }

    fun returnLastElement():Int{
        for (i in 2 downTo 0){
            if (list[i]!=0){ return list[i] }
        }
        return 0
    }

    fun funcForDisplay():MutableList<Int>{
        for (i in 2 downTo 0){
            if (list[i]!=0){
                return mutableListOf(i+1,list[i])
            }
        }
        return mutableListOf(0,0)
    }

    //    fun debugDisplay():MutableList<Int>{
//        return list
//    }
    fun debugDisplay(){
        Log.d("gobblet2","list:${list}")
    }
}