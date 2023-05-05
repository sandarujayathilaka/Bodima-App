package com.example.bodima.activities

import android.Manifest
import android.annotation.SuppressLint
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

class EditGroceryActivity : AppCompatActivity() {

    private lateinit var editTitle: TextView
    private lateinit var editSubtitle: TextView
    private lateinit var editDiscount: TextView
    private lateinit var editPrice: TextView
    private lateinit var editTPNo: TextView
    private lateinit var editLocation: TextView
    private lateinit var editGroceryeimg: ImageView
    private lateinit var editAbout: TextView
    private lateinit var uploadImage:ImageButton
    private lateinit var editCategory: Spinner
    private lateinit var updateBtn:Button


    private val CAMERA_PERMISSION_CODE = 100
    private val CAMERA = 101
    var sImage:String?=""
    var defaultImage:String?=""
    var up:Int?=0
    private var groceryId: String? = null

    private  lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_grocery)

        groceryId = intent.getStringExtra("groceryId").toString()
        val database = Firebase.database
        val myRef = database.reference.child("Grocery")
        val query = myRef.orderByChild("id").equalTo(groceryId)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }

        editTitle = findViewById(R.id.EditTitle)
        editLocation = findViewById(R.id.EditLocation)
        editSubtitle = findViewById(R.id.EditSubtitile)
        editDiscount = findViewById(R.id.EditDiscount)
        editPrice = findViewById(R.id.EditPrice)
        editTPNo = findViewById(R.id.EditContact)
        editGroceryeimg = findViewById(R.id.GrouploadimgEdit)
        editAbout = findViewById(R.id.EditAbout)

        editCategory = findViewById(R.id.grocerydropdownedit)
        updateBtn = findViewById(R.id.GroupdateBtn)
        uploadImage = findViewById(R.id.GrouploadbtnEdit)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {

                        val editOldTitle = snapshot.child("title").getValue(String::class.java)
                        val editOldlocation = snapshot.child("locate").getValue(String::class.java)
                        val editOldPrice = snapshot.child("price").getValue(String::class.java)?.toLong()
                        val editOldDiscount  = snapshot.child("discount").getValue(String::class.java)
                        val editOldSubtitle= snapshot.child("subtitle").getValue(String::class.java)
                        val editOldcontact = snapshot.child("tpNO").getValue(String::class.java)
                        val editOldimg = snapshot.child("groceryimg").getValue(String::class.java)
                        val editOldAbout = snapshot.child("about").getValue(String::class.java)
                        val editOldCategory = snapshot.child("category").getValue(String::class.java)

                        defaultImage = editOldimg ?: "default image"

                        val bytes = Base64.decode(editOldimg, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                        editGroceryeimg.setImageBitmap(bitmap)
                        editTitle.text = editOldTitle
                        editSubtitle.text = editOldSubtitle
                        editPrice.text = editOldPrice ?.toString()
                        editDiscount.text = editOldDiscount
                        editAbout.text = editOldAbout
                        editLocation.text = editOldlocation
                        editTPNo.text = editOldcontact



                        val catIndex = ( editCategory.adapter as ArrayAdapter<String>).getPosition(editOldCategory)
                        editCategory.setSelection(catIndex)


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


            val imageUri = sImage ?: ""
            editGroceryeimg.setImageURI(Uri.parse(imageUri))

        }
        updateBtn.setOnClickListener {
            val id: String? = intent.getStringExtra("groceryId").toString()
            val houseRef = FirebaseDatabase.getInstance().getReference("Grocery").child(id ?: "")


            // Get the data from the UI elements

            val firebaseAuth = FirebaseAuth.getInstance()
            val currentUser = firebaseAuth.currentUser

            val userEmail = currentUser?.email ?: ""

            val newTitle = editTitle.text.toString().trim()
            val newSubTitle = editSubtitle.text.toString().trim()
            val newPrice = editPrice.text.toString().trim()
            val newDiscount = editDiscount.text.toString().trim()
            val newContact = editTPNo.text.toString().trim()
            val newLocation = editLocation.text.toString().trim()
            val newAbout = this.editAbout.text.toString().trim()

            val newCategory = editCategory.selectedItem.toString().trim()
            val newemail = userEmail

            val groceryimg:String

            if(up == 1){
                groceryimg = sImage?:""
            }else{
                groceryimg = defaultImage ?:""
            }

            // Create a HashMap to update the data
            val groceryeUpdates = hashMapOf<String, Any>(



                "id" to id!!,
                "title" to newTitle,
                "subtitle" to newSubTitle,
                "price" to newPrice,
                "discount" to newDiscount,
                "tpNo" to newContact,
                "locate" to newLocation,
                "about" to newAbout,
                "category" to newCategory,
                "groceryimg" to groceryimg,
                "email" to newemail
            )

            // Update the data in the database
            houseRef.updateChildren(groceryeUpdates)
                .addOnSuccessListener {
                    // Data updated successfully
                    Toast.makeText(this, "Grocery updated successfully", Toast.LENGTH_SHORT).show()

// Create an Intent to launch the RecyclerFood activity
                    val intent = Intent(this, RecyclerUserGroceryActivity::class.java)
// Pass the selected category value as an extra

                    startActivity(intent)
                    finish() // Close the current activity


                    // finish() // Close the activity
                }
                .addOnFailureListener {
                    // Failed to update the data
                    Toast.makeText(this, "Failed to update Grocery", Toast.LENGTH_SHORT).show()
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
                    editGroceryeimg.setImageBitmap(bitmap)
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
                    val image = findViewById<ImageView>(R.id.GrouploadbtnEdit)
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
                        val image = findViewById<ImageView>(R.id.GrouploadbtnEdit)
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
