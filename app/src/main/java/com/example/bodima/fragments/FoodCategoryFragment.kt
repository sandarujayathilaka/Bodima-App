package com.example.bodima.fragments


import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import android.content.Intent
import com.example.bodima.R
import com.example.bodima.activities.RecyclerFood

class FoodCategoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food_category, container, false)
        val breakfastCard = view.findViewById<CardView>(R.id.breakfast)
        val lunchCard = view.findViewById<CardView>(R.id.lunch)
        val dinnerCard = view.findViewById<CardView>(R.id.dinner)
        val snackCard = view.findViewById<CardView>(R.id.snack)

        breakfastCard.setOnClickListener {
            val intent = Intent(activity, RecyclerFood::class.java)
            intent.putExtra("foodCategory", "Breakfast")
            startActivity(intent)
        }

        lunchCard.setOnClickListener {
            val intent = Intent(activity, RecyclerFood::class.java)
            intent.putExtra("foodCategory", "Lunch")
            startActivity(intent)
        }

        dinnerCard.setOnClickListener {
            val intent = Intent(activity, RecyclerFood::class.java)
            intent.putExtra("foodCategory", "Dinner")
            startActivity(intent)
        }

        snackCard.setOnClickListener {
            val intent = Intent(activity, RecyclerFood::class.java)
            intent.putExtra("foodCategory", "Snacks and juices")
            startActivity(intent)
        }

        return view
    }
}