package com.example.bodima.fragments
import android.app.Activity
import android.Manifest
import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.bodima.R
import com.example.bodima.models.Grocery
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class GroceryAddFragment : Fragment() {

    private  lateinit var database: DatabaseReference

    private var _binding: View? = null

    private val binding get() = _binding!!
    private val CAMERA_PERMISSION_CODE = 100
    private val CAMERA = 101
    var sImage:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("Grocery")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_grocery_add, container, false)

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }


        val postbutton = view.findViewById<Button>(R.id.postBtn)
        val imagebutton = view.findViewById<ImageButton>(R.id.uploadbtn)


        imagebutton.setOnClickListener {
            insert_Image()

        }

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser


        val userEmail = currentUser?.email ?: ""

        val title = view.findViewById<EditText>(R.id.titlextext)
        val subtitle = view.findViewById<EditText>(R.id.subtitletext)
        val discount = view.findViewById<EditText>(R.id.discounttext)
        val price = view.findViewById<EditText>(R.id.pricetext)
        val tpNO = view.findViewById<EditText>(R.id.teletext)
        val locate = view.findViewById<EditText>(R.id.lactetext)
        val about = view.findViewById<EditText>(R.id.abouttext)
        val category = view.findViewById<Spinner>(R.id.grocerydrpopdown)




        postbutton.setOnClickListener {

            if (title.text.isEmpty() || subtitle.text.isEmpty() || discount.text.isEmpty() || price.text.isEmpty() ||
                tpNO.text.isEmpty() || locate.text.isEmpty() || about.text.isEmpty()

            ) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()

            } else if(category.selectedItem.toString()=="Select Category"){

                Toast.makeText(context, "Please Select Valid Category", Toast.LENGTH_SHORT).show()

            }else if(tpNO.text.toString().length != 10) {

                Toast.makeText(context, "Please enter a valid phone number with 10 digits", Toast.LENGTH_SHORT).show()
            }
            else {

                val key = database.push().key

                val grocery = Grocery(
                    title = title.text.toString(),
                    subtitle = subtitle.text.toString(),
                    discount = discount.text.toString(),
                    price = price.text.toString(),
                    tpNO = tpNO.text.toString(),
                    locate = locate.text.toString(),
                    about = about.text.toString(),
                    category = category.selectedItem.toString(),
                    groceryimg = sImage,
                    id =key,
                    email=userEmail

                )


                key?.let {
                    database.child(it).setValue(grocery)

                }
                Toast.makeText(context, "Your Ad is Posted", Toast.LENGTH_SHORT).show()
                title.setText("")
                subtitle.setText("")
                discount.setText("")
                price.setText("")
                tpNO.setText("")
                locate.setText("")
                about.setText("")

            }
        }

        return view
    }

    private fun insert_Image() {
        val options = arrayOf<CharSequence>("Select from Gallery", "Take a Photo")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Image")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Select from Gallery" -> {
                    val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
                    galleryIntent.type = "image/*"
                    ActivityResultLauncher.launch(galleryIntent)

                }
                options[item] == "Take a Photo" -> {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        ActivityResultLauncher.launch(cameraIntent)


                    } else {
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
                    }
                }
            }
        }
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    private val ActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                if (data.data != null) {
                    val imageUri = data.data!!
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val bytes = stream.toByteArray()
                    sImage = Base64.encodeToString(bytes, Base64.DEFAULT)
                    // Display the selected image in an ImageView
//                val image = view?.findViewById<ImageView>(R.id.imageFood)
//                view.image.setImageBitmap(bitmap)
                    val image = view?.findViewById<ImageView>(R.id.uploadimg)
                    image?.setImageBitmap(bitmap)
                    // Convert the image to Base64 and save it to your variable here

                    Toast.makeText(requireActivity(), "Image selected from gallery", Toast.LENGTH_LONG).show()
                } else {
                    val thumbnail = data.extras?.get("data") as Bitmap?
                    if (thumbnail != null) {
                        val stream = ByteArrayOutputStream()
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val bytes = stream.toByteArray()
                        sImage = Base64.encodeToString(bytes, Base64.DEFAULT)
                        val image = view?.findViewById<ImageView>(R.id.uploadimg)
                        image?.setImageBitmap(thumbnail)
                        Toast.makeText(requireActivity(), "Image taken from camera", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(requireActivity(), "Image selection canceled", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                ActivityResultLauncher.launch(cameraIntent)
            } else {
                Toast.makeText(requireActivity(), "Camera permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}