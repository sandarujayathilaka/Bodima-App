package com.example.bodima.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R
import com.example.bodima.adapters.HouseAdapter
import com.example.bodima.models.Foods
import com.example.bodima.models.House
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class RecycleHouseActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var houseRecyclerView: RecyclerView
    private lateinit var houseArrayList: ArrayList<House>
    private lateinit var houseCategory: String
    private lateinit var resultvalue: TextView
    private lateinit var search: SearchView
    private lateinit var tempArrayList: ArrayList<House>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_house)

        // Get the house type from the intent extras
        houseCategory = intent.getStringExtra("category") ?: ""
        resultvalue = findViewById<TextView>(R.id.homeresultvalue)

        houseRecyclerView = findViewById(R.id.house_item)
        houseRecyclerView.layoutManager = LinearLayoutManager(this)


        houseRecyclerView.hasFixedSize()
        houseArrayList = arrayListOf()

        tempArrayList = arrayListOf()
        val locale = Locale.getDefault()

        //search the homes
        search = findViewById<SearchView>(R.id.housesearch)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Refresh the list with the updated search query
                tempArrayList.clear()

                val searchtext = newText!!.toLowerCase(locale)

                if (searchtext.isNotEmpty()) {
                    houseArrayList.forEach {
                        if (it.location!!.toLowerCase(locale).contains(searchtext) || //search by location and title
                            it.title!!.toLowerCase(locale).contains(searchtext))  {
                            tempArrayList.add(it)
                            resultvalue.text = tempArrayList.size.toString()
                        }
                    }
                    if (tempArrayList.isEmpty()) {
                        resultvalue.text = "0"
                        Toast.makeText(this@RecycleHouseActivity, "No result found", Toast.LENGTH_SHORT).show()
                    }

                    houseRecyclerView.adapter!!.notifyDataSetChanged()


                } else {

                    tempArrayList.clear()
                    tempArrayList.addAll(houseArrayList)
                    resultvalue.text = tempArrayList.size.toString()
                    houseRecyclerView.adapter!!.notifyDataSetChanged()
                }


                return false
            }
        })


        getHouseData()
    }

    private fun getHouseData() {

        val database = Firebase.database
        val myRef = database.reference.child("House")
        val query = myRef.orderByChild("category").equalTo(houseCategory)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (housesnapshot in snapshot.children) {
                        val item = housesnapshot.getValue(House::class.java)
                        houseArrayList.add(item!!)
                    }
                    tempArrayList.addAll(houseArrayList)
                    houseRecyclerView.adapter = HouseAdapter(tempArrayList, object : HouseAdapter.OnItemClickListener {
                        override fun onItemClick(item:House) {
                            val intent = Intent(this@RecycleHouseActivity, SingleHouseActivity::class.java)
                            val houseId: String? = item.id
                            intent.putExtra("itemId", houseId?.toString())
                            startActivity(intent)
                        }
                    })


                    resultvalue.text = houseArrayList.size.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


    }
        )

}

}
