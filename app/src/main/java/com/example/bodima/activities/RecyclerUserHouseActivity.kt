package com.example.bodima.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R
import com.example.bodima.adapters.HouseListAdapter
import com.example.bodima.models.House
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class RecyclerUserHouseActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var houseRecyclerView: RecyclerView
    private lateinit var houseArrayList: ArrayList<House>
    private lateinit var houseCategory: Spinner
    private lateinit var totalvalue: TextView
    private lateinit var search: SearchView
    private lateinit var tempArrayList: ArrayList<House>

    companion object {
        @JvmField
        val TAG: String = RecyclerUserHouseActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_user_house)

        totalvalue = findViewById(R.id.homeresultvalueUser)
        houseCategory = findViewById(R.id.hometypeUser)
        houseRecyclerView = findViewById(R.id.house_itemUser)
        search = findViewById(R.id.housesearchUser)
        houseArrayList = arrayListOf()
        houseRecyclerView.layoutManager = LinearLayoutManager(this)

        tempArrayList = arrayListOf()
        val locale = Locale.getDefault()

        //Search the house item
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Refresh the list with the updated search query
                tempArrayList.clear()

                val searchtext = newText!!.toLowerCase(locale)

                if (searchtext.isNotEmpty()) {
                    //search house item based on location and title
                    houseArrayList.forEach {
                        if (it.location!!.toLowerCase(locale).contains(searchtext) ||
                            it.title!!.toLowerCase(locale).contains(searchtext))  {
                            tempArrayList.add(it)
                            totalvalue.text = tempArrayList.size.toString()
                        }
                    }
                    if (tempArrayList.isEmpty()) {
                        totalvalue.text = "0"
                        Toast.makeText(this@RecyclerUserHouseActivity, "No result found", Toast.LENGTH_SHORT).show()
                    }

                    houseRecyclerView.adapter!!.notifyDataSetChanged()


                } else {

                    tempArrayList.clear()
                    tempArrayList.addAll(houseArrayList)
                    totalvalue.text = tempArrayList.size.toString()
                    houseRecyclerView.adapter!!.notifyDataSetChanged()
                }


                return false
            }
        })



        var selectedHouseCate = "All"

        houseCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
            houseCategory.setSelection(0)


    }

            private fun gethouseData(selectedHouseCate: String) {
                val database = Firebase.database
                val myRef = database.reference.child("House")
                val firebaseAuth = FirebaseAuth.getInstance()
                val currentUser = firebaseAuth.currentUser
                val userEmail = currentUser?.email ?: ""


                var query = myRef.orderByChild("email").equalTo(userEmail) //check current loged users email

                //Filter Houses based on category
                when (selectedHouseCate) {
                    "All" -> {
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                houseArrayList.clear() // clear the previous data from the list
                                tempArrayList.clear()
                                if (snapshot.exists()) {
                                    for (housesnapshot in snapshot.children) {
                                        val item = housesnapshot.getValue(House::class.java)
                                        houseArrayList.add(item!!)
                                    }
                                    tempArrayList.addAll(houseArrayList)
                                    houseRecyclerView.adapter = HouseListAdapter(
                                        tempArrayList,
                                        object : HouseListAdapter.OnItemClickListener {
                                            override fun onItemClick(item: House) {


                                                //Singlehouse
                                                val intent = Intent(
                                                    this@RecyclerUserHouseActivity,
                                                    SingleHouseActivity::class.java
                                                )
                                                val houseid: String? = item.id
                                                intent.putExtra("itemId", houseid?.toString())
                                                startActivity(intent)
                                            }
                                        })


                                    totalvalue.text = houseArrayList.size.toString()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle cancel event
                            }
                        })
                    }
                    "Annex" -> {
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                houseArrayList.clear() // clear the previous data from the list
                                tempArrayList.clear()
                                if (snapshot.exists()) {
                                    for (housesnapshot in snapshot.children) {
                                        val item = housesnapshot.getValue(House::class.java)
                                        if (item?.category == "Annex") {
                                            houseArrayList.add(item)
                                        }
                                    }
                                    tempArrayList.addAll(houseArrayList)
                                    houseRecyclerView.adapter = HouseListAdapter(
                                        tempArrayList,
                                        object : HouseListAdapter.OnItemClickListener {
                                            override fun onItemClick(item: House) {


                                                val intent = Intent(
                                                    this@RecyclerUserHouseActivity,
                                                    SingleHouseActivity::class.java
                                                )
                                                val houseId: String? = item.id
                                                intent.putExtra("itemId", houseId?.toString())
                                                startActivity(intent)
                                            }
                                        })
                                    totalvalue.text = houseArrayList.size.toString()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle cancel event
                            }
                        })
                    }
                    "Single-Room" -> {
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                houseArrayList.clear() // clear the previous data from the list
                                tempArrayList.clear()
                                if (snapshot.exists()) {
                                    for (housesnapshot in snapshot.children) {
                                        val item = housesnapshot.getValue(House::class.java)
                                        if (item?.category == "Single-Room") {
                                            houseArrayList.add(item)
                                        }
                                    }
                                    tempArrayList.addAll(houseArrayList)
                                    houseRecyclerView.adapter = HouseListAdapter(
                                        tempArrayList,
                                        object : HouseListAdapter.OnItemClickListener {
                                            override fun onItemClick(item: House) {


                                                val intent = Intent(
                                                    this@RecyclerUserHouseActivity,
                                                    SingleHouseActivity::class.java
                                                )
                                                val houseId: String? = item.id
                                                intent.putExtra("itemId", houseId?.toString())
                                                startActivity(intent)
                                            }
                                        })
                                    totalvalue.text = houseArrayList.size.toString()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle cancel event
                            }
                        })
                    }

                    "Home" -> {
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                houseArrayList.clear() // clear the previous data from the list
                                tempArrayList.clear()
                                if (snapshot.exists()) {
                                    for (housesnapshot in snapshot.children) {
                                        val item = housesnapshot.getValue(House::class.java)
                                        if (item?.category == "Home") {
                                            houseArrayList.add(item)
                                        }
                                    }
                                    tempArrayList.addAll(houseArrayList)
                                    houseRecyclerView.adapter = HouseListAdapter(
                                        tempArrayList,
                                        object : HouseListAdapter.OnItemClickListener {
                                            override fun onItemClick(item: House) {


                                                val intent = Intent(
                                                    this@RecyclerUserHouseActivity,
                                                    SingleHouseActivity::class.java
                                                )
                                                val houseId: String? = item.id
                                                intent.putExtra("itemId", houseId?.toString())
                                                startActivity(intent)
                                            }
                                        })
                                    totalvalue.text = houseArrayList.size.toString()
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
