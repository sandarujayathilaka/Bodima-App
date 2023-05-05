package com.example.bodima.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.bodima.R
import com.example.bodima.fragments.HomeFragment
import com.example.bodima.fragments.InsertItemMainFragment

lateinit var navHome: ImageView
lateinit var navUser: ImageView
lateinit var navAdd: ImageView

val fragmentItem = InsertItemMainFragment()
val fragmentHome = HomeFragment()

class CalDisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cal_display)


        val totalFee = intent.getDoubleExtra("totalfee", 0.0)
        val restFee = intent.getDoubleExtra("restfee", 0.0)

        val dispalyFee = findViewById<TextView>(R.id.displayfee)
        dispalyFee.text =totalFee.toString()

        val dispalyRestFee = findViewById<TextView>(R.id.restfee)
        dispalyRestFee.text =restFee.toString()


    }
}