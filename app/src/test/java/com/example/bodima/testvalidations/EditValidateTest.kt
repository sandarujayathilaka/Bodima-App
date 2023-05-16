package com.example.bodima.testvalidations

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EditValidateTest {


    @Test
    fun whenEditInputIsValid() {
        val id="-NUPlWnbTT0iHJDgeW0G"
        val name = "Rice"
        val price = "100"
        val description = "Rice with curry"
        val mobile = "0771231232"
        val address = "No:25, Malabe"
        val startTime = "5"
        val startMeridum = "A.M"
        val endTime = "8"
        val endMeridum = "P.M"
        val image = "iVBORw0KGgoAAAANSUhEUgAAA8AAAAUACAIAAAAHjKxCAAAAA3NCSVQICAjb4U"
        val type = "Veg"
        val category = "Lunch"
        val email = "abc@gmail.com"

        val result = EditFood.validateInput(id,name,price,description,mobile,startTime,startMeridum,endTime,endMeridum,address,category,
            type,image,email)

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun whenEditInputIsInvalid() {
        val id="-NUPlWnbTT0iHJDgeW0G"
        val name = "Rice"
        val price = "0"
        val description = "Rice with curry"
        val mobile = "0771231232"
        val address = "No:25, Malabe"
        val startTime = "5"
        val startMeridum = "A.M"
        val endTime = "8"
        val endMeridum = "P.M"
        val image = ""
        val type = "Veg"
        val category = "Lunch"
        val email = "abc@gmail.com"

        val result = EditFood.validateInput(id,name,price,description,mobile,startTime,startMeridum,endTime,endMeridum,address,category,
            type,image,email)

        assertThat(result).isEqualTo(false)
    }
}
