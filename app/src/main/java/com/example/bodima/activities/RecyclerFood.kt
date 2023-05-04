package com.example.bodima.activities

import com.example.bodima.models.Foods
import com.example.bodima.adapters.FoodAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class RecyclerFood : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var foodArrayList: ArrayList<Foods>
    private lateinit var tempArrayList: ArrayList<Foods>

    private lateinit var foodCategory: String
    private lateinit var foodType: Spinner
    private lateinit var resultvalue: TextView
    private lateinit var search: SearchView



    companion object {
        @JvmField val TAG: String = RecyclerFood::class.java.simpleName
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_food)

        // Get the food type from the intent extras
        foodCategory = intent.getStringExtra("foodCategory") ?: ""
        // val category = intent.getStringExtra("category") ?: ""

        resultvalue = findViewById<TextView>(R.id.resultvalue)
        foodType = findViewById<Spinner>(R.id.foodtype)
        foodRecyclerView = findViewById(R.id.item_food)
        foodRecyclerView.layoutManager = LinearLayoutManager(this)

        foodRecyclerView.hasFixedSize()
        foodArrayList = arrayListOf()
        tempArrayList = arrayListOf()
        val locale = Locale.getDefault()

        search = findViewById<SearchView>(R.id.foodsearch)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Refresh the list with the updated search query
                tempArrayList.clear()

                val searchtext = newText!!.toLowerCase(locale)

                if (searchtext.isNotEmpty()) {
                    foodArrayList.forEach {
                        if (it.foodName!!.toLowerCase(locale).contains(searchtext) ||
                            it.foodAddress!!.toLowerCase(locale).contains(searchtext))  {
                            tempArrayList.add(it)
                            resultvalue.text = tempArrayList.size.toString()
                        }
                    }
                    if (tempArrayList.isEmpty()) {
                        resultvalue.text = "0"
                        Toast.makeText(this@RecyclerFood, "No result found", Toast.LENGTH_SHORT).show()
                    }

                    foodRecyclerView.adapter!!.notifyDataSetChanged()


                } else {

                    tempArrayList.clear()
                    tempArrayList.addAll(foodArrayList)
                    resultvalue.text = tempArrayList.size.toString()
                    foodRecyclerView.adapter!!.notifyDataSetChanged()
                }

                // getFoodData(foodType.selectedItem.toString(), newText ?: "")
                return false
            }
        })

        foodType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedFoodType = parent.getItemAtPosition(position).toString()
                //foodType.setSelection(0)
                getFoodData(selectedFoodType)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //  foodType.setSelection(0)
                getFoodData("All")
            }
        }

        // Set the default selected item to "All"
        foodType.setSelection(0)


    }


    private fun getFoodData(selectedFoodType: String) {
        val database = Firebase.database
        val myRef = database.reference.child("Foods")
        var query = myRef.orderByChild("foodCategory").equalTo(foodCategory)

        when(selectedFoodType){
            "All"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                foodArrayList.add(item!!)
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodAdapter(tempArrayList, object : FoodAdapter.OnItemClickListener {
                                override fun onItemClick(item: Foods) {


                                    ////
                                    val intent = Intent(this@RecyclerFood, SingleFood::class.java)
                                    val foodId: String? = item.foodId
                                    intent.putExtra("itemId", foodId?.toString())
                                    startActivity(intent)
                                }
                            })


                            resultvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Veg"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodType == "Veg") {
                                    foodArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodAdapter(tempArrayList, object : FoodAdapter.OnItemClickListener {
                                override fun onItemClick(item: Foods) {


                                    val intent = Intent(this@RecyclerFood, SingleFood::class.java)
                                    val foodId: String? = item.foodId
                                    intent.putExtra("itemId", foodId?.toString())
                                    startActivity(intent)
                                }
                            })
                            resultvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Non-Veg"->{
                //val nonVegQuery = query.orderByChild("foodType").equalTo("Non-Veg")
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodType == "Non-Veg") {
                                    foodArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodAdapter(tempArrayList, object : FoodAdapter.OnItemClickListener {
                                override fun onItemClick(item: Foods) {

                                    val intent = Intent(this@RecyclerFood, SingleFood::class.java)
                                    val foodId: String? = item.foodId
                                    intent.putExtra("itemId", foodId?.toString())
                                    startActivity(intent)
                                }
                            })
                            resultvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
        }
    }

}