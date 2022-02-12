package com.game.gobblet5

//bord <-> line <-> mas

class Bord {
    //ます
     val A1 = Mas("A1")
     val B1 = Mas("B1")
     val C1 = Mas("C1")
     val D1 = Mas("D1")
     val A2 = Mas("A2")
     val B2 = Mas("B2")
     val C2 = Mas("C2")
     val D2 = Mas("D2")
     val A3 = Mas("A3")
     val B3 = Mas("B3")
     val C3 = Mas("C3")
     val D3 = Mas("D3")
     val A4 = Mas("A4")
     val B4 = Mas("B4")
     val C4 = Mas("C4")
     val D4 = Mas("D4")

    //ライン
     val line1:Line  = Line("L1")
     val line2:Line  = Line("L2")
     val line3:Line  = Line("L3")
     val line4:Line  = Line("L4")
     val lineA:Line  = Line("LA")
     val lineB:Line  = Line("LB")
     val lineC:Line  = Line("LC")
     val lineD:Line  = Line("LD")
     val lineS:Line  = Line("LS")
     val lineBS:Line = Line("LBS")

     val allLine = listOf<Line>(
         line1, line2,line3,line4,
         lineA,lineB,lineC,lineD,
         lineS,lineBS)

    //各ラインにマスを入れる
    fun iniLines(){
        line1.listSetter(mutableListOf(A1, B1, C1, D1))//l1
        line2.listSetter(mutableListOf(A2, B2, C2, D2))//l2
        line3.listSetter(mutableListOf(A3, B3, C3, D3))//l3
        line4.listSetter(mutableListOf(A4, B4, C4, D4))//l4
        lineA.listSetter(mutableListOf(A1, A2, A3, A4))//lA
        lineB.listSetter(mutableListOf(B1, B2, B3, B4))//lB
        lineC.listSetter(mutableListOf(C1, C2, C3, C4))//lC
        lineD.listSetter(mutableListOf(D1, D2, D3, D4))//lD
        lineS.listSetter(mutableListOf(A1, B2, C3, D4))//lS
        lineBS.listSetter(mutableListOf(A4, B3, C2, D1))//lBS
    }



}