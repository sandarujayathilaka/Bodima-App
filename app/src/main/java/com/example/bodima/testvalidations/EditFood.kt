package com.example.bodima.testvalidations

object EditFood {
    fun validateInput(foodId:String,foodName:String,foodPrice:String,foodDescription:String,foodMobile:String,foodStartTime:String,
                      foodMeridiumStart:String,foodEndTime:String,foodMeridiumEnd:String,foodAddress:String,foodCategory:String,
                      foodType:String,foodImage:String,foodEmail:String):Boolean{

        return !(foodId.isEmpty() || foodName.isEmpty() || foodPrice.isEmpty() || foodPrice.toString().toInt() <= 0 ||
                foodDescription.isEmpty() || foodMobile.isEmpty() || foodMobile.length != 10 || foodStartTime.isEmpty() ||
                foodMeridiumStart.isEmpty() || foodEndTime.isEmpty() || foodMeridiumEnd.isEmpty() || foodAddress.isEmpty() ||
                foodCategory.isEmpty() || foodType.isEmpty() || foodImage.isEmpty() || foodEmail.isEmpty())
    }
}