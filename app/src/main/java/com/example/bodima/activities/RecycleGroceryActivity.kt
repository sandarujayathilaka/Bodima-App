package com.example.bodima.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.models.Grocery
import com.example.bodima.adapters.GroceryAdapter
import com.example.bodima.R
import com.example.bodima.adapters.FoodAdapter
import com.example.bodima.models.Foods
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class RecycleGroceryActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var groceryRecyclerView: RecyclerView
    private lateinit var groceryArrayList: ArrayList<Grocery>
    private lateinit var groceryCategory: String
    private lateinit var tempArrayList: ArrayList<Grocery>

    private lateinit var resultvalue: TextView

    private lateinit var search: SearchView

    companion object {
        @JvmField val TAG: String = RecycleGroceryActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_grocery)

        // Get the Grocery type from the intent extras
        groceryCategory = intent.getStringExtra("category") ?: ""
        resultvalue = findViewById<TextView>(R.id.groceryresultvalue)



        groceryRecyclerView = findViewById(R.id.grocery_item)
        groceryRecyclerView.layoutManager = LinearLayoutManager(this)



        // val adapter = FoodAdapter(foodArrayList)

// set the adapter to the RecyclerView
        //foodRecyclerView.adapter = adapter
        groceryRecyclerView.hasFixedSize()
        groceryArrayList = arrayListOf()
        tempArrayList = arrayListOf()
        val locale = Locale.getDefault()

        search = findViewById<SearchView>(R.id.grocerysearch)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Refresh the list with the updated search query
                tempArrayList.clear()

                val searchtext = newText!!.toLowerCase(locale)

                if (searchtext.isNotEmpty()) {
                    groceryArrayList.forEach {
                        if (it.title!!.toLowerCase(locale).contains(searchtext) ||
                            it.locate!!.toLowerCase(locale).contains(searchtext))  {
                            tempArrayList.add(it)
                            resultvalue.text = tempArrayList.size.toString()
                        }
                    }
                    if (tempArrayList.isEmpty()) {
                        resultvalue.text = "0"
                        Toast.makeText(this@RecycleGroceryActivity, "No result found", Toast.LENGTH_SHORT).show()
                    }

                    groceryRecyclerView.adapter!!.notifyDataSetChanged()


                } else {

                    tempArrayList.clear()
                    tempArrayList.addAll(groceryArrayList)
                    resultvalue.text = tempArrayList.size.toString()
                    groceryRecyclerView.adapter!!.notifyDataSetChanged()
                }

                // getFoodData(foodType.selectedItem.toString(), newText ?: "")
                return false
            }
        })



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
                    for (foodsnapshot in snapshot.children) {
                        val item = foodsnapshot.getValue(Grocery::class.java)
                        groceryArrayList.add(item!!)
                    }
                    tempArrayList.addAll(groceryArrayList)
                    groceryRecyclerView.adapter = GroceryAdapter(tempArrayList, object : GroceryAdapter.OnItemClickListener {
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
                // Handle cancel event
            }
        })
    }


    }


