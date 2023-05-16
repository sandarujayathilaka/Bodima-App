package com.example.bodima.testvalidations

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)

class CalValidationTest{

    @Test
    fun validateTransportFee() {

        val amount = 100
        val days = 5
        val result = CalValidation.validateTransportCost(amount, days)
        assertEquals(500,result)
    }

    @Test
    fun validateFoodFee() {

        val amount = 200
        val days = 30
        val result = CalValidation.validateFoodCost(amount, days)
        assertEquals(6000,result)
    }

    @Test
    fun validateTotalExpences() {

        val accFeeVal = 100
        val eleFeeVal = 500
        val watFeeVal = 400
        val otherVal = 200
        val amount = 200
        val days = 30
        val result = CalValidation.validateTotalCost(accFeeVal,eleFeeVal,watFeeVal,otherVal,amount,days)
        assertEquals(13200,result)
    }

    @Test
    fun validateNetIncomeCal() {

        val accFeeVal = 100
        val eleFeeVal = 500
        val watFeeVal = 400
        val otherVal = 200
        val amount = 200
        val income =25000
        val days = 30
        val result = CalValidation.netIncomeValidate(accFeeVal,eleFeeVal,watFeeVal,otherVal,amount,days,income)
        assertEquals(11800,result)
    }
















}