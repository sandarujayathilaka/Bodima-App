package com.example.bodima.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bodima.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SingleGroceryActivity : AppCompatActivity() {

    private lateinit var sinGrotitle: TextView
    private lateinit var sinGrosubtitle:TextView
    private lateinit var sindiscount:TextView
    private lateinit var sinGroprice:TextView
    private lateinit var sinGrotpNO:TextView
    private lateinit var sinGrolocate:TextView
    private lateinit var sinGroimg:ImageView
    private lateinit var sinGroabout:TextView
    private lateinit var sinGrophone:Button
    private lateinit var quantity:TextView

    private lateinit var increase:Button
    private lateinit var total:TextView
    private lateinit var decrease:Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_grocery)
        val groceryId = intent.getStringExtra("itemId")

        // initialize UI elements
        sinGrotitle = findViewById(R.id.Grosingletitle)
        sinGrosubtitle = findViewById(R.id.singlesubtitle)
        sindiscount = findViewById(R.id.singlediscount)
        sinGroprice = findViewById(R.id.Grosingleprice)
        sinGrotpNO = findViewById(R.id.singleGrotpNO)
        sinGrolocate = findViewById(R.id.singleGrolocate)
        sinGroabout = findViewById(R.id.singleabout)
        sinGroimg = findViewById(R.id.singlegroceryimage)
        sinGrophone = findViewById(R.id.GrobutCall)
        quantity = findViewById(R.id.groceryQuantityDigit)
        increase = findViewById(R.id.grobtnIncrease)
        decrease = findViewById(R.id.grobtnDecrease)

        total = findViewById(R.id.grototalValue)

        var quantityValue = 1
        quantity.text = quantityValue.toString()




        // retrieve food item data from Firebase Realtime Database
        val database = Firebase.database
        val myRef = database.reference.child("Grocery")
        val query = myRef.orderByChild("id").equalTo(groceryId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {

                        val title = snapshot.child("title").getValue(String::class.java)
                        val price = snapshot.child("price").getValue(String::class.java)?.toLong()
                        val discount = snapshot.child("discount").getValue(String::class.java)
                        val subtitle = snapshot.child("subtitle").getValue(String::class.java)
                        val tpNO = snapshot.child("tpNO").getValue(String::class.java)
                        val locate = snapshot.child("locate").getValue(String::class.java)
                        val about = snapshot.child("about").getValue(String::class.java)
                        val groimg64 = snapshot.child("groceryimg").getValue(String::class.java)


                        val bytes =
                            android.util.Base64.decode(groimg64, android.util.Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        sinGroimg.setImageBitmap(bitmap)

                        // Set the retrieved data to the corresponding UI elements
                        sinGrotitle.text = title
                        sinGroprice.text = price?.toString()
                        sindiscount.text = discount
                        sinGrosubtitle.text = subtitle
                        sinGrotpNO.text = tpNO
                        sinGrolocate.text = locate
                        sinGroabout.text = about


                        // set onclick listener for call button
                        sinGrophone.setOnClickListener {
                            val phoneNumber = tpNO?.toString()?.trim()
                            if (!phoneNumber.isNullOrEmpty()) {
                                val intent =
                                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                                startActivity(intent)
                            }
                        }



                        // set onclick listener for increase button
                        increase.setOnClickListener {
                            quantityValue += 1
                            quantity.text = quantityValue.toString()
                            val newTotalValue = quantityValue * price!!
                            total.text = newTotalValue.toString()
                        }

                        // set onclick listener for decrease button
                        decrease.setOnClickListener {
                            if (quantityValue > 1) {
                                quantityValue -= 1
                                quantity.text = quantityValue.toString()
                                val newTotalValue = quantityValue * price!!
                                total.text = newTotalValue.toString()
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








