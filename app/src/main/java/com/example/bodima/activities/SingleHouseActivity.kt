package com.example.bodima.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.bodima.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SingleHouseActivity : AppCompatActivity() {

    private lateinit var sintitle: TextView
    private lateinit var sinbeds:TextView
    private lateinit var sinaddress:TextView
    private lateinit var sinprice:TextView
    private lateinit var sinbaths:TextView
    private lateinit var sinmobile:TextView
    private lateinit var sinhouseimg:ImageView
    private lateinit var sindescription:TextView
    private lateinit var sinphone:Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_house)
        val houseId = intent.getStringExtra("itemId")

        // initialize UI elements
        sintitle = findViewById(R.id.singletitle)
        sindescription = findViewById(R.id.singlebedrooms)
        sinbeds = findViewById(R.id.singlebedrooms)
        sinaddress = findViewById(R.id.singleaddress)
        sinbaths = findViewById(R.id.singlebathroom)
        sinmobile = findViewById(R.id.mobile)
        sinhouseimg = findViewById(R.id.singlehouseimage)
        sinphone = findViewById(R.id.butCall)
        sinprice = findViewById(R.id.singleprice)


        // retrieve food item data from Firebase Realtime Database
        val database = Firebase.database
        val myRef = database.reference.child("House")
        val query = myRef.orderByChild("id").equalTo(houseId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {

                        val title = snapshot.child("title").getValue(String::class.java)
                        val price = snapshot.child("price").getValue(String::class.java)?.toLong()
                        val beds = snapshot.child("beds").getValue(String::class.java)
                        val address = snapshot.child("address").getValue(String::class.java)
                        val baths = snapshot.child("baths").getValue(String::class.java)
                        val description = snapshot.child("description").getValue(String::class.java)
                        val mobile = snapshot.child("mobile").getValue(String::class.java)
                        val houseimg64 = snapshot.child("houseimg").getValue(String::class.java)


                        val bytes =
                            android.util.Base64.decode(houseimg64, android.util.Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        sinhouseimg.setImageBitmap(bitmap)

                        // Set the retrieved data to the corresponding UI elements
                        sintitle.text = title
                        sinprice.text = price?.toString()
                        sinbeds.text = beds
                        sinbaths.text = baths
                        sindescription.text = description
                        sinaddress.text = address
                        sinmobile.text = mobile


                        // set onclick listener for call button
                        sinphone.setOnClickListener {
                            val phoneNumber = mobile?.toString()?.trim()
                            if (!phoneNumber.isNullOrEmpty()) {
                                val intent =
                                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                                startActivity(intent)
                            }
                        }

                    }
                } else {
                    // Handle the case where the food item with the given foodId doesn't exist
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the case where the database operation is cancelled
            }

        }
        )
    }
        }








