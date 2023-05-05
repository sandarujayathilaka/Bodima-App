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

class SingleFurnitureActivity : AppCompatActivity() {
    private lateinit var sintitle: TextView
    private lateinit var sinquantity:TextView
    private lateinit var sinaddress:TextView
    private lateinit var sinprice:TextView
    private lateinit var sinmobile:TextView
    private lateinit var sinfurnitureimg:ImageView
    private lateinit var sindescription:TextView
    private lateinit var sinphone:Button

    private lateinit var sinfplus:Button
    private lateinit var sinfminus:Button
    private lateinit var newqquantity:TextView
    private lateinit var sinTotal:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_furniture)
        val houseId = intent.getStringExtra("itemId")

        // initialize UI elements
        sintitle = findViewById(R.id.f_singletitle)
        sindescription = findViewById(R.id.f_singledescription)
        sinquantity = findViewById(R.id.f_singlequantity)
        sinaddress = findViewById(R.id.f_singleaddress)
        sinmobile = findViewById(R.id.f_mobile)
        sinfurnitureimg = findViewById(R.id.f_singlefurnitureimage)
        sinphone = findViewById(R.id.f_butCall)
//        sinprice = findViewById(R.id.f_singleprice)

        sinfplus = findViewById(R.id.f_plus)
        sinfminus = findViewById(R.id.f_minus)
        newqquantity = findViewById(R.id.new_quantity)
        sinTotal = findViewById(R.id.f_total)


        // retrieve food item data from Firebase Realtime Database
        val database = Firebase.database
        val myRef = database.reference.child("Furniture")
        val query = myRef.orderByChild("id").equalTo(houseId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {

                        val title = snapshot.child("title").getValue(String::class.java)
                        val price = snapshot.child("price").getValue(String::class.java)?.toLong()
                        val quantity = snapshot.child("beds").getValue(String::class.java)?.toLong()
                        val address = snapshot.child("address").getValue(String::class.java)
                        val description = snapshot.child("description").getValue(String::class.java)
                        val mobile = snapshot.child("mobile").getValue(String::class.java)
                        val furnitureimg64 = snapshot.child("furnitureimg").getValue(String::class.java)


                        val bytes =
                            android.util.Base64.decode(furnitureimg64, android.util.Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        sinfurnitureimg.setImageBitmap(bitmap)

                        // Set the retrieved data to the corresponding UI elements
                        sintitle.text = "Title :" +title
//                        sinprice.text = ":" + price?.toString()
//                        sinquantity.text = "Quanitiy :" + quantity?.toString()
                        sindescription.text = "Description : " + description
                        sinaddress.text = "Address : " + address
                        sinmobile.text = "Mobile : " + mobile
                        newqquantity.text = "0"

                        sinfminus.setOnClickListener {
                            var quantity = newqquantity.text.toString().toInt()
                            quantity++
                            newqquantity.text = quantity.toString()
                            var total = price?.times(quantity)
                            sinTotal.text = "Total :" + total.toString()
                        }

                        sinfplus.setOnClickListener {
                            var quantity = newqquantity.text.toString().toInt()
                            if (quantity > 1) {
                                quantity--
                                newqquantity.text = quantity.toString()
                                var total = price?.times(quantity)
                                sinTotal.text = "Total :" + total.toString()
                            }
                        }

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