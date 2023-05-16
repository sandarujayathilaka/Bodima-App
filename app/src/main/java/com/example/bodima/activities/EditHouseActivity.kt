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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.ui.tooling.data.EmptyGroup.location
import androidx.core.content.ContextCompat
import com.example.bodima.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream

class EditHouseActivity : AppCompatActivity() {

    private lateinit var editTitle: TextView
    private lateinit var editBeds: TextView
    private lateinit var editAddress: TextView
    private lateinit var editPrice: TextView
    private lateinit var editBaths: TextView
    private lateinit var editMobile: TextView
    private lateinit var editHouseimg: ImageView
    private lateinit var editDescription: TextView
    private lateinit var editPhone: TextView
    private lateinit var uploadImage:ImageButton
    private lateinit var editCategory: Spinner
    private lateinit var updateBtn:Button
    private lateinit var editLocation:TextView

    private val CAMERA_PERMISSION_CODE = 100
    private val CAMERA = 101
    var sImage:String?=""
    var defaultImage:String?=""
    var up:Int?=0
    private var houseId: String? = null

    private  lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_house)

        houseId = intent.getStringExtra("houseId").toString() //get the specific house Id
        val database = Firebase.database
        val myRef = database.reference.child("House")
        val query = myRef.orderByChild("id").equalTo(houseId)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }

        editTitle = findViewById(R.id.titletextEdit)
        editLocation = findViewById(R.id.locationtextEdit)
        editBeds = findViewById(R.id.bedstextEdit)
        editAddress = findViewById(R.id.addresstextEdit)
        editPrice = findViewById(R.id.pricetextEdit)
        editBaths = findViewById(R.id.bathtextEdit)
        editMobile = findViewById(R.id.mobiletextEdit)
        editHouseimg = findViewById(R.id.uploadimgEdit)
        editDescription = findViewById(R.id.descriptiontextEdit)
        editPhone = findViewById(R.id.mobiletextEdit)
        editCategory = findViewById(R.id.housedropdownedit)
        updateBtn = findViewById(R.id.updateBtn)
        uploadImage = findViewById(R.id.uploadbtnEdit)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {

                        val editOldTitle = snapshot.child("title").getValue(String::class.java)
                        val editOldlocation = snapshot.child("location").getValue(String::class.java)
                        val editOldPrice = snapshot.child("price").getValue(String::class.java)?.toLong()
                        val editOldAddress  = snapshot.child("address").getValue(String::class.java)
                        val editOldBeds = snapshot.child("beds").getValue(String::class.java)
                        val editOldBaths = snapshot.child("baths").getValue(String::class.java)
                        val editOldMobile = snapshot.child("mobile").getValue(String::class.java)
                        val editOldimg = snapshot.child("houseimg").getValue(String::class.java)
                        val editOldDescription = snapshot.child("description").getValue(String::class.java)
                        val editOldCategory = snapshot.child("category").getValue(String::class.java)

                        defaultImage = editOldimg ?: "default image"

                        val bytes = Base64.decode(editOldimg, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                        editHouseimg.setImageBitmap(bitmap)
                        editTitle.text = editOldTitle
                        editPrice.text = editOldPrice ?.toString()
                        editDescription.text = editOldDescription
                        editAddress.text = editOldAddress
                        editLocation.text = editOldlocation
                        editBaths.text = editOldBaths
                        editBeds.text = editOldBeds
                        editMobile.text = editOldMobile


                        val catIndex = ( editCategory.adapter as ArrayAdapter<String>).getPosition(editOldCategory)
                        editCategory.setSelection(catIndex)


                    }
                } else {
                    // Handle the case where the house with the given houseid doesn't exist
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the case where the database operation is cancelled
            }
        })
        uploadImage.setOnClickListener {

            insert_Image()


            val imageUri = sImage ?: ""
            editHouseimg.setImageURI(Uri.parse(imageUri))

        }
        updateBtn.setOnClickListener {

            val id: String? = intent.getStringExtra("houseId").toString()
            val houseRef = FirebaseDatabase.getInstance().getReference("House").child(id ?: "")


            // Get the data from the UI elements

            val firebaseAuth = FirebaseAuth.getInstance()
            val currentUser = firebaseAuth.currentUser

            val userEmail = currentUser?.email ?: ""

            val newTitle = editTitle.text.toString().trim()
            val newLocation = editLocation.text.toString().trim()
            val newBeds = editBeds.text.toString().trim()
            val newAddress = editAddress.text.toString().trim()
            val newPrice = editPrice.text.toString().trim()
            val newBaths = editBaths.text.toString().trim()
            val newMobile = editMobile.text.toString().trim()
            val newDescription = editDescription.text.toString().trim()
            val newPhone = editPhone.text.toString().trim()
            val newCategory = editCategory.selectedItem.toString().trim()
            val newemail = userEmail

            val houseimg: String

            if (up == 1) {
                houseimg = sImage ?: ""
            } else {
                houseimg = defaultImage ?: ""
            }


            if (newLocation.isEmpty() || newBeds.isEmpty() || newBaths.isEmpty() || newAddress.isEmpty() ||
                newTitle.isEmpty() || newPrice.isEmpty() || newMobile.isEmpty() || newDescription.isEmpty()

            ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()

            } else if (newCategory == "Select Category") {

                Toast.makeText(this, "Please Select Valid Category", Toast.LENGTH_SHORT).show()

            } else if (newMobile.length != 10) {

                Toast.makeText(
                    this,
                    "Please enter a valid phone number with 10 digits",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                // Create a HashMap to update the data
                val houseUpdates = hashMapOf<String, Any>(


                    "id" to id!!,
                    "title" to newTitle,
                    "price" to newPrice,
                    "description" to newDescription,
                    "mobile" to newMobile,
                    "address" to newAddress,
                    "baths" to newBaths,
                    "beds" to newBeds,
                    "location" to newLocation,
                    "category" to newCategory,
                    "houseimg" to houseimg,
                    "email" to newemail
                )

                // Update the data in the database
                houseRef.updateChildren(houseUpdates)
                    .addOnSuccessListener {
                        // Data updated successfully
                        Toast.makeText(this, "House updated successfully", Toast.LENGTH_SHORT)
                            .show()

// Create an Intent to launch the RecyclerHouse activity
                        val intent = Intent(this, RecyclerUserHouseActivity::class.java)
// Pass the selected category value as an extra

                        startActivity(intent)
                        finish() // Close the current activity


                        // finish() // Close the activity
                    }
                    .addOnFailureListener {
                        // Failed to update the data
                        Toast.makeText(this, "Failed to update house", Toast.LENGTH_SHORT).show()
                    }
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
                    editHouseimg.setImageBitmap(bitmap)
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

                    val image = findViewById<ImageView>(R.id.uploadimgEdit)
                    image?.setImageBitmap(bitmap)
                    // Convert the image to Base64 and save it to variable here
                    up = 1
                    Toast.makeText(this, "Image selected from gallery", Toast.LENGTH_LONG).show()
                } else {
                    val thumbnail = data.extras?.get("data") as Bitmap?
                    if (thumbnail != null) {
                        val stream = ByteArrayOutputStream()
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val bytes = stream.toByteArray()
                        sImage = Base64.encodeToString(bytes, Base64.DEFAULT)
                        val image = findViewById<ImageView>(R.id.uploadimgEdit)
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
