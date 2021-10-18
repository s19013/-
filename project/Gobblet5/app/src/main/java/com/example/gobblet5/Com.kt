package com.example.gobblet5

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import org.jetbrains.annotations.NotNull
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
    private var isFirstTurn = true
    private var temochiBig:Temochi? = null
    private var temochiMiddle:Temochi? = null
    private var temochiSmall:Temochi? = null
    private var line1:Line = Line(stringLine1)
    private var line2:Line = Line(stringLine2)
    private var line3:Line = Line(stringLine3)
    private var line4:Line = Line(stringLine4)
    private var lineA:Line = Line(stringLineA)
    private var lineB:Line = Line(stringLineB)
    private var lineC:Line = Line(stringLineC)
    private var lineD:Line = Line(stringLineD)
    private var lineS:Line = Line(stringLineS)
    private var lineBS:Line = Line(stringLineBS)


    private var doNotMove:MutableList<String> = mutableListOf()
    private var EnemyReachList:MutableList<String> = mutableListOf()
    private var ComReachList:MutableList<String> = mutableListOf()
    private var bord:MutableList<MutableList<Mas>> = mutableListOf() //[縦列][横列]　例:B3 -> [2][1]
    private var lineAllAtOnce:MutableList<Mas> = mutableListOf() //すべてのマスクラスに対して色々やる時に使うリスト
    private var judgeList:MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0)
    private var nameList:MutableList<String> = mutableListOf(stringLine1,stringLine2,stringLine3,stringLine4,
        stringLineA,stringLineB,stringLineC,stringLineD,stringLineS,stringLineBS)

    //マスが空かどうかしらべる
    fun checkEmptyMas() {
        fun commonFunc(line: Line){
            for (L in line.list) {
                if (L.returnLastElement() == 0) {
                    L.addScore(10) //マスの中が空だったら評価値10を加える
                }
            }
        }
        commonFunc(line1)
        commonFunc(line2)
        commonFunc(line3)
        commonFunc(line4)
    }

    //各マスに何が入っているのかしらべて評価値をつける
    fun checkWhatIsInTheMas(){
        fun commonFunc(line: Line){
            for (mas in line.list){
                val rv = mas.funcForDisplay() //帰り値を入れる箱を用意する
                when{
                    rv[0] == 3 -> {mas.addScore(-50)}//相手の大コマか自分の大コマが置かれている
                    rv[0] == 2 && rv[1] ==1 ->{mas.addScore(-38)}//相手の中コマが置かれている
                    rv[0] == 1 && rv[1] ==1 ->{mas.addScore(-19)}//相手の小コマが置かれている
                    rv[0] == 2 && rv[1] ==-1 ->{mas.addScore(-8)}//自分の中コマが置かれている
                    rv[0] == 1 && rv[1] ==-1 ->{mas.addScore(-9)}//自分の小コマが置かれている
                }
            }
        }

        commonFunc(line1)
        commonFunc(line2)
        commonFunc(line3)
        commonFunc(line4)
    }

    //リーチなった列がないか調べる
    fun reachChecker(){
        fun counter(line: Line){
            var countM1 = 0
            var countP1 = 0
            for (i in line.list){ // -1,1の個数をそれぞれ数える
                when(i.returnLastElement()) {
                    -1 -> {countM1 += 1}
                    1 -> {countP1 += 1}
                }
                if (countP1 >=3){
                    EnemyReachList.add(line.nameGetter())
                    return
                } //1が3つ以上なら敵がリーチ
                if (countM1 >=3){
                    ComReachList.add(line.nameGetter())
                    return
                } //-1 が3つ以上ならcomがリーチ
            }
        }
        counter(line1)
        counter(line2)
        counter(line3)
        counter(line4)
        counter(lineA)
        counter(lineB)
        counter(lineC)
        counter(lineD)
        counter(lineS)
        counter(lineBS)
    }

    //コンピューターにリーチがかかってないか調べる(止めをさせる場所を探す)
    fun checkmate(){
        //最後の決めてとなる場所を探す,そしてそこに入れられるかを探す
        fun commonFunc(line:Line){
            var finalTarget:Mas? = null
            //ここからどのマスがまだ自分のマスでないかを教える?
            for (i in line.list){
                if (i.returnLastElement() != -1){
                    finalTarget=i
                    //あとは大きさがわかれば良い
                    break
                }
            }

            //コマをおけば勝てるところに相手の大きいコマがおいてないか調べる
            if (finalTarget != null){
                when(howBigEnemysPiece(finalTarget)){
                    3 -> {finalTarget.addScore(-100)}//諦めること指す
                    else -> {finalTarget.addScore(200)}//評価値を入れる
                }
            }
            Log.d("gobblet2Com","finalTarget:${finalTarget?.nameGetter()}")
        }
        
        for (value in ComReachList){
            when(value){
                 stringLine1 -> {commonFunc(line1)}
                 stringLine2 -> {commonFunc(line2)}
                 stringLine3 -> {commonFunc(line3)}
                 stringLine4 -> {commonFunc(line4)}
                 stringLineA -> {commonFunc(lineA)}
                 stringLineB -> {commonFunc(lineB)}
                 stringLineC -> {commonFunc(lineC)}
                 stringLineD -> {commonFunc(lineD)}
                 stringLineS -> {commonFunc(lineS)}
                stringLineBS -> {commonFunc(lineBS)}
            }
        }
    }

    //敵のコマの大きさを調べる
    fun howBigEnemysPiece(mas:Mas):Int{
        val rv = mas.funcForDisplay()
        return rv[0]
    }

    //人間にリーチがかかってないか調べる(相手の勝利を阻止する)
    fun blockCheckmate(){
        ////どこに入れれば防げるか探す
        fun commonFunc(line:Line){
            var target:Mas? = null
            //ここからどのマスがまだ自分のマスでないかを教える?
            for (i in line.list){
                if (i.returnLastElement() != 1){
                    target=i
                    //あとは大きさがわかれば良い
                    break
                }
            }

            if (target != null){//コマをおけば勝てるところに相手の大きいコマがおいてないか調べる
                when(howBigEnemysPiece(target)){
                    3 -> {target.addScore(-100)}//諦めること指す
                    2 -> {target.addScore(82)}//中くらいのコマ
                    1 -> {target.addScore(81)}//小さいコマ
                    0 -> {target.addScore(100)}//何もおいていない
                }
            }
            Log.d("gobblet2Com","blocktarget:${target?.nameGetter()}")
        }
        for (value in EnemyReachList){
            when(value){
                stringLine1 -> {commonFunc(line1)}
                stringLine2 -> {commonFunc(line2)}
                stringLine3 -> {commonFunc(line3)}
                stringLine4 -> {commonFunc(line4)}
                stringLineA -> {commonFunc(lineA)}
                stringLineB -> {commonFunc(lineB)}
                stringLineC -> {commonFunc(lineC)}
                stringLineD -> {commonFunc(lineD)}
                stringLineS -> {commonFunc(lineS)}
                stringLineBS -> {commonFunc(lineBS)}
            }
        }
    }

    //最初のターンの定石を実行
    fun firstTurn(){
        val randomNum = (0..3).random()
        when(randomNum){
            0 ->{} //b2に置く
            1 ->{} //b3に置く
            2 ->{} //c2に置く
            3 ->{} //c3に置く
        }
    }

    //大きさを決定する
    fun ChooseASize(){

    }

    //場所を決める
    fun ChooseAlocation(){

    }

    //一番評価値が大きい場所を選ぶ
    fun biggestScore(){
        var selected :Mas? = line1.list[0] //適当にA1をデフォルトとする
        var biggestScore = 0

        fun commonFunc(line:Line){
            for (mas in line.list){
                if (mas.scoreGetter() > biggestScore){
                    biggestScore = mas.scoreGetter()
                    selected = mas
                }
            }
        }

        commonFunc(line1)
        commonFunc(line2)
        commonFunc(line3)
        commonFunc(line4)

    }

    fun judge(){
        for(i in 0..3){
            judgeList[0] += bord[0][i].returnLastElement()
        }
        for(i in 0..3){
            judgeList[1] += bord[1][i].returnLastElement()
        }
        for(i in 0..3){
            judgeList[2] += bord[2][i].returnLastElement()
        }
        for(i in 0..3){
            judgeList[3] += bord[3][i].returnLastElement()
        }
        for(i in 0..3){
            judgeList[4] += bord[i][0].returnLastElement()
        }
        for(i in 0..3){
            judgeList[5] += bord[i][1].returnLastElement()
        }
        for(i in 0..3){
            judgeList[6] += bord[i][2].returnLastElement()
        }
        for(i in 0..3){
            judgeList[7] += bord[i][3].returnLastElement()
        }
        for(i in 0..3){
            judgeList[8] += bord[i][3-i].returnLastElement()
        }
        for(i in 0..3){
            judgeList[9] += bord[i][i].returnLastElement()
        }
    }

    fun resetScore(){
        //すべてのマスクラスの評価値を0にする
        for (l in line1.list){
            l.resetScore()
        }
        for (l in line2.list){
            l.resetScore()
        }
        for (l in line3.list){
            l.resetScore()
        }
        for (l in line4.list){
            l.resetScore()
        }

    }

    fun resetLists(){
        for (i in 0..9){
            judgeList[i] = 0
        }
        EnemyReachList.clear()
        ComReachList.clear()
    }

    fun iniConcatLine(){ //一旦関数にしないとエラーになるので関数化
//        lineAllAtOnce.addAll(line1)
//        lineAllAtOnce.addAll(line2)
//        lineAllAtOnce.addAll(line3)
//        lineAllAtOnce.addAll(line4)

        //
        bord.add(line1.list)
        bord.add(line2.list)
        bord.add(line3.list)
        bord.add(line4.list)
    }

    fun iniLines(line1:MutableList<Mas>,line2:MutableList<Mas>,line3:MutableList<Mas>,line4:MutableList<Mas>,
                 lineA:MutableList<Mas>,lineB:MutableList<Mas>,lineC:MutableList<Mas>,lineD:MutableList<Mas>,
                 lineS:MutableList<Mas>,lineBS:MutableList<Mas>){
        this.line1.listSetter(line1)
        this.line2.listSetter(line2)
        this.line3.listSetter(line3)
        this.line4.listSetter(line4)
        this.lineA.listSetter(lineA)
        this.lineB.listSetter(lineB)
        this.lineC.listSetter(lineC)
        this.lineD.listSetter(lineD)
        this.lineS.listSetter(lineS)
        this.lineBS.listSetter(lineBS)
    }

    fun deb(){
        Log.d("gobblet2Com","list1:{${bord[0][0].funcForDisplay()},${bord[0][1].funcForDisplay()},${bord[0][2].funcForDisplay()},${bord[0][0].funcForDisplay()}}")
        Log.d("gobblet2Com","list2:{${bord[1][0].funcForDisplay()},${bord[1][1].funcForDisplay()},${bord[1][2].funcForDisplay()},${bord[1][3].funcForDisplay()}}")
        Log.d("gobblet2Com","list3:{${bord[2][0].funcForDisplay()},${bord[2][1].funcForDisplay()},${bord[2][2].funcForDisplay()},${bord[2][3].funcForDisplay()}}")
        Log.d("gobblet2Com","list4:{${bord[3][0].funcForDisplay()},${bord[3][1].funcForDisplay()},${bord[3][2].funcForDisplay()},${bord[3][1].funcForDisplay()}}")
        Log.d("gobblet2Com"," ")
    }

    fun debScore(){
        Log.d("gobblet2Com","list1:{${bord[0][0].scoreGetter()},${bord[0][1].scoreGetter()},${bord[0][2].scoreGetter()},${bord[0][3].scoreGetter()}}")
        Log.d("gobblet2Com","list2:{${bord[1][0].scoreGetter()},${bord[1][1].scoreGetter()},${bord[1][2].scoreGetter()},${bord[1][3].scoreGetter()}}")
        Log.d("gobblet2Com","list3:{${bord[2][0].scoreGetter()},${bord[2][1].scoreGetter()},${bord[2][2].scoreGetter()},${bord[2][3].scoreGetter()}}")
        Log.d("gobblet2Com","list4:{${bord[3][0].scoreGetter()},${bord[3][1].scoreGetter()},${bord[3][2].scoreGetter()},${bord[3][3].scoreGetter()}}")
        Log.d("gobblet2Com"," ")
        Log.d("gobblet2Com","judgeList:${judgeList}")
        Log.d("gobblet2Com","ComReachList:${ComReachList}")
        Log.d("gobblet2Com","EnemyReachList:${EnemyReachList}")
        Log.d("gobblet2Com"," ")
    }

    fun debBord(){
        Log.d("gobblet2Com","[${bord[0][0].nameGetter()},${bord[0][1].nameGetter()},${bord[0][2].nameGetter()},${bord[0][3].nameGetter()}]")
        Log.d("gobblet2Com","[${bord[1][0].nameGetter()},${bord[1][1].nameGetter()},${bord[1][2].nameGetter()},${bord[1][3].nameGetter()}]")
        Log.d("gobblet2Com","[${bord[2][0].nameGetter()},${bord[2][1].nameGetter()},${bord[2][2].nameGetter()},${bord[2][3].nameGetter()}]")
        Log.d("gobblet2Com","[${bord[3][0].nameGetter()},${bord[3][1].nameGetter()},${bord[3][2].nameGetter()},${bord[3][3].nameGetter()}]")

    }

}