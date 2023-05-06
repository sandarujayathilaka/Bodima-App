package com.example.bodima.testvalidations


import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class FoodQuantityTest{

    private val foodQuantity = FoodQuantity(100,5)

    @Test
    fun whenInputIsValid(){
        val foodPrice = 100
        val quantity = 5
        val result = foodQuantity.validateCount(foodPrice,quantity)

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun whenInputIsInvalid(){
        val price = 0
        val quantity = 5


        val result = foodQuantity.validateCount(price,quantity)

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun whenCalculationIsCorrect(){

        assertEquals(500,foodQuantity.calculation())
    }



}
