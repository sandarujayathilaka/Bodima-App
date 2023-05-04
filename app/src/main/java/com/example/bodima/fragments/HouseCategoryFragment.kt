package com.example.bodima.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.bodima.R
import com.example.bodima.activities.RecycleHouseActivity

class HouseCategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_house_category, container, false)
//
//        val annex: LinearLayout = view.findViewById(R.id.annexlayout)
//
//        annex.setOnClickListener {
//            val fragment = HouseAddFragment() // Create an instance of the other fragment you want to open
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragmentContainerView, fragment) // Replace the current fragment with the new fragment
//            transaction.addToBackStack(null) // Add the transaction to the back stack
//            transaction.commit() // Commit the transaction
//        }

        val annexCard = view.findViewById<CardView>(R.id.annexcard)
        val roomCard = view.findViewById<CardView>(R.id.roomcard)
        val homeCard = view.findViewById<CardView>(R.id.homecard)


       annexCard.setOnClickListener {
            val intent = Intent(activity, RecycleHouseActivity::class.java)
            intent.putExtra("category", "Annex")
            startActivity(intent)
        }

       roomCard.setOnClickListener {
            val intent = Intent(activity, RecycleHouseActivity::class.java)
            intent.putExtra("category", "Single-Room")
            startActivity(intent)
        }

        homeCard.setOnClickListener {
            val intent = Intent(activity, RecycleHouseActivity::class.java)
            intent.putExtra("category", "Home")
            startActivity(intent)
        }

        return view
    }
}
