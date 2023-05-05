package com.example.bodima.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.bodima.R

class InsertItemMainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       var view = inflater.inflate(R.layout.fragment_insert_item_main, container, false)


        val addhouse:ImageView =view.findViewById(R.id.insertHome)
        val addfood:ImageView =view.findViewById(R.id.insertFood)
        val imgGro: ImageView = view.findViewById(R.id.insertGrocery)

        addhouse.setOnClickListener {
            val fragment = HouseAddFragment() // Create an instance of the other fragment you want to open
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment) // Replace the current fragment with the new fragment
            transaction.addToBackStack(null) // Add the transaction to the back stack
            transaction.commit() // Commit the transaction
        }

        addfood.setOnClickListener {
            val fragment = AddFood() // Create an instance of the other fragment you want to open
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment) // Replace the current fragment with the new fragment
            transaction.addToBackStack(null) // Add the transaction to the back stack
            transaction.commit() // Commit the transaction
        }



        imgGro.setOnClickListener {

            val fragment = GroceryAddFragment() // Create an instance of the other fragment you want to open
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment) // Replace the current fragment with the new fragment
            transaction.addToBackStack(null) // Add the transaction to the back stack
            transaction.commit() // Commit the transaction.
        }

        return view;
    }

}