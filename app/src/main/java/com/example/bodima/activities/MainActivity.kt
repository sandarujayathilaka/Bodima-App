package com.example.bodima.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.bodima.fragments.HomeFragment
import com.example.bodima.fragments.InsertItemMainFragment
import com.example.bodima.R
import com.example.bodima.fragments.UserProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var navHome: ImageView
    lateinit var navUser: ImageView
    lateinit var navAdd: ImageView
    val fragmentItem = InsertItemMainFragment()
    val fragmentHome = HomeFragment()
    val fragmentUser = UserProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHome = findViewById(R.id.homenav)
        navUser = findViewById(R.id.usernav)
        navAdd = findViewById(R.id.addnav)

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

        navUser.setOnClickListener {
            navAdd.setImageResource(R.drawable.navadd)
            navUser.setImageResource(R.drawable.clickeduser)
            navHome.setImageResource(R.drawable.homenav)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,fragmentUser)
                addToBackStack(null)
                commit()
            }
        }

    }
}