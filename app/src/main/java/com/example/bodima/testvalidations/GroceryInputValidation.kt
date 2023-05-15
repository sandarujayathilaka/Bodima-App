package com.example.bodima.testvalidations

object GroceryInputValidation {

    fun validateGroceryInput(
        title: String,
        subtitle: String,
        discount: String,
        price: String,
        tpNO: String,
        locate: String,
        about: String,
        category: String,
        groceryimg: String,
    ):Boolean{

        return !(title.isEmpty() || subtitle.isEmpty() || discount.toString().toInt() <= 0 || price.toString().toInt() <= 0|| locate.isEmpty()
                || tpNO.length != 10 || about.isEmpty() || category.isEmpty() || groceryimg.isEmpty() )
    }



    fun validateGroceryUpdate(
        title: String,
        subtitle: String,
        discount: String,
        price: String,
        tpNO: String,
        locate: String,
        about: String,
        category: String,
        groceryimg: String,
    ):Boolean{

        return !(title.isEmpty() || subtitle.isEmpty() || discount.toString().toInt() <= 0 || price.toString().toInt() <= 0|| locate.isEmpty()
                || tpNO.length != 10 || about.isEmpty() || category.isEmpty() || groceryimg.isEmpty() )
    }




    fun validateDiscount(amount: Int, price: Int, discount: Int): Int {
        return (amount * (price - (price * discount / 100.0))).toInt()
    }




}


