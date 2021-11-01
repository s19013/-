package com.example.gobblet5

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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

    //
    var destination :Mas? = null //移動先
    var movingSource:String? = null //移動元
    var size = 0 //強制的に動かすコマの大きさを決める時に使う

    var target:Mas? = null //防いだり止めを指すのに使う

    private var turnCount = 0 //自分のターンが回ってきた回数



    //考えるのに使う道具?
    private var masInTheGreenBigPiece:MutableList<Mas> = mutableListOf()   //自分の大コマがどこにあるか把握する
    private var masInTheGreenMiddlePiece:MutableList<Mas> = mutableListOf()//自分の中コマがどこにあるか把握する
    private var masInTheGreenSmallPiece:MutableList<Mas> = mutableListOf() //自分の小コマがどこにあるか把握する

    private var masList:MutableList<Mas> = mutableListOf()

    private var mostBiggestScoreList:MutableList<Mas> = mutableListOf() //一番大きいスコア
    private var secondBiggestScoreList:MutableList<Mas> = mutableListOf() //二番目
    private var thirdBiggestScoreList:MutableList<Mas> = mutableListOf() //3番目

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


////リーチ系=------
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
                    humanReachList.add(line)
                    return
                } //1が3つ以上なら敵がリーチ
                if (countM1 >=3){
                    comReachList.add(line)
                    return
                } //-1 が3つ以上ならcomがリーチ
            }
        }

        for (i in 0 until lineAllAtOnce.size){
            counter(lineAllAtOnce[i])
        }
    }

    fun UseBigPieceInSpecialCase(line: Line){
        //手持ちに大きいこまがあったらそれを使う
        if (temochiBig?.returnCount() != 0){ movingSource=temochiBig?.nameGetter() }
        else{
            //ライン上に無い大きいコマを探す
            movingSource =findOtherBigPiece(line).nameGetter()
        }
    }

    fun UseMiddlePieceInSpecialCase(line: Line){
        //手持ちに中コマがあったらそれを使う
        if (temochiMiddle?.returnCount() != 0){ movingSource=temochiMiddle?.nameGetter() }
        else{
            //ライン上に無い中コマを探す
            movingSource =findOtherMiddlePiece(line).nameGetter()
        }
    }

    fun UseSmallPieceInSpecialCase(line: Line){
        //手持ちに中コマがあったらそれを使う
        if (temochiSmall?.returnCount() != 0){ movingSource=temochiSmall?.nameGetter() }
        else{
            //ライン上に無い中コマを探す
            movingSource =findOtherSmallPiece(line).nameGetter()
        }
    }

    //コンピューターにリーチがかかってないか調べる(止めをさせる場所を探す)
    fun checkCanIcheckmate():Boolean{
        //true:強制的にゲームクラスにわたす
        //false:次の作業へすすむ
        //var target:Mas? = null

        fun containHumanBigPiece(line: Line):Boolean{
            //true:おけた
            //false:置けない
            if (use3BigPieceOnTheLine(line)){
                target?.addScore(-300) //大きいコマをリーチを作るのに使っている時は諦める
                return false
            }

            destination=target
            UseBigPieceInSpecialCase(line)
            return true
        }

        fun containHumanMiddlePiece(line: Line):Boolean{
            //true:おけた
            //false:置けない
            if (use3MiddlePieceOnTheLine(line)){ return false } //中コマが使え無い時

            destination=target
            UseMiddlePieceInSpecialCase(line)
            return true
        }

        fun containHumanSmallPiece(line: Line):Boolean{
            //true:おけた
            //false:置けない
            if (use3SmallPieceOnTheLine(line)){
                target?.addScore(-300) //小さいコマが使え無い時
                return false
            }

            destination=target
            UseSmallPieceInSpecialCase(line)
            return true
        }


        //最後の決めてとなる場所を探す,そしてそこに入れられるかを探す
        fun commonFunc(line:Line):Boolean{
            //ここからどのマスがまだ自分のマスでないかを教える?
            for (i in line.listGetter()){
                if (i.returnLastElement() != -1){
                    target=i //最後に置くところがわかった
                    break
                }
            }

            //コマをおけば勝てるところに相手の大きいコマがおいてないか調べる
            if (target != null){
                when(howBighumansPiece(target)){
                    3 -> { target?.addScore(-300) } //諦めること指す
                    2 -> {
                        //相手の中コマが入っていた
                        if (containHumanBigPiece(line)) {return true}
                    }
                    1 -> {
                        //小コマが入っていた
                        //中コマ->大コマの順で考える
                        when{
                            containHumanMiddlePiece(line) -> {return true}
                            containHumanBigPiece(line)    -> {return true}
                        }
                    }
                    0 -> {
                        //小コマ->中コマ->大コマの順で考える
                        when{
                            containHumanSmallPiece(line)  -> {return true}
                            containHumanMiddlePiece(line) -> {return true}
                            containHumanBigPiece(line)    -> {return true}
                        }
                    }
                }
            }
            return false
        }

        //リストの中身を調べていく
        for (value in comReachList){ if (commonFunc(value)){return true} }
        //trueが帰ってきた->勝てる時はループ抜けてコマを置く処理に移る
        return false
    }



    //人間にリーチがかかってないか調べる(相手の勝利を阻止する)
    fun checkCanIBlockCheckmate():Boolean{
        //こっちは評価値を入れるだけで良い
        //場合によってはかぶせたりするから

        ////どこに入れれば防げるか探す
        fun commonFunc(line:Line):Boolean{
            //var target:Mas? = null
            //ここからどのマスがまだ相手のマスでないかを教える?
            for (i in line.listGetter()){
                if (i.returnLastElement() != 1){
                    target=i//置くべき場所がわかった
                    break
                }
            }

            if (target != null){//コマをおけば防げるところに相手のコマがおいてないか調べる
                when(howBighumansPiece(target)){
                    3 -> {target?.addScore(-300)}//諦めること指す
                    else ->{
                        //大きいコマを入れる
                        if (pickUpBigPiece(target)){
                            destination=target
                            return true
                        }
                    }
                }
            }
            return false
        }

        for (value in humanReachList){ if (commonFunc(value)){return true} }
        return false
    }

    //敵のコマの大きさを調べる
    fun howBighumansPiece(mas:Mas?):Int{
        return mas!!.funcForDisplay()[0]
    }

    //同じライン上で自分の大きいコマを3つ使っているか?
    fun use3BigPieceOnTheLine(line: Line):Boolean{
        // true:3つあった
        //false:それ以外
        var counter = 0
        val list = line.listGetter()
        for (mas in list){
            if (mas.funcForDisplay()[1] == -1 && mas.funcForDisplay()[0] == 3){
                //マスに自分の大きいコマが入っていたら+1
                counter+=1
            }
        }
        if (counter==3){return true}
        return false
    }

    //同じライン上で自分の中コマを3つ使っているか?
    fun use3MiddlePieceOnTheLine(line: Line):Boolean{
        // true:3つあった
        //false:それ以外
        var counter = 0
        val list = line.listGetter()
        for (mas in list){
            if (mas.funcForDisplay()[1] == -1 && mas.funcForDisplay()[0] == middlePiece){
                //マスに自分の中コマが入っていたら+1
                counter+=1
            }
        }
        if (counter==3){return true}
        return false
    }

    //同じライン上で自分の小コマを3つ使っているか?
    fun use3SmallPieceOnTheLine(line: Line):Boolean{
        // true:3つあった
        //false:それ以外
        var counter = 0
        val list = line.listGetter()
        for (mas in list){
            if (mas.funcForDisplay()[1] == -1 && mas.funcForDisplay()[0] == smallPiece){
                //マスに自分の小さいコマが入っていたら+1
                counter+=1
            }
        }
        if (counter==3){return true}
        return false
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
    //ついでにコンピューターのコマがどこにあるかも調べる
    fun checkWhatIsInTheMas(){
        fun commonFunc(line: Line){
            for (mas in line.listGetter()){
                val rv = mas.funcForDisplay() //帰り値を入れる箱を用意する
                when{
                    rv[0] == 3 && rv[1] == 1 -> { mas.addScore(-50) }//相手の大コマが置かれている
                    rv[0] == 2 && rv[1] == 1 -> {mas.addScore(-38)}//相手の中コマが置かれている
                    rv[0] == 1 && rv[1] == 1 -> {mas.addScore(-19)}//相手の小コマが置かれている
                    rv[0] == 3 && rv[1] ==-1 -> {
                        mas.addScore(-8)
                        masInTheGreenBigPiece.add(mas)
                    }//自分の大コマが置かれている
                    rv[0] == 2 && rv[1] ==-1 -> {
                        mas.addScore(-8)
                        masInTheGreenMiddlePiece.add(mas)
                    }//自分の中コマが置かれている
                    rv[0] == 1 && rv[1] ==-1 -> {
                        masInTheGreenSmallPiece.add(mas)
                        mas.addScore(-9)
                    }//自分の小コマが置かれている
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
            var countP1=0 //相手のコマの数を数える
            //そのラインの各ますについて調べる
            for (mas in thisLine){
                if (mas == standard) {continue} //基準のマスを調べようとしたらスキップ
                if (mas.returnLastElement() == 1){ countP1 +=1 }
                if (countP1 == 3){doNotMoveList.add(standard!!) } //ライン上は自分以外全部敵のコマだったらそのコマを動かさないリストに追加
            }
        }


        //基準のマスに相手のコマが入っていた､もしくは何も入ってなかった場合
        fun standardIsP1(){
            if (standard!!.funcForDisplay()[0] == 3) {return}
            for (mas in thisLine){
                val rv = mas.funcForDisplay()
                if (mas == standard) {continue} //基準のマスを調べようとしたらスキップ

                //周りの各マスを調べて
                //基準にしたマスに評価値を入れる
                when(rv[1]){
                    0 -> {inTheCaseOfEmp(standard!!)}//なにもはいってなかった時
                    -1 -> { inTheCaseOfM1(rv[0],standard!!) }//自分のコマが入っていた場合
                    1 -> { inTheCaseOfP1(rv[0],standard!!) }//相手のコマが入っていた場合
                }
            }
        }

        for (i in 0..3){
            standard=line.listGetter()[i]
            //ライン上で一番前のマスから順に基準のマスにしていく

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

    //周りををしらべている時に空のコマがあった時の処理
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
        when(size){
            1->{ mas.addScore(-20) } //小
            2->{ mas.addScore(-40) } //中
            3->{ mas.addScore(-80) } //大
        }
//                Log.d("gobblet2Com","${mas.nameGetter()} add:-20")
//                Log.d("gobblet2Com","${mas.nameGetter()} add:-30")
//                Log.d("gobblet2Com","${mas.nameGetter()} add:-50")
    }
//-----------------------------
//配置
    //


    //一番評価値が大きい場所を選ぶ
    fun biggestScore(){
        var biggestScore = 0
        var secondBiggestScore = 0
        var thirdBiggestScore = 0

        fun commonFunc(line:Line){
            for (mas in line.listGetter()){
                if (mas.scoreGetter() > biggestScore){ //基準より大きかった場合
                    biggestScore = mas.scoreGetter() //基準を設定し直す
                    thirdBiggestScoreList.clear()
                    thirdBiggestScoreList.addAll(secondBiggestScoreList) //3番目に上書き
                    secondBiggestScoreList.clear()
                    secondBiggestScoreList.addAll(mostBiggestScoreList) //2番めに上書き
                    mostBiggestScoreList.clear()
                    mostBiggestScoreList.add(mas) //一番大きいリストに追加
                    Log.d("gobblet2Com","biggestScore:${biggestScore}")
                    debC()
                    Log.d("gobblet2Com","mostBiggestScoreList:${debmostBiggestScoreList}")
                    Log.d("gobblet2Com","secondBiggestScoreList:${debsecondBiggestScoreList}")
                    Log.d("gobblet2Com","thirdBiggestScoreList:${debthirdBiggestScoreList}")
                } else if (mas.scoreGetter() == biggestScore){
                    mostBiggestScoreList.add(mas) //候補リストに追加
                    debC()
                    Log.d("gobblet2Com","mostBiggestScoreListAdd")
                    Log.d("gobblet2Com","mostBiggestScoreList:${debmostBiggestScoreList}")
                }
            }
        }

        for (i in 0..3){
            commonFunc(lineAllAtOnce[i])
        }
    }

    //起き場所を決める
    fun chooseLocation(){
        var errorCount =0
        var success = false
        var tentative:Mas? = null //仮の候補
        //一番大きい評価値のマスから選んで行く
        while (true){
            if (errorCount>=mostBiggestScoreList.size){ break } //候補がなくなったらループから抜ける
            destination = mostBiggestScoreList[(0 until mostBiggestScoreList.size).random()]
            if (!choosePickup(destination)){
                errorCount+=1  //指定した場所におけなかったら他のこうほを探す
            } else {
                success=true
                break //おけるなら置く作業に進む
            }
        }
        //一番大きい評価値のマスから選べなかった場合
        //二番目に大きい評価値のマスから選んで行く
        if (!success){
            errorCount = 0
            while (true){
                if (errorCount>=secondBiggestScoreList.size){ break } //候補がなくなったらループから抜ける
                destination = secondBiggestScoreList[(0 until secondBiggestScoreList.size).random()]
                if (!choosePickup(destination)){
                    errorCount+=1  //指定した場所におけなかったら他のこうほを探す
                } else {
                    success=true
                    break //おけるなら置く作業に進む
                }
            }
        }
        //二番目に大きい評価値のマスから選べなかった場合
        //三番目に大きい評価値のマスから選んで行く
        if (!success){
            errorCount = 0
            while (true){
                if (errorCount>=thirdBiggestScoreList.size){ break } //候補がなくなったらループから抜ける
                destination = thirdBiggestScoreList[(0 until thirdBiggestScoreList.size).random()]
                if (!choosePickup(destination)){
                    errorCount+=1  //指定した場所におけなかったら他のこうほを探す
                } else {
                    success=true
                    break //おけるなら置く作業に進む
                }
            }
        }
    }

    //取り出す場所を決める
    //その前に色々検証?
    fun choosePickup(mas: Mas?):Boolean{
        //移動先におけるコマがあるか検証
        when(destination?.funcForDisplay()?.get(0)){
            3 ->{return false} //そもそも大きいコマはどうやっても置けないかえら諦める
            2 ->{
                //中コマなら大きいコマのみおけるから大きいコマを取り出せるか調べる
                return pickUpBigPiece(mas)
            }
            1 -> {
                //小さいコマなら中コマか大コマを取り出せるかしらべる
                //中コマ->大コマと探す
                if (pickUpMiddlePiece(mas)){ return true }
                else  { return pickUpBigPiece(mas) }
            }
            0 -> {
                //空いているなら何でも入れられる
                //中コマ->大コマ->小コマと探す
                return when {
                    pickUpMiddlePiece(mas) -> { true }
                    pickUpBigPiece(mas) -> { true }
                    else -> { pickUpSmallPiece(mas) }
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
        if (temochiBig?.returnCount() != 0){
            movingSource = temochiBig?.nameGetter() //手持ちからだせるなら手持ちを移動元にする
            return true
        } else {
            val box = masInTheGreenBigPiece.minus(doNotMoveList) //差集合を使って動かせる大きいコマがあるか調べる
            if (box.isNotEmpty()){
                //一つでも動かせるならそれを移動元にする
                movingSource= box[0].nameGetter()
                return true
            } else{return false} //だめならだめと返す
        }
    }
    
    //中コマを取り出す関数
    fun pickUpMiddlePiece(mas: Mas?):Boolean{
        //true:取り出せる
        //false:取り出せない
        if (temochiMiddle?.returnCount() != 0){
            movingSource = temochiMiddle?.nameGetter() //手持ちからだせるなら手持ちを移動元にする
            return true
        } else {
            val box = masInTheGreenMiddlePiece.minus(doNotMoveList) //差集合を使って動かせる大きいコマがあるか調べる
            if (box.isNotEmpty()){
                //一つでも動かせるならそれを移動元にする
                movingSource= box[0].nameGetter()
                return true
            } else{return false} //だめならだめと返す
        }
    }

    //小さいコマを取り出す関数
    fun pickUpSmallPiece(mas: Mas?):Boolean{
        //true:取り出せる
        //false:取り出せない
        if (temochiSmall?.returnCount() != 0){
            movingSource = temochiSmall?.nameGetter() //手持ちからだせるなら手持ちを移動元にする
            return true
        } else {
            val box = masInTheGreenSmallPiece.minus(doNotMoveList) //差集合を使って動かせる大きいコマがあるか調べる
            if (box.isNotEmpty()){
                //一つでも動かせるならそれを移動元にする
                movingSource= box[0].nameGetter()
                return true
            } else{return false} //だめならだめと返す
        }
    }

    //最初のターンの定石を実行
    fun firstTurn(){
        val randomNum = (0..3).random()
        when(randomNum){
            0 ->{ destination=lineB.listGetter()[1] } //b2に置く
            1 ->{ destination=lineB.listGetter()[2] } //b3に置く
            2 ->{ destination=lineC.listGetter()[1] } //c2に置く
            3 ->{ destination=lineC.listGetter()[2] } //c3に置く
        }
        movingSource= temochiBig?.nameGetter()
    }

    //2ターン目の定石
    fun secondTurn(){
        movingSource=temochiBig?.nameGetter()

    }


    fun start(){
        turnCount+=1
        reachChecker() //リーチがかかってないか調べる
        //自分にリーチがかかっていた
        if (comReachList.size!=0){ if (checkCanIcheckmate()){return} }
        //相手にリーチがかかっていた
        if (humanReachList.size!=0){
            if (checkCanIBlockCheckmate()){return}
        }
        //1ターン目
        if (turnCount==1){
            firstTurn()
            return
        }
        //2ターン目
//        if (turnCount==2){
//            secondTurn()
//            return
//        }
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

    fun destinationGetter(): Mas? {
        return destination
    }

    fun movingSourceGetter():String?{
        return movingSource
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
        target=null
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
        Log.d("gobblet2Com"," ")
        Log.d("gobblet2Com","movingSource:${movingSource}")
        Log.d("gobblet2Com","destination:${destination?.nameGetter()}")
        Log.d("gobblet2Com","--------------------------------")

    }

    fun debC(){
        val debList = mutableListOf(
            debCandidateList,debDoNotMoveList,
            debComReachList, debhumanReachList,
            debMasInTheGreenBigPiece, debMasInTheGreenMiddlePiece, debMasInTheGreenSmallPiece,
            debMasList,
            debmostBiggestScoreList, debsecondBiggestScoreList, debthirdBiggestScoreList
            )

        val List = mutableListOf(
            candidateList,doNotMoveList,
            comReachList, humanReachList,
            masInTheGreenBigPiece, masInTheGreenMiddlePiece, masInTheGreenSmallPiece,
            masList,
            mostBiggestScoreList, secondBiggestScoreList, thirdBiggestScoreList
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