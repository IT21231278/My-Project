package com.agri.myapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
//import android.text.Editable
import android.util.Base64
import android.util.Log
import android.widget.*
//import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
//import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream

class Edit_form_Activity : AppCompatActivity() {

    private lateinit var name:TextView
    private lateinit var price:TextView
    private lateinit var charges:TextView
    private lateinit var type:Spinner
    private lateinit var quantity:TextView
    private lateinit var phone:TextView
    private lateinit var image:ImageView
    private lateinit var uploadImage:ImageButton
    private lateinit var update:Button

    private val CAMERA_PERMISSION_CODE = 100
    private val CAMERA = 101
    var sImage:String?=""
    var defaultImage:String?=""
    var up:Int?=0
    private var productId: String? = null
    private  lateinit var productDbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_form)

        productId = intent.getStringExtra("productId").toString()
        val database = Firebase.database
        val myRef = database.reference.child("Product")
        val query = myRef.orderByChild("productId").equalTo(productId)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }

        name = findViewById(R.id.pnametextEdit)
        price = findViewById(R.id.pricetextEdit)
        charges = findViewById(R.id.chargeaddEdit)
        quantity = findViewById(R.id.quantityaddEdit)
        phone = findViewById(R.id.mobileaddEdit)

        type = findViewById(R.id.dropdownEdit)
        image = findViewById(R.id.uploadimgEdit)

        update = findViewById(R.id.postBtnEdit)
        uploadImage = findViewById(R.id.uploadbtnEdit)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {

                        val productName = snapshot.child("productName").getValue(String::class.java)
                        val productPrice = snapshot.child("productPrice").getValue(String::class.java)?.toLong()
                        val productCharges = snapshot.child("productCharge").getValue(String::class.java)
                        val productMobile = snapshot.child("productMobile").getValue(String::class.java)
                        val productQuantity = snapshot.child("productCount").getValue(String::class.java)
                        val productType = snapshot.child("productType").getValue(String::class.java)
                        val productImageBase64 = snapshot.child("productImage").getValue(String::class.java)
                        //val email = snapshot.child("email").getValue(String::class.java)

                        defaultImage = productImageBase64 ?: "default image"

                        val bytes = Base64.decode(productImageBase64, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        image.setImageBitmap(bitmap)


                        // Set the retrieved data to the corresponding UI elements
                        name.text = productName

                        price.text = productPrice?.toString()

                        charges.text = productCharges
                        quantity.text = productQuantity
                        phone.text = productMobile

                        val startIndex = (type.adapter as ArrayAdapter<String>).getPosition(productType)
                        type.setSelection(startIndex)



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

            val imageUri = sImage ?: ""
            image.setImageURI(Uri.parse(imageUri))

        }
        update.setOnClickListener {
            val id: String? = intent.getStringExtra("productId").toString()
            val productRef = FirebaseDatabase.getInstance().getReference("Product").child(id ?: "")


            // Get the data from the UI elements
            val name = name.text.toString().trim()
            val price = price.text.toString().trim()
            val quantity = quantity.text.toString().trim()
            val type = type.selectedItem.toString().trim()
            val charges = charges.text.toString().trim()
            val phone = phone.text.toString().trim()

//            val firebaseAuth = FirebaseAuth.getInstance()
//            val currentUser = firebaseAuth.currentUser
//
//            val userEmail = currentUser?.email ?: ""
            val userEmail = "agri@gmail.com"

            val productImage:String
            if(up == 1){
                productImage = sImage?:""
            }else{
                productImage = defaultImage ?:""
            }


            // Create a HashMap to update the data
            val productUpdates = hashMapOf<String, Any>(

                "productId" to id!!,
                "productName" to name,
                "productPrice" to price,
                "productCount" to quantity,
                "productMobile" to phone,
                "productCharge" to charges,
                "productType" to type,
                "productImage" to productImage,
                "Email" to userEmail
            )

            // Update the data in the database
            productRef.updateChildren(productUpdates)
                .addOnSuccessListener {
                    // Data updated successfully
                    Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()

// Create an Intent to launch the RecyclerFood activity
                    val intent = Intent(this, SellerRecyclerActivity::class.java)
// Pass the selected category value as an extra

                    startActivity(intent)
                    finish() // Close the current activity


                    // finish() // Close the activity
                }
                .addOnFailureListener {
                    // Failed to update the data
                    Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show()
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

                    val image = findViewById<ImageView>(R.id.uploadimgEdit)
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