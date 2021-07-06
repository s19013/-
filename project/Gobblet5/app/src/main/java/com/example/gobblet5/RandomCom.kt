package com.example.gobblet5

import kotlin.random.Random

class RandomCom {
    var returnBox = mutableListOf<String>()



    fun pickup():Int{
        return 0
    }

    fun TemochiPickup():Int{
        return (1..3).random()
    }

    fun X():Int{
        return (1..4).random()
    }

    fun Y():Int{
        return (1..4).random()
    }


}