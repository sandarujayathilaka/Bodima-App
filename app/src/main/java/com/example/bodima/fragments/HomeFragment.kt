package com.example.bodima.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.bodima.R


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        val imgHouse: ImageView = view.findViewById(R.id.HomeImg)
        val imgCal: ImageView = view.findViewById(R.id.calimg)
        val imgFood: ImageView = view.findViewById(R.id.foodImg)
        val imgGro: ImageView = view.findViewById(R.id.GroceryImg)

        imgHouse.setOnClickListener {

            val fragment = HouseCategoryFragment() // Create an instance of the other fragment you want to open
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment) // Replace the current fragment with the new fragment
            transaction.addToBackStack(null) // Add the transaction to the back stack
            transaction.commit() // Commit the transaction.
        }

        imgCal.setOnClickListener {

            val fragment = CalculatorFragment() // Create an instance of the other fragment you want to open
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment) // Replace the current fragment with the new fragment
            transaction.addToBackStack(null) // Add the transaction to the back stack
            transaction.commit() // Commit the transaction.
        }

        imgFood.setOnClickListener {

            val fragment = FoodCategoryFragment() // Create an instance of the other fragment you want to open
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment) // Replace the current fragment with the new fragment
            transaction.addToBackStack(null) // Add the transaction to the back stack
            transaction.commit() // Commit the transaction.
        }



        imgGro.setOnClickListener {

            val fragment = GroceryCategoryFragment() // Create an instance of the other fragment you want to open
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment) // Replace the current fragment with the new fragment
            transaction.addToBackStack(null) // Add the transaction to the back stack
            transaction.commit() // Commit the transaction.
        }

        return view;
    }
}