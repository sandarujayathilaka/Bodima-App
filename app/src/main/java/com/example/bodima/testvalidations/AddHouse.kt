package com.example.bodima.testvalidations

object AddHouse {

    fun validateInput(location:String,beds:String,baths:String,address:String,title:String,price:String,mobile:String,description:String,category:String):Boolean{

        return !(location.isEmpty() || beds.isEmpty() || baths.isEmpty() || address.isEmpty() ||
                title.isEmpty() || price.isEmpty() || mobile.isEmpty() || description.isEmpty() || mobile.length != 10
                || category =="Select Category"
                )
    }

}