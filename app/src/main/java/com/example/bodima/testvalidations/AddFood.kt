package com.example.bodima.testvalidations

object AddFood {

    fun validateInput(foodName:String,foodPrice:String,foodDescription:String,foodMobile:String,foodStartTime:String,foodMeridiumStart:String,
                      foodEndTime:String,foodMeridiumEnd:String,foodAddress:String,foodCategory:String,foodType:String,foodImage:String):Boolean{

        return !(foodName.isEmpty() || foodPrice.isEmpty() || foodPrice.toString().toInt() <= 0 || foodDescription.isEmpty() ||
                foodMobile.isEmpty() || foodMobile.length != 10 || foodStartTime.isEmpty() || foodMeridiumStart.isEmpty() ||
                foodEndTime.isEmpty() || foodMeridiumEnd.isEmpty() || foodAddress.isEmpty() || foodCategory.isEmpty() ||
                foodType.isEmpty() || foodImage.isEmpty())
    }
}