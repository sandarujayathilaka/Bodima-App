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
import com.squareup.picasso.Picasso

class SingleFood : AppCompatActivity() {
    private lateinit var name:TextView
    private lateinit var price:TextView
    private lateinit var description:TextView
    private lateinit var startTime:TextView
    private lateinit var endTime:TextView
    private lateinit var supplier:TextView
    private lateinit var address:TextView
    private lateinit var increase:Button
    private lateinit var decrease:Button
    private lateinit var total:TextView
    private lateinit var call:Button
    private lateinit var quantity:TextView
    private lateinit var image:ImageView
    private lateinit var phone:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_food)
        val foodId = intent.getStringExtra("itemId")

        // initialize UI elements
        name = findViewById(R.id.singlefoodName)
        price = findViewById(R.id.singlefoodPrice)
        description = findViewById(R.id.singlefoodDescValue)
        startTime = findViewById(R.id.singlestartvalue)
        endTime = findViewById(R.id.singleendvalue)
        supplier = findViewById(R.id.singleSubValue)
        quantity = findViewById(R.id.foodQuantityDigit)
        address = findViewById(R.id.singleAddressValue)
        increase = findViewById(R.id.btnIncrease)
        decrease = findViewById(R.id.btnDecrease)
        total = findViewById(R.id.totalValue)
        call = findViewById(R.id.butCall)
        image= findViewById(R.id.singlefoodimage)
        phone= findViewById(R.id.phone)


        // set initial quantity value
        var quantityValue = 1
        quantity.text = quantityValue.toString()

        // retrieve food item data from Firebase Realtime Database
        val database = Firebase.database
        val myRef = database.reference.child("Foods")
        val query = myRef.orderByChild("foodId").equalTo(foodId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val foodName = snapshot.child("foodName").getValue(String::class.java)
                        val foodPrice = snapshot.child("foodPrice").getValue(String::class.java)?.toLong()
                        val foodDescription = snapshot.child("foodDescription").getValue(String::class.java)
                        val foodStartTime = snapshot.child("foodStartTime").getValue(String::class.java)
                        val foodMeridiumStart = snapshot.child("foodMeridiumStart").getValue(String::class.java)
                        val foodEndTime = snapshot.child("foodEndTime").getValue(String::class.java)
                        val foodMeridiumEnd = snapshot.child("foodMeridiumEnd").getValue(String::class.java)
                        val foodAddress = snapshot.child("foodAddress").getValue(String::class.java)
                        val foodMobile = snapshot.child("foodMobile").getValue(String::class.java)


                        val foodImageBase64 = snapshot.child("foodImage").getValue(String::class.java)


                        val bytes = android.util.Base64.decode(foodImageBase64, android.util.Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        image.setImageBitmap(bitmap)

                        // Set the retrieved data to the corresponding UI elements
                        name.text = foodName
                        price.text = foodPrice?.toString()

                        description.text = foodDescription
                        startTime.text = "$foodStartTime $foodMeridiumStart"
                        endTime.text = "$foodEndTime $foodMeridiumEnd"
                        supplier.text = "Your supplier name" // replace with the actual supplier name
                        address.text = foodAddress
                        //call.text = foodMobile // replace with the actual supplier's contact number
                        phone.text = foodMobile
                        //    image.text = foodImage
                        val foodPriceString = foodPrice?.toString() ?: ""
                        val foodPriceInt = foodPriceString.toIntOrNull() ?: 0

                        val totalValue = quantityValue * foodPriceInt
                        total.text = totalValue.toString()

                        // set onclick listener for increase button
                        increase.setOnClickListener {
                            quantityValue += 1
                            quantity.text = quantityValue.toString()
                            val newTotalValue = quantityValue * foodPriceInt
                            total.text = newTotalValue.toString()
                        }

                        // set onclick listener for decrease button
                        decrease.setOnClickListener {
                            if (quantityValue > 1) {
                                quantityValue -= 1
                                quantity.text = quantityValue.toString()
                                val newTotalValue = quantityValue * foodPriceInt
                                total.text = newTotalValue.toString()
                            }
                        }
                        // set onclick listener for call button
                        call.setOnClickListener {
                            val phoneNumber = foodMobile?.toString()?.trim()
                            if (!phoneNumber.isNullOrEmpty()) {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
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
        })



    }
}

































