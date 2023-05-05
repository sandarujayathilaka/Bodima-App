package com.example.bodima.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R
import com.example.bodima.adapters.FurnitureAdapter
import com.example.bodima.models.Furniture
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RecycleFurnitureActivity : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private lateinit var furnitureRecyclerView: RecyclerView
    private lateinit var furnitureArrayList: ArrayList<Furniture>
    private lateinit var furnitureCategory: String

    //    private lateinit var furnitureType: Spinner
    private lateinit var resultvalue: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_furniture)

        // Get the furniture type from the intent extras
        furnitureCategory = intent.getStringExtra("category") ?: ""
        resultvalue = findViewById<TextView>(R.id.furnitureresultvalue)

//        furnitureType = findViewById<Spinner>(R.id.foodtype)

        furnitureRecyclerView = findViewById(R.id.furniture_item)
        furnitureRecyclerView.layoutManager = LinearLayoutManager(this)

        // val adapter = FoodAdapter(foodArrayList)

// set the adapter to the RecyclerView
        //foodRecyclerView.adapter = adapter
        furnitureRecyclerView.hasFixedSize()
        furnitureArrayList = arrayListOf()


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
        getFurnitureData()
    }

    private fun getFurnitureData() {

        val database = Firebase.database
        val myRef = database.reference.child("Furniture")
        val query = myRef.orderByChild("category").equalTo(furnitureCategory)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                furnitureArrayList.clear() // clear the previous data from the list
                if (snapshot.exists()) {
                    for (furnituresnapshot in snapshot.children) {
                        val item = furnituresnapshot.getValue(Furniture::class.java)
                        furnitureArrayList.add(item!!)
                    }
                    furnitureRecyclerView.adapter = FurnitureAdapter(furnitureArrayList, object : FurnitureAdapter.OnItemClickListener {
                        override fun onItemClick(item: Furniture) {
                            val intent = Intent(this@RecycleFurnitureActivity, SingleFurnitureActivity::class.java)
                            val furnitureId: String? = item.id
                            intent.putExtra("itemId", furnitureId?.toString())
                            startActivity(intent)
                        }
                    })


                    resultvalue.text = furnitureArrayList.size.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        }
        )

    }
}