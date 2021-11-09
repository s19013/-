package com.example.gobblet5

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment7_1
import com.example.gobblet5.HowToPlayFragment.HowToPlayFragment7_2
import org.jetbrains.annotations.NotNull
import java.util.stream.Collectors
import java.util.stream.Stream

class Com {
    //マジックナンバー防止
    private val stringLine1="L1"
    private val stringLine2="L2"
    private val stringLine3="L3"
    private val stringLine4="L4"
    private val stringLineA="LA"
    private val stringLineB="LB"
    private val stringLineC="LC"
    private val stringLineD="LD"
    private val stringLineS="LS"
    private val stringLineBS="LBS"

    private val comPiece=-1
    private val humanPiece = 1
    private val empty = 0

    private val bigPiece=3
    private val middlePiece=2
    private val smallPiece=1


    //ライン
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

    //手持ち
    private var temochiBig:Temochi? = null
    private var temochiMiddle:Temochi? = null
    private var temochiSmall:Temochi? = null

    var destination :Mas? = null //移動先
    var movingSource:Any? = null //移動元

    var blocking=false
    var chance = false

    private var turnCount = 0 //自分のターンが回ってきた回数



    //考えるのに使う道具?
    private var masInTheGreenBigPiece:MutableList<Mas> = mutableListOf()   //自分の大コマがどこにあるか把握する
    private var masInTheGreenMiddlePiece:MutableList<Mas> = mutableListOf()//自分の中コマがどこにあるか把握する
    private var masInTheGreenSmallPiece:MutableList<Mas> = mutableListOf() //自分の小コマがどこにあるか把握する

    private var masList:MutableList<Mas> = mutableListOf()

    private var mostBiggestScoreList:MutableList<Mas>   = mutableListOf() //一番大きいスコア
    private var secondBiggestScoreList:MutableList<Mas> = mutableListOf() //二番目
    private var thirdBiggestScoreList:MutableList<Mas>  = mutableListOf() //3番目
    private var fourthBiggestScoreList:MutableList<Mas> = mutableListOf() //4番目
    private var fifthBiggestScoreList:MutableList<Mas>  = mutableListOf() //5番目

    private var doNotMoveList:MutableList<Mas> = mutableListOf() //動かしては行けないコマを管理
    private var unnecessaryList:MutableList<String> = mutableListOf() //うごかしても問題ないコマを管理
    private var candidateList:MutableList<Mas> = mutableListOf() //コマを入れる候補を管理するリスト
    private var humanReachList:MutableList<Line> = mutableListOf() //敵にリーチがかかっているラインを管理するリスト
    private var comReachList:MutableList<Line> = mutableListOf() //自分にリーチがかかっているラインを管理するリスト
    private var bord:MutableList<MutableList<Mas>> = mutableListOf() //[縦列][横列]　例:B3 -> [2][1]
    private var lineAllAtOnce:MutableList<Line> = mutableListOf() //すべてのラインクラスに対して色々やる時に使うリスト
    private var judgeList:MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0)
    private var nameList:MutableList<String> = mutableListOf(stringLine1,stringLine2,stringLine3,stringLine4,
        stringLineA,stringLineB,stringLineC,stringLineD,stringLineS,stringLineBS)

    //デバッグ用
    private var debComReachList = mutableListOf<String>()
    private var debhumanReachList = mutableListOf<String>()
    private var debCandidateList= mutableListOf<String>()
    private var debDoNotMoveList= mutableListOf<String>()
    private var debMasInTheGreenBigPiece:MutableList<String> = mutableListOf()
    private var debMasInTheGreenMiddlePiece:MutableList<String> = mutableListOf()
    private var debMasInTheGreenSmallPiece:MutableList<String> = mutableListOf()
    private var debMasList = mutableListOf<String>()
    private var debmostBiggestScoreList:MutableList<String> = mutableListOf() //一番大きいスコア
    private var debsecondBiggestScoreList:MutableList<String> = mutableListOf() //二番目
    private var debthirdBiggestScoreList:MutableList<String> = mutableListOf() //3番目
    private var debFourthBiggestScoreList:MutableList<String> = mutableListOf() //4
    private var debFifthBiggestScoreList:MutableList<String> = mutableListOf() //5

////リーチ系=------
    //リーチなった列がないか調べる
    fun reachChecker(){
        fun commonFunc(line: Line){
            if (line.comPieceCounter()>=3){
                comReachList.add(line)
                return
            }

            if (line.humanPieceCounter()>=3){
                humanReachList.add(line)
                return
            }
        }

        for (i in 0 until lineAllAtOnce.size){
            commonFunc(lineAllAtOnce[i])
        }
    }

    //コンピューターにリーチがかかってないか調べる(止めをさせる場所を探す)
    fun checkCanIcheckmate(){
        var target:Mas? = null

        //最後の決めてとなる場所を探す,そしてそこに入れられるかを探す
        fun commonFunc(line:Line){
            //ここからどのマスがまだ自分のマスでないかを教える?
            for (i in line.listGetter()){
                if (i.returnLastElement() != comPiece){
                    //相手の大きいコマでブロックされている
                    if (i.funcForDisplay()[1]*i.funcForDisplay()[0] == 3){
                        comReachList.remove(line)
                    }
                    //すでにすべての大きいコマをそのリーチを作るのに使っていてなおかつ､最後のマスに相手の中コマ以上が入っている
                    else if (line.use3BigPieceOnTheLine() && i.funcForDisplay()[1]*i.funcForDisplay()[0] > 2){
                        comReachList.remove(line) //すでにブロックされていたらリストから消す
                    } else{
                        target=i//置くべき場所がわかった
                        break
                    }
                }
            }

            //コマをおけば勝てるところに相手の大きいコマがおいてないか調べる
            if (target != null){
                //狙っている場所の相手のコマの大きさを調べる
                when(howBigPiece(target)){
                    3 -> {
                        target?.addScore(-300)
                    } //諦めること指す
                    else -> {target?.addScore(10000)} //行けることを表す
                }
            }
        }

        //リストの中身を調べていく
        var listForIterativeProcessing = mutableListOf<Line>()
        listForIterativeProcessing.addAll(comReachList)
        //繰り返し処理中のリストにremoveとかしちゃうと動きがおかしくなるから一旦別の変数にコピー
        for (value in listForIterativeProcessing){ commonFunc(value) }

        if (comReachList.isNotEmpty()){chance=true}
    }

    //人間にリーチがかかってないか調べる(相手の勝利を阻止する)
    fun checkCanIBlockCheckmate(){
        //場合によってはblockingを解除する必要がある
        var target:Mas? = null

        ////どこに入れれば防げるか探す
        fun commonFunc(line:Line){
            //そのライン上で相手のものになっていないマスを探す?
            for (mas in line.listGetter()){
                if (!mas.OccupiedByTheHuman()){ //相手のものになってないマスを見つけた
                    //すでに自分の大きいコマでブロックしてあるか調べる
                    if (mas.funcForDisplay()[1]*mas.funcForDisplay()[0] == -3){
                        humanReachList.remove(line) //すでにブロックしてたらリストから消す
                    } else{
                        target=mas//置くべき場所がわかった
                        break
                    }
                }
            }

            if (target != null){//コマをおけば防げるところに相手のコマがおいてないか調べる
                when(howBigPiece(target)){
                    bigPiece -> {
                        target?.addScore(-300)
                    }//諦めること指す
                    else ->{ target?.addScore(800) }
                }
            }
        }

        var listForIterativeProcessing = mutableListOf<Line>()
        listForIterativeProcessing.addAll(humanReachList)
        //繰り返し処理中のリストにremoveとかしちゃうと動きがおかしくなるから一旦別の変数にコピー
        for (value in listForIterativeProcessing){ commonFunc(value) }

        debC()
        Log.d("gobblet2Com","debhumanReachList:${debhumanReachList}")

        //細かくしらべて本当にリーチがかかっているかつまだ止めをさせないならばブロックする
        if (humanReachList.isNotEmpty() && !chance){blocking = true}

        Log.d("gobblet2Com","blocking:${blocking}")
        Log.d("gobblet2Com","chance:${chance}")

    }

    //敵のコマの大きさを調べる
    fun howBigPiece(mas:Mas?):Int{
        return mas!!.funcForDisplay()[0]
    }



    //ボード上に動かせる大きなコマはないか探す
    fun findOtherBigPiece(line: Line):Mas{
        val list = line.listGetter()
        val etc = masInTheGreenBigPiece.minus(list)
        return etc[0]
    }

    //ボード上に動かせる中コマはないか探す
    fun findOtherMiddlePiece(line: Line):Mas{
        val list = line.listGetter()
        val etc = masInTheGreenMiddlePiece.minus(list)
        return etc[0]
    }

    //ボード上に動かせる小コマはないか探す
    fun findOtherSmallPiece(line: Line):Mas{
        val list = line.listGetter()
        val etc = masInTheGreenSmallPiece.minus(list)
        return etc[0]
    }
////-----------------------
//評価値関係?-----------------
    //マスが空かどうかしらべる
    fun checkEmptyMas() {
        fun commonFunc(line: Line){
            for (mas in line.listGetter()) {
                if (mas.returnLastElement() == 0) {
                    mas.addScore(10) //マスの中が空だったら評価値10を加える
                }
            }
        }

        for (i in 0..3){
            commonFunc(lineAllAtOnce[i])
        }

    }

    //各マスに何が入っているのかしらべて評価値をつける
    //ついでにコンピューターのコマがどこにあるかも調べる
    fun checkWhatIsInTheMas(){
        fun commonFunc(line: Line){
            for (mas in line.listGetter()){
                val rv = mas.funcForDisplay() //帰り値を入れる箱を用意する
                val size = rv[0] //コマの大きさ
                val attribute = rv[1] //人間のかコンピューターのか
                when{
                    size == bigPiece    && attribute == humanPiece -> { mas.addScore(-50) }//相手の大コマが置かれている
                    size == middlePiece && attribute == humanPiece -> { mas.addScore(-30) }//相手の中コマが置かれている
                    size == smallPiece  && attribute == humanPiece -> { mas.addScore(-20) }//相手の小コマが置かれている
                    size == bigPiece    && attribute == comPiece -> {
                        //自分の大コマが置かれている
                        mas.addScore(-50)
                        masInTheGreenBigPiece.add(mas)
                    }
                    size == middlePiece && attribute == comPiece -> {
                        //自分の中コマが置かれている
                        mas.addScore(-5)
                        masInTheGreenMiddlePiece.add(mas)
                    }
                    size == smallPiece && attribute == comPiece -> {
                        //自分の小コマが置かれている
                        masInTheGreenSmallPiece.add(mas)
                        mas.addScore(-10)
                    }
                }
            }
        }

        for (i in 0..3){
            commonFunc(lineAllAtOnce[i])
        }
    }

    //コマの周りを調べる(今は空白の部分だけ)
    fun checkEachMas(){
        for (line in lineAllAtOnce){
            funcForCheckEachMas(line)
        }
    }

    //ラインごとに分けて各マスに評価値を入れる
    fun funcForCheckEachMas(line: Line){
        var standard:Mas? =null
        var thisLine= line.listGetter()

        //基準のマスに自分のコマが入っていた場合
        fun standardIsM1(){
            if (line.humanPieceCounter() == 3 && !comReachList.contains(line) ){ //<- なんで!comReachList.contains(line)とかにしているかわすれた
                //ライン上は自分以外全部敵のコマだった
                //基準の自分のコマは相手のリーチをふせいでいる
                //基準のコマがリーチを作るのに使われていなかったらそのコマを動かさないリストに追加
                doNotMoveList.add(standard!!)
            }
        }


        //基準のマスに相手のコマが入っていた､もしくは何も入ってなかった場合
        //後で編集
        fun standardIsP1(){
            if (standard!!.funcForDisplay()[0] == bigPiece) {return} //基準が大きいコマだったら飛ばす (どうやってもコマが入らないから)
            for (mas in thisLine){
                val rv = mas.funcForDisplay()
                if (mas == standard) {continue} //基準のマスを調べようとしたらスキップ

                //周りの各マスを調べて
                //基準にしたマスに評価値を入れる
                when(rv[1]){
                    empty      -> { inTheCaseOfEmp(standard!!)}//なにもはいってなかった時
                    comPiece   -> { inTheCaseOfM1(rv[0],standard!!) }//自分のコマが入っていた場合
                    humanPiece -> { inTheCaseOfP1(rv[0],standard!!) }//相手のコマが入っていた場合
                }
            }
        }

        fun standardIsEmp(){

        }

        for (i in 0..3){
            //ライン上で一番前のマスから順に基準のマスにしていく
            standard=line.listGetter()[i]

            //基準となったマスに何が入っているかによってすることが違う
            when(standard.funcForDisplay()[1]){
                comPiece ->{standardIsM1()}
                humanPiece ->{standardIsP1()}
                empty ->{standardIsP1()}
            }
        }

        //Log.d("gobblet2Com","${bord[y][x].nameGetter()}に緑")
        //Log.d("gobblet2Com","${bord[y][x].nameGetter()}に赤")
    }

    //周りををしらべている時に空のコマがあった時の処理
    fun inTheCaseOfEmp(mas:Mas){
        mas.addScore(30)
//        Log.d("gobblet2Com","${mas.nameGetter()} add:10")
    }

    //周りををしらべている時に自分のコマがあった時の処理
    fun inTheCaseOfM1(size:Int,mas: Mas){
        when(size){
            smallPiece  ->{ mas.addScore(40) } //小
            middlePiece ->{ mas.addScore(50) } //中
            bigPiece    ->{ mas.addScore(60) } //大
        }
    }

    //周りををしらべている時に相手のコマがあった時の処理
    fun inTheCaseOfP1(size:Int,mas: Mas){
        when(size){
            smallPiece  ->{ mas.addScore(-10) } //小
            middlePiece ->{ mas.addScore(-30) } //中
            bigPiece    ->{ mas.addScore(-50) } //大
        }
    }
//-----------------------------
//配置
    //


    //一番評価値が大きい場所を選ぶ
    fun biggestScore(){
        var biggestScore = mutableListOf(-500,-500,-500,-500,-500) //[dammy,1番,2番､3番､4番､5番]

        fun setBiggestScore(line: Line){
            for (mas in line.listGetter()){
                when{
                    mas.scoreGetter() > biggestScore[0] ->
                        {
                            for (i in 4 downTo 1){ biggestScore[i] = biggestScore[i-1] }
                            biggestScore[0] = mas.scoreGetter()
                        }
                    mas.scoreGetter() > biggestScore[1] && mas.scoreGetter() < biggestScore[0] ->
                        {
                            for (i in 4 downTo 2){ biggestScore[i] = biggestScore[i-1] }
                            biggestScore[1] = mas.scoreGetter()
                        }
                    mas.scoreGetter() > biggestScore[2] && mas.scoreGetter() < biggestScore[1] ->
                        {
                            for (i in 4 downTo 3){ biggestScore[i] = biggestScore[i-1] }
                            biggestScore[2] = mas.scoreGetter()
                        }
                    mas.scoreGetter() > biggestScore[3] && mas.scoreGetter() < biggestScore[2] ->
                        {
                            biggestScore[4] = biggestScore[3]
                            biggestScore[3] = mas.scoreGetter()
                        }
                    mas.scoreGetter() > biggestScore[4] && mas.scoreGetter() < biggestScore[3] ->
                        {
                            biggestScore[4] = mas.scoreGetter()
                        }
                }
            }
        }

        fun addMas(line: Line){
            for (mas in line.listGetter()){
                when{
                    mas.scoreGetter() == biggestScore[0] -> {mostBiggestScoreList.add(mas)}
                    mas.scoreGetter() == biggestScore[1] -> {secondBiggestScoreList.add(mas)}
                    mas.scoreGetter() == biggestScore[2] -> {thirdBiggestScoreList.add(mas)}
                    mas.scoreGetter() == biggestScore[3] -> {fourthBiggestScoreList.add(mas)}
                    mas.scoreGetter() == biggestScore[4] -> {fifthBiggestScoreList.add(mas)}
                }
            }
        }

        for (i in 0..3){
            setBiggestScore(lineAllAtOnce[i])
        }
        for (i in 0..3){
            addMas(lineAllAtOnce[i])
        }

//        debC()
//        Log.d("gobblet2Com","1番:${biggestScore[0]} -${debmostBiggestScoreList}")
//        Log.d("gobblet2Com","2番:${biggestScore[1]} -${debsecondBiggestScoreList}")
//        Log.d("gobblet2Com","3番:${biggestScore[2]} -${debthirdBiggestScoreList}")
//        Log.d("gobblet2Com","4番:${biggestScore[3]} -${debFourthBiggestScoreList}")
//        Log.d("gobblet2Com","5番:${biggestScore[4]} -${debFifthBiggestScoreList}")
    }

    //起き場所を決める
    fun chooseLocation(){
        var errorCount =0
        var success = false
        //一番大きい評価値のマスから選んで行く
        while (true){
            if (errorCount>=mostBiggestScoreList.size){ break } //数回エラーがでたらループを抜ける
            destination = mostBiggestScoreList[(0 until mostBiggestScoreList.size).random()]
            if (!choosePickup(destination)){ errorCount+=1 }//指定した場所におけなかったら他のこうほを探す
            else { return } //おけるなら置く作業に進む
        }
        //一番大きい評価値のマスから選べなかった場合
        //二番目に大きい評価値のマスから選んで行く
        errorCount = 0
        while (true){
            if (errorCount>=secondBiggestScoreList.size){ break } //数回エラーがでたらループを抜ける
            destination = secondBiggestScoreList[(0 until secondBiggestScoreList.size).random()]
            if (!choosePickup(destination)){ errorCount+=1 } //指定した場所におけなかったら他のこうほを探す
            else { return } //おけるなら置く作業に進む

        }
        //二番目に大きい評価値のマスから選べなかった場合
        //三番目に大きい評価値のマスから選んで行く
        errorCount = 0
        while (true){
            if (errorCount>=thirdBiggestScoreList.size){ break } //候補がなくなったらループから抜ける
            destination = thirdBiggestScoreList[(0 until thirdBiggestScoreList.size).random()]
            if (!choosePickup(destination)){ errorCount+=1 }
            else { return } //おけるなら置く作業に進む
        }

        errorCount = 0
        while (true){
            if (errorCount>=fourthBiggestScoreList.size){ break } //候補がなくなったらループから抜ける
            destination = fourthBiggestScoreList[(0 until fourthBiggestScoreList.size).random()]
            if (!choosePickup(destination)){ errorCount+=1 }
            else { return } //おけるなら置く作業に進む
        }

        errorCount = 0
        while (true){
            if (errorCount>=fifthBiggestScoreList.size){ break } //候補がなくなったらループから抜ける
            destination = fifthBiggestScoreList[(0 until fifthBiggestScoreList.size).random()]
            if (!choosePickup(destination)){ errorCount+=1 }
            else { return } //おけるなら置く作業に進む
        }
    }

    //取り出す場所を決める
    //その前に色々検証?
    fun choosePickup(mas: Mas?):Boolean{
        //移動先におけるコマがあるか検証
        when(destination?.funcForDisplay()?.get(0)){
            bigPiece ->{return false} //そもそも大きいコマはどうやっても置けないかえら諦める
            middlePiece ->{
                //中コマなら大きいコマのみおけるから大きいコマを取り出せるか調べる
                return pickUpBigPiece(mas)
            }
            smallPiece -> {
                //小さいコマなら中コマか大コマを取り出せるかしらべる
                //中コマ->大コマと探す
                if (pickUpMiddlePiece(mas)){ return true }
                else  { return pickUpBigPiece(mas) }
            }
            empty -> {
                //ここではいろんな条件に応じてうごかないと行けない
                if (blocking){
                    //ブロックまたは2ターン目に大きいコマを使う
                    if (pickUpBigPiece(mas)){
                        return true
                    } else if (pickUpMiddlePiece(mas)){
                        //どうしても大きいコマが使えない時は中コマ
                        return true
                    } else {
                        return pickUpSmallPiece(mas)
                    }
                }
                //空いているなら何でも入れられる
                //中コマ->小コマ->大きいと探す
                else{
                    if (temochiMiddle?.returnCount()!! == 0 && temochiBig?.returnCount()!! == 0){
                        if (pickUpSmallPiece(mas)){return true}
                    }
                    if (pickUpMiddlePiece(mas)){return true}
                    if (pickUpBigPiece(mas)) {return true}
                }
            }
        }
        return false
    }

    //大きさを決定する
    fun chooseASize(){

    }

    //大コマを取り出す関数
    fun pickUpBigPiece(mas: Mas?):Boolean{
    //true:取り出せる
    //false:取り出せない
        if (temochiBig?.returnCount()!! > 0){
            movingSource = temochiBig //手持ちからだせるなら手持ちを移動元にする
            Log.d("gobblet2Com","pickupFromTemochi")
            return true
        } else {
            val box = masInTheGreenBigPiece.minus(doNotMoveList) //差集合を使って動かせる大きいコマがあるか調べる
            Log.d("gobblet2Com","boxSize${box.size}")
            if (box.isNotEmpty()){
                //一つでも動かせるならそれを移動元にする
                movingSource= box[(box.indices).random()]
                Log.d("gobblet2Com","pickupFrom${movingSource}")
                return true
            } else{
                Log.d("gobblet2Com","can't pickup")
                return false} //だめならだめと返す
        }
    }
    
    //中コマを取り出す関数
    fun pickUpMiddlePiece(mas: Mas?):Boolean{
        //true:取り出せる
        //false:取り出せない
        if (temochiMiddle?.returnCount()!! > 0){
            movingSource = temochiMiddle //手持ちからだせるなら手持ちを移動元にする
            return true
        } else {
            val box = masInTheGreenMiddlePiece.minus(doNotMoveList) //差集合を使って動かせる大きいコマがあるか調べる
            if (box.isNotEmpty()){
                //一つでも動かせるならそれを移動元にする
                movingSource= box[(box.indices).random()]
                return true
            } else{return false} //だめならだめと返す
        }
    }

    //小さいコマを取り出す関数
    fun pickUpSmallPiece(mas: Mas?):Boolean{
        //true:取り出せる
        //false:取り出せない
        if (temochiSmall?.returnCount()!! > 0){
            movingSource = temochiSmall //手持ちからだせるなら手持ちを移動元にする
            return true
        } else {
            val box = masInTheGreenSmallPiece.minus(doNotMoveList) //差集合を使って動かせる大きいコマがあるか調べる
            if (box.isNotEmpty()){
                //一つでも動かせるならそれを移動元にする
                movingSource= box[(box.indices).random()]
                return true
            } else{return false} //だめならだめと返す
        }
    }

    //最初のターンの定石を実行
    fun firstTurn(){
        while (true){
            val randomNum = (0..3).random()
            when(randomNum){
                0 ->{
                    //b2に置く
                    if (lineB.listGetter()[1].returnLastElement() != 1){
                        destination=lineB.listGetter()[1]
                        break
                    }
                }
                1 ->{
                    //b3に置く
                    if (lineB.listGetter()[2].returnLastElement() != 1){
                        destination=lineB.listGetter()[2]
                        break
                    }
                }
                2 ->{
                    //c2に置く
                    if (lineC.listGetter()[1].returnLastElement() != 1){
                        destination=lineC.listGetter()[1]
                        break
                    }
                }
                3 ->{
                    //c3に置く
                    if (lineC.listGetter()[2].returnLastElement() != 1){
                        destination=lineC.listGetter()[2]
                        break
                    }
                }
            }
            //すでにプレイヤーがコマをおいていたらやり直し
            //空いているところを見つけるまでずっと探す

        }
        movingSource= temochiBig
    }

    //2ターン目の定石
    fun secondTurn(){
        movingSource=temochiBig

    }

    fun start(){
        turnCount+=1
        //1ターン目
        if (turnCount==1){
            firstTurn()
            return
        }
        reachChecker() //リーチがかかってないか調べる
        //自分にリーチがかかっていた
        if (comReachList.isNotEmpty()){ checkCanIcheckmate() }
        //相手にリーチがかかっていた
        if (humanReachList.isNotEmpty()){ checkCanIBlockCheckmate() }

        standardProcessing()
    }

    //特定条件以外での処理
    fun standardProcessing(){
        checkWhatIsInTheMas()
        checkEachMas()
        biggestScore()
        chooseLocation()
    }

    //リストにスコアをいれていく
    //最後に大きい順にソート
    //ソート順だとある程度決まった場所しかとらないので面白くない
    fun sortMasList(){
        val comparator:Comparator<Mas> = compareBy<Mas> { it.scoreGetter() }
        masList.sortWith(comparator)
        masList.reverse()
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

    fun destinationGetter(): Mas?{
        return destination
    }

    fun movingSourceGetter():Any?{
        return movingSource
    }

//リセット関係
    fun resetScore(){
        //すべてのマスクラスの評価値を0にする
        for (mas in line1.listGetter()){ mas.resetScore() }
        for (mas in line2.listGetter()){ mas.resetScore() }
        for (mas in line3.listGetter()){ mas.resetScore() }
        for (mas in line4.listGetter()){ mas.resetScore() }
    }

    fun resetLists(){
        for (i in 0..9){ judgeList[i] = 0 }

        humanReachList.clear()
        comReachList.clear()
        candidateList.clear()
        doNotMoveList.clear()
        masInTheGreenSmallPiece.clear()
        masInTheGreenMiddlePiece.clear()
        masInTheGreenBigPiece.clear()
        mostBiggestScoreList.clear()
        secondBiggestScoreList.clear()
        thirdBiggestScoreList.clear()
        fourthBiggestScoreList.clear()
        fifthBiggestScoreList.clear()
        blocking=false
        chance=false
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


        for (mas in line1.listGetter()){ masList.add(mas) }
        for (mas in line2.listGetter()){ masList.add(mas) }
        for (mas in line3.listGetter()){ masList.add(mas) }
        for (mas in line4.listGetter()){ masList.add(mas) }


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
        debC()
        Log.d("gobblet2Com","comReachList:${debComReachList}")
        Log.d("gobblet2Com","humanReachList:${debhumanReachList}")
        Log.d("gobblet2Com","DoNotMoveList:${debDoNotMoveList}")
        Log.d("gobblet2Com","debMasInTheGreenBigPiece:${debMasInTheGreenBigPiece}")
        Log.d("gobblet2Com"," ")
        if (movingSource is Mas){
            val m:Mas= movingSource as Mas
            Log.d("gobblet2Com","movingSource:${m.nameGetter()}")
        }
        if (movingSource is Temochi){
            val m:Temochi= movingSource as Temochi
            Log.d("gobblet2Com","movingSource:${m.nameGetter()}")
        }
        Log.d("gobblet2Com","destination:${destination?.nameGetter()}")
        Log.d("gobblet2Com","--------------------------------")

    }

    fun debC(){
        val debList = mutableListOf(
            debCandidateList,debDoNotMoveList,
            debComReachList, debhumanReachList,
            debMasInTheGreenBigPiece, debMasInTheGreenMiddlePiece, debMasInTheGreenSmallPiece,
            debMasList,
            debmostBiggestScoreList, debsecondBiggestScoreList, debthirdBiggestScoreList,
            debFourthBiggestScoreList,debFifthBiggestScoreList
            )

        val List = mutableListOf(
            candidateList,doNotMoveList,
            comReachList, humanReachList,
            masInTheGreenBigPiece, masInTheGreenMiddlePiece, masInTheGreenSmallPiece,
            masList,
            mostBiggestScoreList, secondBiggestScoreList, thirdBiggestScoreList,
            fourthBiggestScoreList,fifthBiggestScoreList
        )

        for (l in debList){
            l.clear()
        }

        for (i in candidateList){ debCandidateList.add(i.nameGetter()) }
        for (i in doNotMoveList){ debDoNotMoveList.add(i.nameGetter()) }
        for (i in comReachList){ debComReachList.add(i.nameGetter()) }
        for (i in humanReachList){ debhumanReachList.add(i.nameGetter()) }
        for (i in masInTheGreenBigPiece){ debMasInTheGreenBigPiece.add(i.nameGetter()) }
        for (i in masInTheGreenMiddlePiece){ debMasInTheGreenMiddlePiece.add(i.nameGetter()) }
        for (i in masInTheGreenSmallPiece){ debMasInTheGreenSmallPiece.add(i.nameGetter()) }
        for (i in masList){debMasList.add(i.nameGetter())}

        for (i in mostBiggestScoreList){debmostBiggestScoreList.add(i.nameGetter())}
        for (i in secondBiggestScoreList){debsecondBiggestScoreList.add(i.nameGetter())}
        for (i in thirdBiggestScoreList){debthirdBiggestScoreList.add(i.nameGetter())}
        for (i in fourthBiggestScoreList){debFourthBiggestScoreList.add(i.nameGetter())}
        for (i in fifthBiggestScoreList){debFifthBiggestScoreList.add(i.nameGetter())}
        
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