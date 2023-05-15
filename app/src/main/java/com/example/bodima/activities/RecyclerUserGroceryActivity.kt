package com.example.bodima.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R
import com.example.bodima.adapters.FoodListAdapter
import com.example.bodima.adapters.GroceryListAdapter
import com.example.bodima.models.Foods
import com.example.bodima.models.Grocery
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class RecyclerUserGroceryActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var groceryRecyclerView: RecyclerView
    private lateinit var groceryArrayList: ArrayList<Grocery>
    private lateinit var groceryCategory: Spinner
    private lateinit var totalvalue: TextView
    private lateinit var search: SearchView
    private lateinit var tempArrayList: ArrayList<Grocery>
    private lateinit var resultvalue: TextView




    companion object {
        @JvmField
        val TAG: String = RecyclerUserGroceryActivity::class.java.simpleName
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_user_grocery)

        totalvalue = findViewById(R.id.groceryresultvalueUser)
        groceryCategory = findViewById(R.id.grocerytypeUser)
        groceryRecyclerView = findViewById(R.id.grocery_itemUser)
        search = findViewById(R.id.grocerysearchUser)
        groceryArrayList = arrayListOf()
        groceryRecyclerView.layoutManager = LinearLayoutManager(this)

        groceryRecyclerView.hasFixedSize()

        tempArrayList = arrayListOf()
        val locale = Locale.getDefault()
        var selectedHouseCate = "All"

        groceryCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedHouseCate = parent.getItemAtPosition(position).toString()
                gethouseData(selectedHouseCate)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        groceryCategory.setSelection(0)


        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()

                val searchtext = newText!!.toLowerCase(locale)

                if (searchtext.isNotEmpty()) {
                    groceryArrayList.forEach {
                        if (it.title!!.toLowerCase(locale).contains(searchtext) ||
                            it.locate!!.toLowerCase(locale).contains(searchtext))  {
                            tempArrayList.add(it)
                            totalvalue.text = tempArrayList.size.toString()
                        }
                    }
                    if (tempArrayList.isEmpty()) {
                        totalvalue.text = "0"
                        Toast.makeText(this@RecyclerUserGroceryActivity, "No result found", Toast.LENGTH_SHORT).show()
                    }

                    groceryRecyclerView.adapter!!.notifyDataSetChanged()


                } else {

                    tempArrayList.clear()
                    tempArrayList.addAll(groceryArrayList)
                    totalvalue.text = tempArrayList.size.toString()
                    groceryRecyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })




    }



    private fun gethouseData(selectedHouseCate: String) {
        val database = Firebase.database
        val myRef = database.reference.child("Grocery")
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userEmail = currentUser?.email ?: ""


        var query = myRef.orderByChild("email").equalTo(userEmail)
        when (selectedHouseCate) {
            "All" -> {
                query.addListenerForSingleValueEvent(object : ValueEventListener {
//
                    override fun onDataChange(snapshot: DataSnapshot) {
                        groceryArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (foodsnapshot in snapshot.children) {
                                val item = foodsnapshot.getValue(Grocery::class.java)
                                groceryArrayList.add(item!!)
                            }
                            tempArrayList.addAll(groceryArrayList)
                            groceryRecyclerView.adapter = GroceryListAdapter(
                                tempArrayList,
                                object : GroceryListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Grocery) {

                                        val intent =
                                        Intent(this@RecyclerUserGroceryActivity, SingleGroceryActivity::class.java)
                                        val foodId: String? = item.id
                                        intent.putExtra("itemId", foodId?.toString())
                                        startActivity(intent)
                                    }
                                })


                            totalvalue.text = groceryArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Beverages" -> {
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        groceryArrayList.clear() // clear the previous data from the list
                        if (snapshot.exists()) {
                            for (grocerysnapshot in snapshot.children) {
                                val item = grocerysnapshot.getValue(Grocery::class.java)
                                if (item?.category == "Beverages") {
                                    groceryArrayList.add(item)
                                }
                            }
                            groceryRecyclerView.adapter = GroceryListAdapter(
                                groceryArrayList,
                                object : GroceryListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Grocery) {


                                        val intent = Intent(
                                            this@RecyclerUserGroceryActivity,
                                            SingleGroceryActivity::class.java
                                        )
                                        val houseId: String? = item.id
                                        intent.putExtra("itemId", houseId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = groceryArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Dairy" -> {
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        groceryArrayList.clear() // clear the previous data from the list
                        if (snapshot.exists()) {
                            for (grocerysnapshot in snapshot.children) {
                                val item = grocerysnapshot.getValue(Grocery::class.java)
                                if (item?.category == "Dairy") {
                                    groceryArrayList.add(item)
                                }
                            }
                            groceryRecyclerView.adapter = GroceryListAdapter(
                                groceryArrayList,
                                object : GroceryListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Grocery) {


                                        val intent = Intent(
                                            this@RecyclerUserGroceryActivity,
                                            SingleGroceryActivity::class.java
                                        )
                                        val houseId: String? = item.id
                                        intent.putExtra("itemId", houseId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = groceryArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }

            "Bread" -> {
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        groceryArrayList.clear() // clear the previous data from the list
                        if (snapshot.exists()) {
                            for (grocerysnapshot in snapshot.children) {
                                val item = grocerysnapshot.getValue(Grocery::class.java)
                                if (item?.category == "Bread") {
                                    groceryArrayList.add(item)
                                }
                            }
                            groceryRecyclerView.adapter = GroceryListAdapter(
                                groceryArrayList,
                                object : GroceryListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: Grocery) {


                                        val intent = Intent(
                                            this@RecyclerUserGroceryActivity,
                                            SingleGroceryActivity::class.java
                                        )
                                        val houseId: String? = item.id
                                        intent.putExtra("itemId", houseId?.toString())
                                        startActivity(intent)
                                    }
                                })
                            totalvalue.text = groceryArrayList.size.toString()
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
