package com.example.bodima.activities

import com.example.bodima.models.Foods
import com.example.bodima.adapters.FoodListAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class RecyclerUserFood : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var foodArrayList: ArrayList<Foods>
    private lateinit var tempArrayList: ArrayList<Foods>
    private lateinit var foodCategory: Spinner
    private lateinit var foodType: Spinner
    private lateinit var totalvalue: TextView
    private lateinit var search: SearchView
    val default="All"


    //    companion object {
//        @JvmField val TAG: String = RecyclerUserFood::class.java.simpleName
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_user_food)
        // foodCategory = intent.getStringExtra("foodCategory") ?: ""


        totalvalue = findViewById<TextView>(R.id.totalvalue)
        foodType = findViewById<Spinner>(R.id.listFoodType)
        foodCategory = findViewById<Spinner>(R.id.listFoodCategory)
        foodRecyclerView = findViewById(R.id.item_food_list)
        foodRecyclerView.layoutManager = LinearLayoutManager(this)

        foodRecyclerView.hasFixedSize()
        foodArrayList = arrayListOf()
        tempArrayList = arrayListOf()
        val locale = Locale.getDefault()

        search = findViewById<SearchView>(R.id.search)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()

                val searchtext = newText!!.toLowerCase(locale)

                if (searchtext.isNotEmpty()) {
                    foodArrayList.forEach {
                        if (it.foodName!!.toLowerCase(locale).contains(searchtext) ||
                            it.foodAddress!!.toLowerCase(locale).contains(searchtext))  {
                            tempArrayList.add(it)
                            totalvalue.text = tempArrayList.size.toString()
                        }
                    }
                    if (tempArrayList.isEmpty()) {
                        totalvalue.text = "0"
                        Toast.makeText(this@RecyclerUserFood, "No result found", Toast.LENGTH_SHORT).show()
                    }

                    foodRecyclerView.adapter!!.notifyDataSetChanged()


                } else {

                    tempArrayList.clear()
                    tempArrayList.addAll(foodArrayList)
                    totalvalue.text = tempArrayList.size.toString()
                    foodRecyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })

        var selectedFoodType = "All"
        var selectedFoodCategory = "All"

        foodType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedFoodType = parent.getItemAtPosition(position).toString()
                getFoodData(selectedFoodType, selectedFoodCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                getFoodData(selectedFoodType, selectedFoodCategory)
            }
        }

        foodCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedFoodCategory = parent.getItemAtPosition(position).toString()
                getFoodData(selectedFoodType, selectedFoodCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                getFoodData(selectedFoodType, selectedFoodCategory)
            }
        }

// Set the default selected item to "All"
        foodType.setSelection(0)
        foodCategory.setSelection(0)

        getFoodData(selectedFoodType, selectedFoodCategory)


    }

    private fun getFoodData(selectedFoodType: String,selectedFoodCategory: String) {
        val database = Firebase.database
        val myRef = database.reference.child("Foods")
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

           val userEmail = currentUser?.email ?: ""

        var query = myRef.orderByChild("foodEmail").equalTo(userEmail)


        when (selectedFoodCategory to selectedFoodType){
            "All" to "All" ->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                foodArrayList.add(item!!)
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodListAdapter(
                                tempArrayList,
                                object : FoodListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Foods) {


                                        //SingleFood//
                                        val intent =
                                            Intent(this@RecyclerUserFood, SingleFood::class.java)
                                        val foodId: String? = item.foodId
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })


                            totalvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Breakfast" to "All"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodCategory == "Breakfast") {
                                    foodArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodListAdapter(
                                tempArrayList,
                                object : FoodListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Foods) {


                                        val intent =
                                            Intent(this@RecyclerUserFood, SingleFood::class.java)
                                        val foodId: String? = item.foodId
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Breakfast" to "Veg"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodCategory == "Breakfast" && item?.foodType == "Veg") {
                                    foodArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodListAdapter(
                                tempArrayList,
                                object : FoodListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Foods) {


                                        val intent =
                                            Intent(this@RecyclerUserFood, SingleFood::class.java)
                                        val foodId: String? = item.foodId
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Breakfast" to "Non-Veg"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodCategory == "Breakfast" && item?.foodType == "Non-Veg") {
                                    foodArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodListAdapter(
                                tempArrayList,
                                object : FoodListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Foods) {


                                        val intent =
                                            Intent(this@RecyclerUserFood, SingleFood::class.java)
                                        val foodId: String? = item.foodId
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Lunch" to "All"->{
                //val nonVegQuery = query.orderByChild("foodType").equalTo("Non-Veg")
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodCategory == "Lunch") {
                                    foodArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodListAdapter(
                                tempArrayList,
                                object : FoodListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Foods) {

                                        val intent =
                                            Intent(this@RecyclerUserFood, SingleFood::class.java)
                                        val foodId: String? = item.foodId
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Lunch" to "Veg"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodCategory == "Lunch" && item?.foodType == "Veg") {
                                    foodArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodListAdapter(
                                tempArrayList,
                                object : FoodListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Foods) {


                                        val intent =
                                            Intent(this@RecyclerUserFood, SingleFood::class.java)
                                        val foodId: String? = item.foodId
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Lunch" to "Non-Veg"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodCategory == "Lunch" && item?.foodType == "Non-Veg") {
                                    foodArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodListAdapter(
                                tempArrayList,
                                object : FoodListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Foods) {


                                        val intent =
                                            Intent(this@RecyclerUserFood, SingleFood::class.java)
                                        val foodId: String? = item.foodId
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Dinner" to "All"->{
                //val nonVegQuery = query.orderByChild("foodType").equalTo("Non-Veg")
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodCategory == "Dinner") {
                                    foodArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodListAdapter(
                                tempArrayList,
                                object : FoodListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Foods) {

                                        val intent =
                                            Intent(this@RecyclerUserFood, SingleFood::class.java)
                                        val foodId: String? = item.foodId
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Dinner" to "Veg"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodCategory == "Dinner" && item?.foodType == "Veg") {
                                    foodArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodListAdapter(
                                tempArrayList,
                                object : FoodListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Foods) {


                                        val intent =
                                            Intent(this@RecyclerUserFood, SingleFood::class.java)
                                        val foodId: String? = item.foodId
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Dinner" to "Non-Veg"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodCategory == "Dinner" && item?.foodType == "Non-Veg") {
                                    foodArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodListAdapter(
                                tempArrayList,
                                object : FoodListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Foods) {


                                        val intent =
                                            Intent(this@RecyclerUserFood, SingleFood::class.java)
                                        val foodId: String? = item.foodId
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Snacks and juices" to "All"->{
                //val nonVegQuery = query.orderByChild("foodType").equalTo("Non-Veg")
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodCategory == "Snacks and juices") {
                                    foodArrayList.add(item)
                                }
                            }
                            foodRecyclerView.adapter = FoodListAdapter(
                                foodArrayList,
                                object : FoodListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Foods) {

                                        val intent =
                                            Intent(this@RecyclerUserFood, SingleFood::class.java)
                                        val foodId: String? = item.foodId
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Snacks and juices" to "Veg"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodCategory == "Snacks and juices" && item?.foodType == "Veg") {
                                    foodArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodListAdapter(
                                tempArrayList,
                                object : FoodListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Foods) {


                                        val intent =
                                            Intent(this@RecyclerUserFood, SingleFood::class.java)
                                        val foodId: String? = item.foodId
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = foodArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Snacks and juices" to "Non-Veg"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        foodArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Foods::class.java)
                                if (item?.foodCategory == "Snacks and juices" && item?.foodType == "Non-Veg") {
                                    foodArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(foodArrayList)
                            foodRecyclerView.adapter = FoodListAdapter(
                                tempArrayList,
                                object : FoodListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Foods) {


                                        val intent =
                                            Intent(this@RecyclerUserFood, SingleFood::class.java)
                                        val foodId: String? = item.foodId
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = foodArrayList.size.toString()
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