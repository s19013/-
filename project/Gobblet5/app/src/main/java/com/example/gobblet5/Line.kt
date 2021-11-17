package com.example.gobblet5

import android.util.Log

class Line(private val name:String) {
    private var list:MutableList<Mas> = mutableListOf()
    private val comPiece=-1
    private val humanPiece = 1
    private val empty = 0

    private val bigPiece=3
    private val middlePiece=2
    private val smallPiece=1

    fun nameGetter():String{ return name }

    fun listSetter(mutableList: MutableList<Mas>){ this.list = mutableList }

    fun listGetter():MutableList<Mas>{ return list }

    //ライン上にあるコンピューターのコマの数を数える
    fun comPieceCounter():Int{
        var counter=0
        for (mas in list){
            if (mas.returnLastElement()==comPiece){ counter+=1 }
        }
        return counter
    }

    //ライン上にある人間のコマの数を数える
    fun humanPieceCounter():Int{
        var counter=0
        for (mas in list){
            if (mas.returnLastElement()==humanPiece){ counter+=1 }
        }

        Log.d("gobblet2Com","${name} contains ${counter}humanPieace")
        return counter
    }

    //同じライン上で自分の大きいコマを3つ使っているか?
    fun use3BigPieceOnTheLine():Boolean{
        //true :3つあった
        //false:それ以外
        var counter = 0
        for (mas in list){
            if (mas.funcForDisplay()[1] == comPiece && mas.funcForDisplay()[0] == bigPiece){ counter+=1 }
            //マスに自分の大きいコマが入っていたら+1
        }
        if (counter == 3){return true}
        return false
    }

    //同じライン上で自分の中コマを3つ使っているか?
    fun use3MiddlePieceOnTheLine():Boolean{
        //true :3つあった
        //false:それ以外
        var counter = 0
        for (mas in list){
            if (mas.funcForDisplay()[1] == comPiece && mas.funcForDisplay()[0] == middlePiece){
                //マスに自分の中コマが入っていたら+1
                counter+=1
            }
        }
        if (counter == 3){return true}
        return false
    }

    //同じライン上で自分の小コマを3つ使っているか?
    fun use3SmallPieceOnTheLine():Boolean{
        //true :3つあった
        //false:それ以外
        var counter = 0
        for (mas in list){
            if (mas.funcForDisplay()[1] == comPiece && mas.funcForDisplay()[0] == smallPiece){
                //マスに自分の小さいコマが入っていたら+1
                counter+=1
            }
        }
        if (counter == 3){return true}
        return false
    }

    fun deb(){
        Log.d("gobblet2Com","${name}:${list[0].nameGetter()},${list[1].nameGetter()},${list[2].nameGetter()},${list[3].nameGetter()}")

    }

}