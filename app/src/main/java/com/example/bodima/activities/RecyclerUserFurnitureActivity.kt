package com.example.bodima.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R
import com.example.bodima.adapters.FurnitureListAdapter
import com.example.bodima.models.Furniture
import android.widget.SearchView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RecyclerUserFurnitureActivity : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private lateinit var furnitureRecyclerView: RecyclerView
    private lateinit var furnitureArrayList: ArrayList<Furniture>
    private lateinit var furnitureCategory: Spinner
    private lateinit var totalvalue: TextView
    private lateinit var search: SearchView

    companion object {
        @JvmField
        val TAG: String = RecyclerUserFurnitureActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_user_furniture)

        totalvalue = findViewById(R.id.furnitureresultvalueUser)
        furnitureCategory = findViewById(R.id.furnituretypeUser)
        furnitureRecyclerView = findViewById(R.id.furniture_itemUser)
        search = findViewById(R.id.f_furnituresearchUser)
        furnitureArrayList = arrayListOf()
        furnitureRecyclerView.layoutManager = LinearLayoutManager(this)

        var selectedFurnitureCate = "All"

        furnitureCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                selectedFurnitureCate = parent.getItemAtPosition(position).toString()
                getfurnitureData(selectedFurnitureCate)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
            furnitureCategory.setSelection(0)


    }

            private fun getfurnitureData(selectedFurnitureCate: String) {
                val database = Firebase.database
                val myRef = database.reference.child("Furniture")
                val firebaseAuth = FirebaseAuth.getInstance()
                val currentUser = firebaseAuth.currentUser
                val userEmail = currentUser?.email ?: ""


                var query = myRef.orderByChild("email").equalTo(userEmail)
                when (selectedFurnitureCate) {
                    "All" -> {
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                furnitureArrayList.clear() // clear the previous data from the list
                                if (snapshot.exists()) {
                                    for (furnituresnapshot in snapshot.children) {
                                        val item = furnituresnapshot.getValue(Furniture::class.java)
                                        furnitureArrayList.add(item!!)
                                    }
                                    furnitureRecyclerView.adapter = FurnitureListAdapter(
                                        furnitureArrayList,
                                        object : FurnitureListAdapter.OnItemClickListener {
                                            override fun onItemClick(item: Furniture) {


                                                //SingleFood//
                                                val intent = Intent(
                                                    this@RecyclerUserFurnitureActivity,
                                                    SingleFurnitureActivity::class.java
                                                )
                                                val furnitureid: String? = item.id
                                                intent.putExtra("itemId", furnitureid?.toString())
                                                startActivity(intent)
                                            }
                                        })


                                    totalvalue.text = furnitureArrayList.size.toString()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle cancel event
                            }
                        })
                    }
                    "Used" -> {
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                furnitureArrayList.clear() // clear the previous data from the list
                                if (snapshot.exists()) {
                                    for (furnituresnapshot in snapshot.children) {
                                        val item = furnituresnapshot.getValue(Furniture::class.java)
                                        if (item?.category == "Used") {
                                            furnitureArrayList.add(item)
                                        }
                                    }
                                    furnitureRecyclerView.adapter = FurnitureListAdapter(
                                        furnitureArrayList,
                                        object : FurnitureListAdapter.OnItemClickListener {
                                            override fun onItemClick(item: Furniture) {


                                                val intent = Intent(
                                                    this@RecyclerUserFurnitureActivity,
                                                    SingleFurnitureActivity::class.java
                                                )
                                                val furnitureId: String? = item.id
                                                intent.putExtra("itemId", furnitureId?.toString())
                                                startActivity(intent)
                                            }
                                        })
                                    totalvalue.text = furnitureArrayList.size.toString()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle cancel event
                            }
                        })
                    }
                    "Brand-new" -> {
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                furnitureArrayList.clear() // clear the previous data from the list
                                if (snapshot.exists()) {
                                    for (furnituresnapshot in snapshot.children) {
                                        val item = furnituresnapshot.getValue(Furniture::class.java)
                                        if (item?.category == "Brand-new") {
                                            furnitureArrayList.add(item)
                                        }
                                    }
                                    furnitureRecyclerView.adapter = FurnitureListAdapter(
                                        furnitureArrayList,
                                        object : FurnitureListAdapter.OnItemClickListener {
                                            override fun onItemClick(item: Furniture) {


                                                val intent = Intent(
                                                    this@RecyclerUserFurnitureActivity,
                                                    SingleFurnitureActivity::class.java
                                                )
                                                val furnitureId: String? = item.id
                                                intent.putExtra("itemId", furnitureId?.toString())
                                                startActivity(intent)
                                            }
                                        })
                                    totalvalue.text = furnitureArrayList.size.toString()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle cancel event
                            }
                        })
                    }

                    "Reconditioned" -> {
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                furnitureArrayList.clear() // clear the previous data from the list
                                if (snapshot.exists()) {
                                    for (furnituresnapshot in snapshot.children) {
                                        val item = furnituresnapshot.getValue(Furniture::class.java)
                                        if (item?.category == "Reconditioned") {
                                            furnitureArrayList.add(item)
                                        }
                                    }
                                    furnitureRecyclerView.adapter = FurnitureListAdapter(
                                        furnitureArrayList,
                                        object : FurnitureListAdapter.OnItemClickListener {
                                            override fun onItemClick(item: Furniture) {


                                                val intent = Intent(
                                                    this@RecyclerUserFurnitureActivity,
                                                    SingleFurnitureActivity::class.java
                                                )
                                                val furnitureId: String? = item.id
                                                intent.putExtra("itemId", furnitureId?.toString())
                                                startActivity(intent)
                                            }
                                        })
                                    totalvalue.text = furnitureArrayList.size.toString()
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