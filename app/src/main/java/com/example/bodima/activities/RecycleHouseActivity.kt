package com.example.bodima.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R
import com.example.bodima.adapters.HouseAdapter
import com.example.bodima.models.House
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RecycleHouseActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var houseRecyclerView: RecyclerView
    private lateinit var houseArrayList: ArrayList<House>
    private lateinit var houseCategory: String
    private lateinit var resultvalue: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_house)

        // Get the food type from the intent extras
        houseCategory = intent.getStringExtra("category") ?: ""
        resultvalue = findViewById<TextView>(R.id.homeresultvalue)

//        houseType = findViewById<Spinner>(R.id.foodtype)

        houseRecyclerView = findViewById(R.id.house_item)
        houseRecyclerView.layoutManager = LinearLayoutManager(this)

        // val adapter = FoodAdapter(foodArrayList)

// set the adapter to the RecyclerView
        //foodRecyclerView.adapter = adapter
        houseRecyclerView.hasFixedSize()
        houseArrayList = arrayListOf()


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
        getHouseData()
    }

    private fun getHouseData() {

        val database = Firebase.database
        val myRef = database.reference.child("House")
        val query = myRef.orderByChild("category").equalTo(houseCategory)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                houseArrayList.clear() // clear the previous data from the list
                if (snapshot.exists()) {
                    for (housesnapshot in snapshot.children) {
                        val item = housesnapshot.getValue(House::class.java)
                        houseArrayList.add(item!!)
                    }
                    houseRecyclerView.adapter = HouseAdapter(houseArrayList, object : HouseAdapter.OnItemClickListener {
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
