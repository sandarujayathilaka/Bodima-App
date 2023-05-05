package com.example.bodima.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.bodima.R
import com.example.bodima.activities.RecyclerUserFood
import com.example.bodima.activities.RecyclerUserGroceryActivity
import com.example.bodima.activities.RecyclerUserHouseActivity

class UserProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_user_profile, container, false)

        val houseEdit = view.findViewById<ImageView>(R.id.HomeImgEdit)
        val foodEdit = view.findViewById<ImageView>(R.id.foodImgEdit)
        val GroceryEdit = view.findViewById<ImageView>(R.id.GroceryImgEdit)

        houseEdit.setOnClickListener{

            val intent = Intent(activity, RecyclerUserHouseActivity::class.java)
            startActivity(intent)

        }

        foodEdit.setOnClickListener{

            val intent = Intent(activity, RecyclerUserFood::class.java)
            startActivity(intent)

        }



        GroceryEdit.setOnClickListener{

            val intent = Intent(activity, RecyclerUserGroceryActivity::class.java)
            startActivity(intent)

        }


        return view
    }




}