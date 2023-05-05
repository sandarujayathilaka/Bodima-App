package com.example.bodima.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.bodima.R
import com.example.bodima.activities.RecycleGroceryActivity

class GroceryCategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_grocery_category, container, false)
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
        val bevaCard = view.findViewById<CardView>(R.id.beva)
        val dairyCard  = view.findViewById<CardView>(R.id.dairy)
        val breadCard  = view.findViewById<CardView>(R.id.bread)


        bevaCard.setOnClickListener {
            val intent = Intent(activity, RecycleGroceryActivity::class.java)
            intent.putExtra("category", "Beverages")
            startActivity(intent)
        }

        dairyCard.setOnClickListener {
            val intent = Intent(activity, RecycleGroceryActivity::class.java)
            intent.putExtra("category", "Dairy")
            startActivity(intent)
        }

        breadCard.setOnClickListener {
            val intent = Intent(activity, RecycleGroceryActivity::class.java)
            intent.putExtra("category", "Bread")
            startActivity(intent)
        }

        return view;
    }
}
