package com.example.bodima.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.bodima.R
import com.example.bodima.activities.CalDisplayActivity


class CalculatorFragment : Fragment() {


   lateinit var accFee: EditText
    lateinit var eleFee: EditText
    lateinit var watFee: EditText
     lateinit var traFee: EditText
     lateinit var tdays: EditText
    lateinit var foodFee: EditText
     lateinit var fdays: EditText
     lateinit var other: EditText
    lateinit var income: EditText
     lateinit var calbtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calculator, container, false)

        accFee = view.findViewById(R.id.acctext)
        eleFee = view.findViewById(R.id.elebilltext)
        watFee = view.findViewById(R.id.waterbilltext)
        traFee = view.findViewById(R.id.tcosttext)
        tdays = view.findViewById(R.id.transdaystext)
        foodFee = view.findViewById(R.id.foodexpentext)
        fdays = view.findViewById(R.id.fdaystext)
        other = view.findViewById(R.id.othertext)
        income = view.findViewById(R.id.incometext)
        calbtn = view.findViewById(R.id.calBtn)
        calbtn.setOnClickListener {


            calbtn.setOnClickListener {
                val accFeeVal = if (accFee.text.toString().isNotEmpty()) accFee.text.toString()
                    .toDouble() else 0.0
                val eleFeeVal = if (eleFee.text.toString().isNotEmpty()) eleFee.text.toString()
                    .toDouble() else 0.0
                val watFeeVal = if (watFee.text.toString().isNotEmpty()) watFee.text.toString()
                    .toDouble() else 0.0
                val otherVal = if (other.text.toString().isNotEmpty()) other.text.toString()
                    .toDouble() else 0.0
                val incomeVal = if (income.text.toString().isNotEmpty()) income.text.toString().toDouble() else 0.0


                val total = accFeeVal + eleFeeVal + watFeeVal + totalFoodCost() + totalTransportCost() + otherVal
                val rest = incomeVal - total
                Log.d("name","$rest")

                val intent = Intent(activity, CalDisplayActivity::class.java)
                 intent.putExtra("totalfee", total)
                intent.putExtra("restfee", rest)
                startActivity(intent)
            }
        }

        return view
    }

     fun totalTransportCost(): Double {
        val days = if (tdays.text.toString().isNotEmpty()) tdays.text.toString().toInt() else 0
        val tfee = if (traFee.text.toString().isNotEmpty()) traFee.text.toString().toDouble() else 0.0
        return days * tfee
    }

    private fun totalFoodCost(): Double {
        val days = if (fdays.text.toString().isNotEmpty()) fdays.text.toString().toInt() else 0
        val fee = if (foodFee.text.toString().isNotEmpty()) foodFee.text.toString().toDouble() else 0.0
        return days * fee
    }

}