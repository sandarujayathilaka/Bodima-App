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

        navHome = findViewById(R.id.homenavcal)
        navUser = findViewById(R.id.usernavcal)
        navAdd = findViewById(R.id.addnavcal)

        val totalFee = intent.getDoubleExtra("totalfee", 0.0)
        val restFee = intent.getDoubleExtra("restfee", 0.0)

        val dispalyFee = findViewById<TextView>(R.id.displayfee)
        dispalyFee.text =totalFee.toString()

        val dispalyRestFee = findViewById<TextView>(R.id.restfee)
        dispalyRestFee.text =restFee.toString()

        navAdd.setOnClickListener {
            navAdd.setImageResource(R.drawable.clickadd)
            navUser.setImageResource(R.drawable.usernav)
            navHome.setImageResource(R.drawable.homenav)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,fragmentItem)
                addToBackStack(null)
                commit()
            }
        }

        navHome.setOnClickListener {
            navAdd.setImageResource(R.drawable.navadd)
            navUser.setImageResource(R.drawable.usernav)
            navHome.setImageResource(R.drawable.clickedhome)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,fragmentHome)
                addToBackStack(null)
                commit()
            }
        }



    }
}