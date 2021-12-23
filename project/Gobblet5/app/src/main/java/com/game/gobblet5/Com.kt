package com.game.gobblet5

class Com {
    private val comPiece=-1
    private val humanPiece = 1
    private val empty = 0

    private val bigPiece=3
    private val middlePiece=2
    private val smallPiece=1

    //ライン
    private val line1:Line = Line("L1")
    private val line2:Line = Line("L2")
    private val line3:Line = Line("L3")
    private val line4:Line = Line("L4")
    private val lineA:Line = Line("LA")
    private val lineB:Line = Line("LB")
    private val lineC:Line = Line("LC")
    private val lineD:Line = Line("LD")
    private val lineS:Line = Line("LS")
    private val lineBS:Line = Line("LBS")

    //手持ち
    private var temochiBig:Temochi? = null
    private var temochiMiddle:Temochi? = null
    private var temochiSmall:Temochi? = null


    //ゲームクラスが取り出せるやつ
    var destination :Mas? = null //移動先
    var movingSource:Any? = null //移動元

    private var blocking=false
    private var chance = false

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

    private var doNotMoveListBecauseItIsBlocking:MutableList<Mas> = mutableListOf() //敵のリーチをブロックしているから動かしては行けないコマを管理
    private var doNotMoveListBecauseItMakeReach:MutableList<Mas> = mutableListOf() //リーチを作っているから動かしては行けないコマを管理
    private var candidateList:MutableList<Mas> = mutableListOf() //コマを入れる候補を管理するリスト
    private var humanReachList:MutableList<Line> = mutableListOf() //敵にリーチがかかっているラインを管理するリスト
    private var comReachList:MutableList<Line> = mutableListOf() //自分にリーチがかかっているラインを管理するリスト
    private var bord:MutableList<MutableList<Mas>> = mutableListOf() //[縦列][横列]　例:B3 -> [2][1]
    
    private var judgeList:MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0)

    //デバッグ用
    private var debComReachList = mutableListOf<String>()
    private var debHumanReachList = mutableListOf<String>()
    private var debCandidateList= mutableListOf<String>()
    private var debDoNotMoveList= mutableListOf<String>()
    private var debDoNotMoveListBecauseItIsBlocking:MutableList<String> = mutableListOf()
    private var debDoNotMoveListBecauseItMakeReach:MutableList<String> = mutableListOf()
    private var debMasInTheGreenBigPiece:MutableList<String> = mutableListOf()
    private var debMasInTheGreenMiddlePiece:MutableList<String> = mutableListOf()
    private var debMasInTheGreenSmallPiece:MutableList<String> = mutableListOf()
    private var debMasList = mutableListOf<String>()
    private var debMostBiggestScoreList:MutableList<String> = mutableListOf() //一番大きいスコア
    private var debSecondBiggestScoreList:MutableList<String> = mutableListOf() //二番目
    private var debThirdBiggestScoreList:MutableList<String> = mutableListOf() //3番目
    private var debFourthBiggestScoreList:MutableList<String> = mutableListOf() //4
    private var debFifthBiggestScoreList:MutableList<String> = mutableListOf() //5

    //アルゴリズムを少しいじる

////リーチ系=------
    //リーチなった列がないか調べる
    private fun reachChecker(){
        fun commonFunc(line: Line){
            if (line.comPieceCounter()>=3){ comReachList.add(line) } //なんでここでリターンしたんだろう?リターンしたら人間のリーチをしらべられない
            if (line.humanPieceCounter()>=3){ humanReachList.add(line) }
        }

        commonFunc(line1)
        commonFunc(line2)
        commonFunc(line3)
        commonFunc(line4)
        commonFunc(lineA)
        commonFunc(lineB)
        commonFunc(lineC)
        commonFunc(lineD)
        commonFunc(lineS)
        commonFunc(lineBS)
    }

    //コンピューターにリーチがかかってないか調べる(止めをさせる場所を探す)
    private fun checkCanICheckmate(){

        //最後の決めてとなる場所を探す,そしてそこに入れられるかを探す
        fun commonFunc(line:Line){
            //どのマスがまだ自分のマスでないかを調べる?
            for (mas in line.listGetter()){
                val size = mas.funcForDisplay()[0] //コマの大きさ
                val attribute = mas.funcForDisplay()[1] //人間のかコンピューターのか
                if (!mas.OccupiedByTheCom()){ //自分のマスで埋まってない場所を見つけた
                    //最後のマスが相手の大きいコマでブロックされている場合は諦める
                    if (attribute==humanPiece && size == bigPiece){
                            comReachList.remove(line)
                            mas.addScore(-300)
                    }
                    //大きいコマがすべて動かせない状態で
                    //なおかつ､最後のマスに相手の中コマ以上が入っている場合は諦める
                    else if (line.use3BigPieceOnTheLine() &&
                        attribute == humanPiece && size > 2){
                            comReachList.remove(line)
                            mas.addScore(-300)
                    }
                    else{
                        mas.addScore(10000)
                        break
                    }
                }
            }
        }

        //リストの中身を調べていく
        val listForIterativeProcessing = mutableListOf<Line>()
        listForIterativeProcessing.addAll(comReachList)
        //繰り返し処理中のリストにremoveとかしちゃうと動きがおかしくなるから一旦別の変数にコピー
        for (value in listForIterativeProcessing){ commonFunc(value) }

        if (comReachList.isNotEmpty()){chance=true}
    }

    //人間にリーチがかかってないか調べる(相手の勝利を阻止する)
    private fun checkCanIBlockCheckmate(){
        ////どこに入れれば防げるか探す
        fun commonFunc(line:Line){
            //そのライン上で相手のものになっていないマスを探す?
            for (mas in line.listGetter()){
                val size = mas.funcForDisplay()[0] //コマの大きさ
                val attribute = mas.funcForDisplay()[1] //人間のかコンピューターのか
                if (!mas.OccupiedByTheHuman()){ //相手のものになってないマスを見つけた
                    //すでに自分の大きいコマでブロックしてあるか調べる
                    if (attribute * size == -3){ humanReachList.remove(line) }//すでにブロックしてたらリストから消す
                    else{
                        if (size == bigPiece){ mas.addScore(-300) } //コマをおけば防げるところに相手の大きいコマがおいてないあったら諦める
                        break
                    }
                }
            }
        }

        val listForIterativeProcessing = mutableListOf<Line>()
        listForIterativeProcessing.addAll(humanReachList)
        //繰り返し処理中のリストにremoveとかしちゃうと動きがおかしくなるから一旦別の変数にコピー
        for (value in listForIterativeProcessing){ commonFunc(value) }
        //細かくしらべて本当にリーチがかかっているかつまだ止めをさせないならばブロックする
        if (humanReachList.isNotEmpty()){blocking = true}
    }

    //敵のコマの大きさを調べる
    fun howBigPiece(mas:Mas?):Int{ return mas!!.funcForDisplay()[0] }
////-----------------------
//評価値関係?-----------------
    //各マスに何が入っているのかしらべて評価値をつける
    //ついでにコンピューターのコマがどこにあるかも調べる
    private fun checkWhatIsInTheMas(){
        fun commonFunc(line: Line){
            for (mas in line.listGetter()){
                val size = mas.funcForDisplay()[0] //コマの大きさ
                val attribute = mas.funcForDisplay()[1] //人間のかコンピューターのか
                when{
                    size == bigPiece    && attribute == humanPiece -> { mas.addScore(-300) }//相手の大コマが置かれている
                    size == bigPiece    && attribute == comPiece -> {
                        //自分の大コマが置かれている
                        masInTheGreenBigPiece.add(mas)
//                        mas.addScore(-300)
                    }
                    size == middlePiece && attribute == comPiece -> { masInTheGreenMiddlePiece.add(mas) } //自分の中コマが置かれている
                    size == smallPiece && attribute == comPiece -> { masInTheGreenSmallPiece.add(mas) } //自分の小コマが置かれている
                }
            }
        }

        commonFunc(line1)
        commonFunc(line2)
        commonFunc(line3)
        commonFunc(line4)
    }

    //コマの周りを調べる
    private fun checkEachMas(){
        funcForCheckEachMas(line1)
        funcForCheckEachMas(line2)
        funcForCheckEachMas(line3)
        funcForCheckEachMas(line4)
        funcForCheckEachMas(lineA)
        funcForCheckEachMas(lineB)
        funcForCheckEachMas(lineC)
        funcForCheckEachMas(lineD)
        funcForCheckEachMas(lineS)
        funcForCheckEachMas(lineBS)
    }

    //ラインごとに分けて各マスに評価値を入れる
    private fun funcForCheckEachMas(line: Line){
        var standard:Mas? =null
        val linesList= line.listGetter()

        //基準のマスに自分のコマが入っていた場合
        fun standardIsM1(){
            if (line.humanPieceCounter() == 3 && !comReachList.contains(line) && standard?.funcForDisplay()!![0] == bigPiece){
                //ライン上は自分以外全部敵のコマだった
                //基準の自分のコマは大きいコマで相手のリーチをふせいでいる
                doNotMoveListBecauseItIsBlocking.add(standard!!) //防いでるコマは動かせない
            }

            //基準のコマがリーチを作るのに使われていたら動かさないリストに追加
            if (line.comPieceCounter() == 3 && comReachList.contains(line)){
                doNotMoveListBecauseItMakeReach.add(standard!!) //リーチを作っているから動かせない
            }
        }


        //基準のマスに相手のコマが入っていた､もしくは何も入ってなかった場合
        //後で編集
        fun standardIsP1AndEmp(){
            if (standard!!.funcForDisplay()[0] == bigPiece) {return} //基準が大きいコマだったら飛ばす (どうやってもコマが入らないから)
            if (humanReachList.contains(line)) { standard?.addScore(300) } //ここでコマを置いたら相手のリーチを防げる場合､基準のマスに評価値を追加
            //被せたり､空いているところに
                for (mas in linesList){
                    val size = mas.funcForDisplay()[0] //コマの大きさ
                    val attribute = mas.funcForDisplay()[1] //人間のかコンピューターのか
                    if (mas == standard) {continue} //基準のマスを調べようとしたらスキップ

                    //周りの各マスを調べて
                    //基準にしたマスに評価値を入れる
                    when(attribute){
                        empty      -> { inTheCaseOfEmp(standard!!)}//なにもはいってなかった時
                        comPiece   -> { inTheCaseOfM1(size,standard!!) }//自分のコマが入っていた場合
                        humanPiece -> { inTheCaseOfP1(size,standard!!) }//相手のコマが入っていた場合
                    }
                }
        }

        for (i in 0..3){
            //ライン上で一番前のマスから順に基準のマスにしていく
            standard=line.listGetter()[i]

            //基準となったマスに何が入っているかによってすることが違う
            when(standard.funcForDisplay()[1]){
                comPiece ->{standardIsM1()}
                humanPiece ->{standardIsP1AndEmp()}
                empty ->{standardIsP1AndEmp()}
            }
        }
    }

    //周りををしらべている時に空のコマがあった時の処理
    private fun inTheCaseOfEmp(mas:Mas){ mas.addScore(10) }

    //周りををしらべている時に自分のコマがあった時の処理
    //大きさの差を減らそうかな?
    private fun inTheCaseOfM1(size:Int,mas: Mas){
        when(size){
            smallPiece  ->{ mas.addScore(30) } //小 50
            middlePiece ->{ mas.addScore(30) } //中 55
            bigPiece    ->{ mas.addScore(30) } //大
        }
    }

    //周りををしらべている時に相手のコマがあった時の処理
    private fun inTheCaseOfP1(size:Int,mas: Mas){ if (size == bigPiece){mas.addScore(-5)} }
//-----------------------------
//配置
    //一番評価値が大きい場所を選ぶ
    private fun biggestScore(){
        val biggestScore = mutableListOf(-500,-500,-500,-500,-500) //[1番,2番､3番､4番､5番]

        fun setBiggestScore(line: Line){
            for (mas in line.listGetter()){
                when{
                    mas.scoreGetter() > biggestScore[0] -> //基準の一番大きい評価値よりも大きい値を見つけた
                        {
                            //基準の評価値たちを設定し直す
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
                        { biggestScore[4] = mas.scoreGetter() }
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

        setBiggestScore(line1)
        setBiggestScore(line2)
        setBiggestScore(line3)
        setBiggestScore(line4)

        addMas(line1)
        addMas(line2)
        addMas(line3)
        addMas(line4)
    }

    //起き場所を決める
    private fun chooseLocation(){
        fun commonFunc(scoreList:MutableList<Mas>):Boolean{
            var errorCount = 0
            while (true){
                if (errorCount>scoreList.size){ return false } //数回エラーがでたらループを抜ける
                destination = scoreList[(0 until scoreList.size).random()]
                if (!choosePickup()){ errorCount+=1 }//指定した場所におけなかったら他のこうほを探す
                else { return true } //おけるなら置く作業に進む
            }
        }

        //一番大きい評価値のマスから選んで行く
        if (commonFunc(mostBiggestScoreList)){ return }
        //一番大きい評価値のマスから選べなかった場合
        //二番目に大きい評価値のマスから選んで行く
        if (commonFunc(secondBiggestScoreList)){ return }

        //二番目に大きい評価値のマスから選べなかった場合
        //三番目に大きい評価値のマスから選んで行く
        if (commonFunc(thirdBiggestScoreList)){ return }

        if (commonFunc(fourthBiggestScoreList)){ return }
        if (commonFunc(fifthBiggestScoreList)){ return }
    }



    //取り出す場所を決める
    //その前に色々検証?
    private fun choosePickup():Boolean{
        //移動先におけるコマがあるか検証
        when(destination?.funcForDisplay()?.get(0)){
            bigPiece ->{return false} //そもそも大きいコマはどうやっても置けないかえら諦める
            middlePiece ->{ return pickUpPiece(bigPiece) } //中コマなら大きいコマのみおけるから大きいコマを取り出せるか調べる
            smallPiece -> {
                //小さいコマなら中コマか大コマを取り出せるかしらべる
                //中コマ->大コマと探す
                return if (pickUpPiece(middlePiece)){ true }
                else  { pickUpPiece(bigPiece) }
            }
            empty -> {
                //ここではいろんな条件に応じてうごかないと行けない
                if (chance){
                    return when {
                        pickUpPiece(smallPiece) -> { true } //空だから小さいやつを入れても平気
                        pickUpPiece(middlePiece) -> { true }
                        else -> { pickUpPiece(bigPiece) }
                    }
                }

                if (blocking){
                    //ブロックするときは大きいコマを使う
                    return when {
                        pickUpPiece(bigPiece) -> { true }
                        pickUpPiece(middlePiece) -> { true } //どうしても大きいコマが使えない時は中コマ
                        else -> { pickUpPiece(smallPiece) } //最悪小さいコマ
                    }
                }
                else{
                    //空いているなら何でも入れられる
                    //小コマ->中コマ->大きいと探す
                    if (temochiMiddle?.returnCount()!! == 0 && temochiBig?.returnCount()!! == 0){
                        if (pickUpPiece(smallPiece)){return true}
                    }
                    if (pickUpPiece(middlePiece)){return true}
                    if (pickUpPiece(bigPiece)) {return true}
                }
            }
        }
        return false
    }
    
    private fun pickUpPiece(size: Int):Boolean{
        var masInTheGreenPiece:MutableList<Mas> = mutableListOf()   
        when(size){
            smallPiece  -> {
                masInTheGreenPiece = masInTheGreenSmallPiece
                if (temochiSmall?.returnCount()!! > 0){
                    movingSource = temochiSmall //手持ちからだせるなら手持ちを移動元にする
                    return true
                }
            }
            middlePiece -> {
                masInTheGreenPiece = masInTheGreenMiddlePiece
                if (temochiMiddle?.returnCount()!! > 0){
                    movingSource = temochiMiddle //手持ちからだせるなら手持ちを移動元にする
                    return true
                }
            }
            bigPiece    -> {
                masInTheGreenPiece = masInTheGreenBigPiece
                if (temochiBig?.returnCount()!! > 0){
                    movingSource = temochiBig //手持ちからだせるなら手持ちを移動元にする
                    return true
                }
            }
        }

        when {
            chance -> { //止めをさせそうな時
                //差集合を使ってリーチを作っているコマ以外で動かせる大きいコマがあるか調べる
                val box = masInTheGreenPiece.minus(doNotMoveListBecauseItMakeReach)
                if (box.isNotEmpty()) {
                    //一つでも動かせるならそれを移動元にする
                    movingSource = box[(box.indices).random()]
                    return true
                }
            }
            blocking -> { //ブロックする必要がある場合
                //差集合を使って防いでいるコマ以外で動かせる大きいコマがあるか調べる
                val box = masInTheGreenPiece.minus(doNotMoveListBecauseItIsBlocking)
                if (box.isNotEmpty()) {
                    //一つでも動かせるならそれを移動元にする
                    movingSource = box[(box.indices).random()]
                    return true
                }
            }
            else ->{
                //差集合を使って動かせる大きいコマがあるか調べる
                val box1 = masInTheGreenPiece.minus(doNotMoveListBecauseItIsBlocking) //ブロックに使っている
                val box2 = masInTheGreenPiece.minus(doNotMoveListBecauseItMakeReach) //リーチにつかっている
                val box = box1+box2 //1,2の条件に合わないやつを選ぶ
                if (box.isNotEmpty()) {
                    //一つでも動かせるならそれを移動元にする
                    movingSource = box[(box.indices).random()]
                    return true
                }
            }
        }
        return false //だめならだめと返す
    }

    //最初のターンの定石を実行
    private fun firstTurn(){
        while (true){
            movingSource= temochiBig
            when((0..3).random()){
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
        if (comReachList.isNotEmpty()){ checkCanICheckmate() }
        //相手にリーチがかかっていた
        if (humanReachList.isNotEmpty()){ checkCanIBlockCheckmate()}

        standardProcessing()
    }

    //特定条件以外での処理
    private fun standardProcessing(){
        checkWhatIsInTheMas()
        checkEachMas()
        doNotMoveList.addAll(doNotMoveListBecauseItMakeReach)
        doNotMoveList.addAll(doNotMoveListBecauseItIsBlocking)
        biggestScore()
        chooseLocation()
    }

    fun destinationGetter(): Mas?{ return destination }

    fun movingSourceGetter():Any?{ return movingSource }

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
        doNotMoveListBecauseItIsBlocking.clear()
        doNotMoveListBecauseItMakeReach.clear()
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
}


