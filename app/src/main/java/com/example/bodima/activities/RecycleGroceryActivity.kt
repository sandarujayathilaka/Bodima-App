package com.example.bodima.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.models.Grocery
import com.example.bodima.adapters.GroceryAdapter
import com.example.bodima.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RecycleGroceryActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var groceryRecyclerView: RecyclerView
    private lateinit var groceryArrayList: ArrayList<Grocery>
    private lateinit var groceryCategory: String

    //    private lateinit var houseType: Spinner
    private lateinit var resultvalue: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_grocery)

        // Get the food type from the intent extras
        groceryCategory = intent.getStringExtra("category") ?: ""
        resultvalue = findViewById<TextView>(R.id.groceryresultvalue)

//        houseType = findViewById<Spinner>(R.id.foodtype)

        groceryRecyclerView = findViewById(R.id.grocery_item)
        groceryRecyclerView.layoutManager = LinearLayoutManager(this)

        // val adapter = FoodAdapter(foodArrayList)

// set the adapter to the RecyclerView
        //foodRecyclerView.adapter = adapter
        groceryRecyclerView.hasFixedSize()
        groceryArrayList = arrayListOf()


//        foodType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                val selectedFoodType = parent.getItemAtPosition(position).toString()
//                //foodType.setSelection(0)
//                getFoodData(selectedFoodType)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                //  foodType.setSelection(0)
//                getFoodData("All")
//            }
//        }

//        Set the default selected item to "All"
//        foodType.setSelection(0)
        getGroceryData()
    }

    private fun getGroceryData() {

        val database = Firebase.database
        val myRef = database.reference.child("Grocery")
        val query = myRef.orderByChild("category").equalTo(groceryCategory)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groceryArrayList.clear() // clear the previous data from the list
                if (snapshot.exists()) {
                    for (grocerysnapshot in snapshot.children) {
                        val item = grocerysnapshot.getValue(Grocery::class.java)
                        groceryArrayList.add(item!!)
                    }
                    groceryRecyclerView.adapter = GroceryAdapter(groceryArrayList, object : GroceryAdapter.OnItemClickListener {
                        override fun onItemClick(item: Grocery) {
                            val intent = Intent(this@RecycleGroceryActivity, SingleGroceryActivity::class.java)
                            val groceryId: String? = item.id
                            intent.putExtra("itemId", groceryId?.toString())
                            startActivity(intent)
                        }
                    })


                    resultvalue.text = groceryArrayList.size.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        }
        )

    }

}
