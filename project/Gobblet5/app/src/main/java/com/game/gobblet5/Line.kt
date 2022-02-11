package com.game.gobblet5

import android.util.Log

class Line(private val name:String) {
    private var list:MutableList<Mas> = mutableListOf()
    private val p1Piece =  1 //1pのコマ
    private val p2Piece = -1 //2pのコマ


    private val bigPiece    =3 //
    private val middlePiece =2 //
    private val smallPiece  =1 //
    
    private var countedP1Piece = 0 //ラインに入っているcomのコマの数
    private var countedP2Piece = 0 //ラインに入っているのコマの数

    private var differenceScore = 0 //ラインの守るスコア
    private var offenceScore    = 0 //ラインの攻めるスコア

    //ラインを更新
    fun listSetter(mutableList: MutableList<Mas>){ this.list = mutableList }

    //ラインを取り出す
    fun listGetter():MutableList<Mas>{ return list }

    //ライン上にあるコンピューターのコマの数を数える
    fun comPieceCounter():Int{
        var counter=0
        for (mas in list){
            if (mas.returnLastElement()==p2Piece){ counter+=1 }
        }
        return counter
    }

    //ライン上にある人間のコマの数を数える
    fun humanPieceCounter():Int{
        var counter=0
        for (mas in list){
            if (mas.returnLastElement()==p1Piece){ counter+=1 }
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
            if (mas.funcForDisplay()[1] == p2Piece && mas.funcForDisplay()[0] == bigPiece){ counter+=1 }
            //マスに自分の大きいコマが入っていたら+1
        }
        if (counter == 3){return true}
        return false
    }

    //自分､もしくは相手のコマが4つ揃っているか
    fun judge():Int{
        var rv = 0
        for (m in list){ rv += m.funcForDisplay()[1] }
        return rv
    }

    //リスト内のコマを数える
    fun insideCounter(){
        for (mas in list){
            when(mas.funcForDisplay()[1]){
                 1 -> countedP1Piece+=1
                -1 -> countedP2Piece+=1
            }
        }
    }

    //カウントに使った変数をリセット
    fun resetCounted(){
        countedP1Piece = 0
        countedP2Piece = 0
    }




}