package com.example.gobblet5

import kotlin.random.Random

class RandomCom {
    var randomNumber = 0
    var turnCount = 0
    var errorCount = 0
    var iWantPickup = "none"
    var TorM = "none"

//ID
    val IDBig = "Big"
    val IDMiddle = "Middle"
    val IDSmall = "Small"
    val IDTemochi = "Temochi"
    val IDMas = "Mas"

    fun countUpTurnCount(){
        turnCount+=1
    }

    fun countUpErrorCount(){
        errorCount += 1
    }

    fun resetErrorCount(){
        errorCount = 0
    }

    fun returnErrorCount():Int{
        return errorCount
    }

    fun setRandomNumber(){
        randomNumber = (1..10).random()
    }

    fun canItDividable2():Boolean{
        return randomNumber%2==0
    }

    fun canItDividable3():Boolean{
        return randomNumber%3==0
    }

    fun canItDividable5():Boolean{
        return randomNumber%5==0
    }

    fun whatWillToDo():String{
        return "dammy"
    }

    fun whatWillToDo4OrMoreAndLessThan7(){

    }

    fun whatWillToDo7OrMore(){

    }

    fun whichTemochiToPickup():String{
        return "dammy"
    }

    fun whichTemochiToPickupLessThen4(){

    }

    fun whichTemochiToPickup4OrMoreAndLessThan7(){

    }

    fun whichTemochiToPickup7OrMore(){

    }

    fun whichMasToDo():Int{
        return (0..15).random()
    }
}