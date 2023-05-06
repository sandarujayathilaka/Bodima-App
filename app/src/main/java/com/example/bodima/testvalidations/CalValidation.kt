package com.example.bodima.testvalidations

object CalValidation {


    fun validateTransportCost(amount:Int,days:Int): Int {

        return (amount*days)

    }


    fun validateFoodCost(amount:Int,days:Int): Int {

        return (amount*days)

    }

    fun validateTotalCost(accFeeVal:Int,eleFeeVal:Int,watFeeVal:Int,otherVal:Int,amount:Int,days:Int): Int {

        val total = accFeeVal+eleFeeVal+watFeeVal+otherVal+ validateFoodCost(amount,days)+ validateTransportCost(amount,days)
        return (total)

    }

    fun netIncomeValidate(accFeeVal:Int,eleFeeVal:Int,watFeeVal:Int,otherVal:Int,amount:Int,days:Int,income:Int): Int {

        val rest = income - validateTotalCost(accFeeVal,eleFeeVal,watFeeVal,otherVal,amount,days)
        return (rest)

    }





}