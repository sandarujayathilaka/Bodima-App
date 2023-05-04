package com.example.bodima.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.bodima.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream

class EditFoodActivity : AppCompatActivity() {
    private lateinit var name:TextView
    private lateinit var price:TextView
    private lateinit var description:TextView
    private lateinit var startTime:Spinner
    private lateinit var endTime:Spinner
    private lateinit var startMeridium:Spinner
    private lateinit var address:TextView
    private lateinit var endMeridum:Spinner
    private lateinit var phone:TextView

    private lateinit var category:Spinner
    private lateinit var type:Spinner
    private lateinit var image:ImageView
    private lateinit var uploadImage:Button
    private lateinit var update:Button

    private val CAMERA_PERMISSION_CODE = 100
    private val CAMERA = 101
    var sImage:String?=""
    var defaultImage:String?=""
    var up:Int?=0
    private var foodId: String? = null

    private  lateinit var foodDbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_food)

        foodId = intent.getStringExtra("foodId").toString()
        val database = Firebase.database
        val myRef = database.reference.child("Foods")
        val query = myRef.orderByChild("foodId").equalTo(foodId)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }

        name = findViewById(R.id.addFood)
        price = findViewById(R.id.addPrice)
        description = findViewById(R.id.EditDesc)
        address = findViewById(R.id.addAddress)
        phone = findViewById(R.id.addMobile)
        startTime = findViewById(R.id.startTime)
        startMeridium = findViewById(R.id.startMeridium)
        endTime = findViewById(R.id.endTime)
        endMeridum = findViewById(R.id.endMeridium)
        category = findViewById(R.id.addCategory)
        type = findViewById(R.id.addType)
        image = findViewById(R.id.imageFood)
        address = findViewById(R.id.addAddress)
        update = findViewById(R.id.postbtn)
        uploadImage = findViewById(R.id.imageButton)

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
                        val foodCategory = snapshot.child("foodCategory").getValue(String::class.java)
                        val foodType = snapshot.child("foodType").getValue(String::class.java)
                        val foodImageBase64 = snapshot.child("foodImage").getValue(String::class.java)
                        defaultImage = foodImageBase64 ?: "default image"

                        val bytes = Base64.decode(foodImageBase64, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        image.setImageBitmap(bitmap)


                        // Set the retrieved data to the corresponding UI elements
                        name.text = foodName

                        price.text = foodPrice?.toString()

                        description.text = foodDescription

                        address.text = foodAddress
                        phone.text = foodMobile

                        val startIndex = (startTime.adapter as ArrayAdapter<String>).getPosition(foodStartTime)
                        startTime.setSelection(startIndex)

                        val startMer = (startMeridium.adapter as ArrayAdapter<String>).getPosition(foodMeridiumStart)
                        startMeridium.setSelection(startMer)

                        val endIndex = (endTime.adapter as ArrayAdapter<String>).getPosition(foodEndTime)
                        endTime.setSelection(endIndex)

                        val endMer = (endMeridum.adapter as ArrayAdapter<String>).getPosition(foodMeridiumEnd)
                        endMeridum.setSelection(endMer)

                        val catIndex = (category.adapter as ArrayAdapter<String>).getPosition(foodCategory)
                        category.setSelection(catIndex)

                        val typeIndex = (type.adapter as ArrayAdapter<String>).getPosition(foodType)
                        type.setSelection(typeIndex)
//                        startTime.text = Editable.Factory.getInstance().newEditable(foodStartTime)



                    }
                } else {
                    // Handle the case where the food item with the given foodId doesn't exist
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the case where the database operation is cancelled
            }
        })
        uploadImage.setOnClickListener {
            insert_Image()
            update.isEnabled = true

            //  update.setBackgroundColor(resources.getColor(R.color.red))

            // Set the sImage value as foodImage when the user selects the uploadImage button
            val imageUri = sImage ?: ""
            image.setImageURI(Uri.parse(imageUri))

        }
        update.setOnClickListener {
            val id: String? = intent.getStringExtra("foodId").toString()
            val foodRef = FirebaseDatabase.getInstance().getReference("Foods").child(id ?: "")


            // Get the data from the UI elements
            val name = name.text.toString().trim()
            val price = price.text.toString().trim()
            val description = description.text.toString().trim()
            val startTime = startTime.selectedItem.toString().trim()
            val startMeridium = startMeridium.selectedItem.toString().trim()
            val endTime = endTime.selectedItem.toString().trim()
            val endMeridium = endMeridum.selectedItem.toString().trim()
            val category = category.selectedItem.toString().trim()
            val type = type.selectedItem.toString().trim()
            val address = address.text.toString().trim()
            val phone = phone.text.toString().trim()

            val firebaseAuth = FirebaseAuth.getInstance()
            val currentUser = firebaseAuth.currentUser

            val userEmail = currentUser?.email ?: ""

            val foodImage:String
            if(up == 1){
                foodImage = sImage?:""
            }else{
                foodImage = defaultImage ?:""
            }


            // Create a HashMap to update the data
            val foodUpdates = hashMapOf<String, Any>(

                "foodId" to id!!,
                "foodName" to name,
                "foodPrice" to price,
                "foodDescription" to description,
                "foodMobile" to phone,
                "foodStartTime" to startTime,
                "foodMeridiumStart" to startMeridium,
                "foodEndTime" to endTime,
                "foodMeridiumEnd" to endMeridium,
                "foodAddress" to address,
                "foodCategory" to category,
                "foodType" to type,
                "foodImage" to foodImage,
                "foodEmail" to userEmail
            )

            // Update the data in the database
            foodRef.updateChildren(foodUpdates)
                .addOnSuccessListener {
                    // Data updated successfully
                    Toast.makeText(this, "Food updated successfully", Toast.LENGTH_SHORT).show()

// Create an Intent to launch the RecyclerFood activity
                    val intent = Intent(this, RecyclerUserFood::class.java)
// Pass the selected category value as an extra

                    startActivity(intent)
                    finish() // Close the current activity


                    // finish() // Close the activity
                }
                .addOnFailureListener {
                    // Failed to update the data
                    Toast.makeText(this, "Failed to update food", Toast.LENGTH_SHORT).show()
                }
        }


    }

    private fun insert_Image() {
        val options = arrayOf<CharSequence>("Select from Gallery", "Take a Photo")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Image")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Select from Gallery" -> {
                    val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
                    galleryIntent.type = "image/*"
                    ActivityResultLauncher.launch(galleryIntent)

                }
                options[item] == "Take a Photo" -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        ActivityResultLauncher.launch(cameraIntent)


                    } else {
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
                    }
                }
                else -> {
                    up = 0
                    val bytes = Base64.decode(defaultImage, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    image.setImageBitmap(bitmap)
                }
            }
        }
        builder.show()
    }

    private val ActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                if (data.data != null) {
                    val imageUri = data.data!!
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val bytes = stream.toByteArray()
                    sImage = Base64.encodeToString(bytes, Base64.DEFAULT)
                    // Display the selected image in an ImageView
//                val image = view?.findViewById<ImageView>(R.id.imageFood)
//                view.image.setImageBitmap(bitmap)
                    val image = findViewById<ImageView>(R.id.imageFood)
                    image?.setImageBitmap(bitmap)
                    // Convert the image to Base64 and save it to your variable here
                    up = 1
                    Toast.makeText(this, "Image selected from gallery", Toast.LENGTH_LONG).show()
                } else {
                    val thumbnail = data.extras?.get("data") as Bitmap?
                    if (thumbnail != null) {
                        val stream = ByteArrayOutputStream()
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val bytes = stream.toByteArray()
                        sImage = Base64.encodeToString(bytes, Base64.DEFAULT)
                        val image = findViewById<ImageView>(R.id.imageFood)
                        image?.setImageBitmap(thumbnail)
                        up = 1
                        Toast.makeText(this, "Image taken from camera", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Image selection canceled", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                ActivityResultLauncher.launch(cameraIntent)
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}