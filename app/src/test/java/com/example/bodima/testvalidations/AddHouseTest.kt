package com.example.bodima.testvalidations

import com.example.bodima.fragments.AddFood
import org.junit.Assert.*
import org.junit.Test

class AddHouseTest{

    @Test
    fun houseInputValidate(){

        val location = "Matara"
        val beds = "3"
        val description = "Luxery Room"
        val mobile = "0771231232"
        val address = "Malabe,Kaduwela"
        val baths = "2"
        val title= "Room Near SLIIT"
        val price = "8000"
        val category = "Single-Room"


        val result = AddHouse.validateInput(location,beds,baths,address,title,price,mobile,description,category)

        assertThat(result).isFalse()
    }

    @Test
    fun whenAddInputIsInvalid(){
        val id="-NUPlWnbTT0iHJDgeW0G"
        val name = "Rice"
        val price = "0"
        val description = "Rice with curry"
        val mobile = ""
        val address = "No:25, Malabe"
        val startTime = "5"
        val startMeridum = "A.M"
        val endTime = "8"
        val endMeridum = "P.M"
        val image = ""
        val type = "Veg"
        val category = "Lunch"
        val email = "abc@gmail.com"

        val result = AddFood.validateInput(id,name,price,description,mobile,startTime,startMeridum,endTime,endMeridum,address,category,type,image,email)

        assertThat(result).isEqualTo(false)

    }







}