package com.example.bodima.testvalidations

class FoodQuantity(val foodPrice:Int ,val quantity:Int) {


    fun validateCount(foodPrice:Int,quantity:Int):Boolean{
        return !(foodPrice <= 0 || quantity <= 0)
    }

    fun calculation() = foodPrice * quantity;
}