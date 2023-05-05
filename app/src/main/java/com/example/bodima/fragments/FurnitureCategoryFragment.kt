package com.example.bodima.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.bodima.R
import com.example.bodima.activities.RecycleFurnitureActivity
import com.example.bodima.activities.RecycleHouseActivity

class FurnitureCategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_furniture_category, container, false)
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

        val brandnewCard = view.findViewById<CardView>(R.id.brandnewcard)
        val usedCard = view.findViewById<CardView>(R.id.usedcard)
        val reconditionedCard = view.findViewById<CardView>(R.id.reconditionedcard)


        brandnewCard.setOnClickListener {
            val intent = Intent(activity, RecycleFurnitureActivity::class.java)
            intent.putExtra("category", "Brand-new")
            startActivity(intent)
        }

        usedCard.setOnClickListener {
            val intent = Intent(activity, RecycleFurnitureActivity::class.java)
            intent.putExtra("category", "Used")
            startActivity(intent)
        }

        reconditionedCard.setOnClickListener {
            val intent = Intent(activity, RecycleFurnitureActivity::class.java)
            intent.putExtra("category", "Reconditioned")
            startActivity(intent)
        }

        return view
    }

}