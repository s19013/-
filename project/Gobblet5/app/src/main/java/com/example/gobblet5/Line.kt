package com.example.gobblet5

import android.util.Log

class Line(private val name:String) {
    private var list:MutableList<Mas> = mutableListOf()

    fun nameGetter():String{
        return name
    }

    fun listSetter(mutableList: MutableList<Mas>){
        this.list = mutableList
    }

    fun listGetter():MutableList<Mas>{
        return list
    }

    fun deb(){
        Log.d("gobblet2Com","${name}:${list[0].nameGetter()},${list[1].nameGetter()},${list[2].nameGetter()},${list[3].nameGetter()}")

    }

}