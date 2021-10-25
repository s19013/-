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
    private var turnCount = 0 //自分のターンが回ってきた回数
    private var isFirstTurn = true
    private var P1Count = 0
    private var temochiBig:Temochi? = null
    private var temochiMiddle:Temochi? = null
    private var temochiSmall:Temochi? = null
    private val line1:Line = Line(stringLine1)
    private val line2:Line = Line(stringLine2)
    private val line3:Line = Line(stringLine3)
    private val line4:Line = Line(stringLine4)
    private val lineA:Line = Line(stringLineA)
    private val lineB:Line = Line(stringLineB)
    private val lineC:Line = Line(stringLineC)
    private val lineD:Line = Line(stringLineD)
    private val lineS:Line = Line(stringLineS)
    private val lineBS:Line = Line(stringLineBS)


    private var doNotMoveList:MutableList<Mas> = mutableListOf() //動かしては行けないコマを管理
    private var unnecessaryList:MutableList<String> = mutableListOf() //うごかしても問題ないコマを管理
    private var candidateList:MutableList<Mas> = mutableListOf() //コマを入れる候補を管理するリスト
    private var enemyReachList:MutableList<String> = mutableListOf() //敵にリーチがかかっているラインを管理するリスト
    private var comReachList:MutableList<String> = mutableListOf() //自分にリーチがかかっているラインを管理するリスト
    private var bord:MutableList<MutableList<Mas>> = mutableListOf() //[縦列][横列]　例:B3 -> [2][1]
    private var lineAllAtOnce:MutableList<Line> = mutableListOf() //すべてのラインクラスに対して色々やる時に使うリスト
    private var judgeList:MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0)
    private var nameList:MutableList<String> = mutableListOf(stringLine1,stringLine2,stringLine3,stringLine4,
        stringLineA,stringLineB,stringLineC,stringLineD,stringLineS,stringLineBS)

    private var debCandidateList= mutableListOf<String>()
    private var debDoNotMoveList= mutableListOf<String>()

//評価値関係?
    //コマの周りを調べる(今は空白の部分だけ)
    fun checkEachMas(){
        for (line in lineAllAtOnce){
            checkVertical(line)
        }
//        var mas:Mas? =null
//        for (y in 0..3){
//            for (x in 0..3){
//                mas=bord[y][x]
//                val rv = mas.funcForDisplay()
//                //まず自分自身のなかが空かどうか調べる
//                if (rv[0] == 3 || rv[1] == -1){continue} //大きいコマか自分のコマが入っていたら飛ばす
//                checkVertical(mas)  //縦
//                checkHorizontal(mas)//横
//
//                //斜めはlineS,lineBSにそのコマが入っている場合のみ調べる
//                if (lineS.listGetter().contains(mas)){checkSlash(mas)} //左からの斜め
//                if (lineBS.listGetter().contains(mas)){checkBackSlash(mas)}//右からの斜め
//            }
//        }
    }

    //うごかしてはいけないコマを探す
    fun whichPieceSholdDonNotMove(){
        //列に自分以外全部敵コマの時は絶対に動かさない
        //敵3,自分1のラインを探す
        //そのラインうち自分のコマがおいてあるマスをリストに入れる
    }

    //動かしても特に問題ないコマを探す
    fun whichPieceIsUnnecessary(){

    }

    //そのマスの縦のラインを調べる
    fun checkVertical(line: Line){
//        Log.d("gobblet2Com","${mas.nameGetter()}からみた縦 bord[${mas.myValueOfYGetter()}][${mas.myValueOfXGetter()}]")
        //まずはどこか調べ
        var standard:Mas? =null
        var thisLine= line.listGetter()

        //基準のマスに自分のコマが入っていた場合
        fun standardIsM1(){
            var countP1=0 //相手のコマの数を数える
            //そのラインの各ますについて調べる
            for (mas in thisLine){
                if (mas == standard) {continue} //基準のマスを調べようとしたらスキップ
                if (mas.returnLastElement() == 1){ countP1 +=1 }
                if (countP1 == 3){doNotMoveList.add(standard!!) }
            }
        }


      //基準のマスに相手のコマが入っていた場合
      fun standardIsP1(){
          for (mas in thisLine){
              val rv = mas.funcForDisplay()
              if (mas == standard) {continue} //基準のマスを調べようとしたらスキップ
              if (standard!!.funcForDisplay()[0] == 3) {continue}

              //周りの各マスを調べて
              //基準にしたマスに評価値を入れる
              when(rv[1]){
                  0 -> {inTheCaseOfEmp(standard!!)}//なにもはいってなかった時
                  -1 -> { inTheCaseOfM1(rv[0],standard!!) }//自分のコマが入っていた場合
                  1 -> { inTheCaseOfP1(rv[0],standard!!) }//相手のコマが入っていた場合
              }
          }
        }

        fun f3(){

        }

        for (i in 0..3){
            standard=line.listGetter()[i]
            //そのラインの中での基準を決める
            //基準はA1,B1,C1と変わって行く

            //基準となったマスに何が入っているかによってすることが違う
            when(standard.funcForDisplay()[1]){
                -1 ->{standardIsM1()}
                1 ->{standardIsP1()}
                0 ->{standardIsP1()}
            }
        }
    //Log.d("gobblet2Com","${bord[y][x].nameGetter()}に緑")
    //Log.d("gobblet2Com","${bord[y][x].nameGetter()}に赤")
    }

    //そのマスの横のラインを調べる
    fun checkHorizontal(mas: Mas){
//        Log.d("gobblet2Com","${mas.nameGetter()}からみた横 bord[${mas.myValueOfYGetter()}][${mas.myValueOfXGetter()}]")
        //まずはどこか調べ
        val y=mas.myValueOfYGetter()//yの値を固定
        val refX=mas.myValueOfXGetter()

        //1つ1つのマスの中を調べる
        for (x in 0..3){
            if (x==refX){continue}//自分自身を調べようとしたらスキップ
            val rv = bord[y][x].funcForDisplay()
            when(rv[1]){
                 0 -> {inTheCaseOfEmp(mas)}//なにもはいってなかった時
                -1 -> { inTheCaseOfM1(rv[0],mas) }//自分のコマが入っていた場合
                 1 -> { inTheCaseOfP1(rv[0],mas) } //相手のコマが入っていた場合
            }
            //                    Log.d("gobblet2Com","${bord[y][x].nameGetter()}に緑")
            //                     Log.d("gobblet2Com","${bord[y][x].nameGetter()}に赤")
        }
    }

    //そのマスの左斜めのラインを調べる
    fun checkSlash(mas: Mas){
//        Log.d("gobblet2Com","${mas.nameGetter()}からみた左斜め bord[${mas.myValueOfYGetter()}][${mas.myValueOfXGetter()}]")
        val refX=mas.myValueOfYGetter()
        for (n in 0..3){
            if (n==refX){continue}//自分自身を調べようとしたらスキップ
            val rv = bord[n][n].funcForDisplay()
            
            //1つ1つのマスの中を調べる
            when(rv[1]){
                 0 -> {inTheCaseOfEmp(mas)}//なにもはいってなかった時
                -1 -> { inTheCaseOfM1(rv[0],mas) } //自分のコマが入っていた場合
                 1 -> { inTheCaseOfP1(rv[0],mas) } //相手のコマが入っていた場合
            }
//                    Log.d("gobblet2Com","${bord[n][n].nameGetter()}に緑")
//                     Log.d("gobblet2Com","${bord[n][n].nameGetter()}に赤")
        }
    }

    //そのマスの右斜めのラインを調べる
    fun checkBackSlash(mas:Mas){
//        Log.d("gobblet2Com","${mas.nameGetter()}から右斜め bord[${mas.myValueOfYGetter()}][${mas.myValueOfXGetter()}]")
        val refX=mas.myValueOfYGetter()
        for (n in 0..3){
            if (n==refX){continue}//自分自身を調べようとしたらスキップ
            val rv = bord[3-n][n].funcForDisplay()
            
            when(rv[1]){
                 0 -> {inTheCaseOfEmp(mas)}//なにもはいってなかった時
                -1 -> { inTheCaseOfM1(rv[0],mas) } //自分のコマが入っていた場合
                 1-> { inTheCaseOfP1(rv[0],mas) } //相手のコマが入っていた場合
            }
//                    Log.d("gobblet2Com","${bord[3-n][n].nameGetter()}に緑")
//                     Log.d("gobblet2Com","${bord[3-n][n].nameGetter()}に赤")
        }
    }
    
    fun inTheCaseOfEmp(mas:Mas){
        mas.addScore(20)
//        Log.d("gobblet2Com","${mas.nameGetter()} add:10")
    }

    //周りををしらべている時に自分のコマがあった時の処理
    fun inTheCaseOfM1(size:Int,mas: Mas){
        when(size){
            1->{ mas.addScore(40) } //小
            2->{ mas.addScore(50) } //中
            3->{ mas.addScore(60) } //大
        }
//                Log.d("gobblet2Com","${mas.nameGetter()} add:40")
//                Log.d("gobblet2Com","${mas.nameGetter()} add:50")
//                Log.d("gobblet2Com","${mas.nameGetter()} add:60")
    }

    //周りををしらべている時に相手のコマがあった時の処理
    fun inTheCaseOfP1(size:Int,mas: Mas){
        P1Count+=1
        when(size){
            1->{ mas.addScore(-20) } //小
            2->{ mas.addScore(-50) } //中
            3->{ mas.addScore(-80) } //大
        }
//                Log.d("gobblet2Com","${mas.nameGetter()} add:-20")
//                Log.d("gobblet2Com","${mas.nameGetter()} add:-30")
//                Log.d("gobblet2Com","${mas.nameGetter()} add:-50")
    }

    //マスが空かどうかしらべる
    fun checkEmptyMas() {
        fun commonFunc(line: Line){
            for (L in line.listGetter()) {
                if (L.returnLastElement() == 0) {
                    L.addScore(10) //マスの中が空だったら評価値10を加える
                }
            }
        }

        for (i in 0..3){
            commonFunc(lineAllAtOnce[i])
        }

    }

    //各マスに何が入っているのかしらべて評価値をつける
    fun checkWhatIsInTheMas(){
        fun commonFunc(line: Line){
            for (mas in line.listGetter()){
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

        for (i in 0..3){
            commonFunc(lineAllAtOnce[i])
        }
    }

    //リーチなった列がないか調べる
    fun reachChecker(){
        fun counter(line: Line){
            var countM1 = 0
            var countP1 = 0
            for (i in line.listGetter()){ // -1,1の個数をそれぞれ数える
                when(i.returnLastElement()) {
                    -1 -> {countM1 += 1}
                    1 -> {countP1 += 1}
                }
                if (countP1 >=3){
                    enemyReachList.add(line.nameGetter())
                    return
                } //1が3つ以上なら敵がリーチ
                if (countM1 >=3){
                    comReachList.add(line.nameGetter())
                    return
                } //-1 が3つ以上ならcomがリーチ
            }
        }

        for (i in 0 until lineAllAtOnce.size){
            counter(lineAllAtOnce[i])
        }
    }

    //コンピューターにリーチがかかってないか調べる(止めをさせる場所を探す)
    fun checkCanIcheckmate(){
        //最後の決めてとなる場所を探す,そしてそこに入れられるかを探す
        fun commonFunc(line:Line){
            var finalTarget:Mas? = null
            //ここからどのマスがまだ自分のマスでないかを教える?
            for (i in line.listGetter()){
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
                    else -> {finalTarget.addScore(500)}//評価値を入れる
                }
            }
            Log.d("gobblet2Com","finalTarget:${finalTarget?.nameGetter()}")
        }
        
        for (value in comReachList){
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

    //人間にリーチがかかってないか調べる(相手の勝利を阻止する)
    fun checkCanIBlockCheckmate(){
        ////どこに入れれば防げるか探す
        fun commonFunc(line:Line){
            var target:Mas? = null
            //ここからどのマスがまだ相手のマスでないかを教える?
            for (i in line.listGetter()){
                if (i.returnLastElement() != 1){
                    target=i
                    //あとは大きさがわかれば良い
                    break
                }
            }

            if (target != null){//コマをおけば防げるところに相手の大きいコマがおいてないか調べる
                when(howBigEnemysPiece(target)){
                    3 -> {target.addScore(-100)}//諦めること指す
                    2 -> {target.addScore(81)}//中くらいのコマ
                    1 -> {target.addScore(82)}//小さいコマ
                    0 -> {target.addScore(400)}//何もおいていない
                }
            }
            Log.d("gobblet2Com","blocktarget:${target?.nameGetter()}")
        }
        for (value in enemyReachList){
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
        var selected :Mas? = line1.listGetter()[0] //適当にA1をデフォルトとする
        var biggestScore = 0

        fun commonFunc(line:Line){
            for (mas in line.listGetter()){
                if (mas.scoreGetter() > biggestScore){ //基準より大きかった場合
                    candidateList.clear() //リストをリセット
                    candidateList.add(mas) //候補リストに追加
                    biggestScore = mas.scoreGetter() //基準を設定し直す
                } else if (mas.scoreGetter() == biggestScore){
                    candidateList.add(mas) //候補リストに追加
                }
            }
        }

        candidateList.clear()
        for (i in 0..3){
            commonFunc(lineAllAtOnce[i])
        }

        selected = candidateList[(0 until  candidateList.size).random()] //候補リストから適当に場所を選ぶ
        Log.d("gobblet2Com","selected:${selected?.nameGetter()}")
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
//リセット関係
    fun resetScore(){
        //すべてのマスクラスの評価値を0にする
        for (l in line1.listGetter()){
            l.resetScore()
        }
        for (l in line2.listGetter()){
            l.resetScore()
        }
        for (l in line3.listGetter()){
            l.resetScore()
        }
        for (l in line4.listGetter()){
            l.resetScore()
        }

    }

    fun resetLists(){
        for (i in 0..9){
            judgeList[i] = 0
        }
        enemyReachList.clear()
        comReachList.clear()
        candidateList.clear()
        doNotMoveList.clear()
    }

//初期化関係
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

    fun iniConcatLine(){ //一旦関数にしないとエラーになるので関数化
        lineAllAtOnce.add(line1)
        lineAllAtOnce.add(line2)
        lineAllAtOnce.add(line3)
        lineAllAtOnce.add(line4)
        lineAllAtOnce.add(lineA)
        lineAllAtOnce.add(lineB)
        lineAllAtOnce.add(lineC)
        lineAllAtOnce.add(lineD)
        lineAllAtOnce.add(lineS)
        lineAllAtOnce.add(lineBS)

        bord.add(line1.listGetter())
        bord.add(line2.listGetter())
        bord.add(line3.listGetter())
        bord.add(line4.listGetter())
    }

    fun iniTemochi(b:Temochi,m:Temochi,s:Temochi){
        temochiBig=b
        temochiMiddle=m
        temochiSmall=s
    }
//デバック関係
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
        Log.d("gobblet2Com","comReachList:${comReachList}")
        Log.d("gobblet2Com","enemyReachList:${enemyReachList}")
        debC()
        Log.d("gobblet2Com","candidateList:${debCandidateList}")
        Log.d("gobblet2Com","DoNotMoveList:${debDoNotMoveList}")
        Log.d("gobblet2Com"," ")
    }

    fun debC(){
        debCandidateList.clear()
        for (i in candidateList){
            debCandidateList.add(i.nameGetter())
        }
        debDoNotMoveList.clear()
        for (i in doNotMoveList){
            debDoNotMoveList.add(i.nameGetter())
        }
    }

    fun debBord(){
        Log.d("gobblet2Com","[${bord[0][0].nameGetter()},${bord[0][1].nameGetter()},${bord[0][2].nameGetter()},${bord[0][3].nameGetter()}]")
        Log.d("gobblet2Com","[${bord[1][0].nameGetter()},${bord[1][1].nameGetter()},${bord[1][2].nameGetter()},${bord[1][3].nameGetter()}]")
        Log.d("gobblet2Com","[${bord[2][0].nameGetter()},${bord[2][1].nameGetter()},${bord[2][2].nameGetter()},${bord[2][3].nameGetter()}]")
        Log.d("gobblet2Com","[${bord[3][0].nameGetter()},${bord[3][1].nameGetter()},${bord[3][2].nameGetter()},${bord[3][3].nameGetter()}]")
    }

    fun deblist(){
        Log.d("gobblet2Com","[]")
    }

}

//Log.d("gobblet2Com","now checking ${bord[y][x].nameGetter()}")