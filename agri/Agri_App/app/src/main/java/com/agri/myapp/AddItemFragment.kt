package com.agri.myapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import android.Manifest

//import com.google.firebase.auth.FirebaseAuth


class AddItemFragment : Fragment() {
    private  lateinit var productDbRef: DatabaseReference
    private var _binding: View? = null

    private val binding get() = _binding!!
    private val CAMERA_PERMISSION_CODE = 100
    private val CAMERA = 101
    var sImage:String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDbRef = FirebaseDatabase.getInstance().getReference("Product")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_item, container, false)

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }


        val postbutton = view.findViewById<Button>(R.id.postbutton)
        val imagebutton = view.findViewById<ImageButton>(R.id.uploadbutton)
        postbutton.setOnClickListener {
            val userEmail = "agri@gmail.com"
            val productName = view?.findViewById<EditText>(R.id.pnameadd)?.text.toString()
            val productPrice = view?.findViewById<EditText>(R.id.priceadd)?.text.toString()
            val productQuantity = view?.findViewById<EditText>(R.id.quantityadd)?.text.toString()
            val mobile = view?.findViewById<EditText>(R.id.mobileadd)?.text.toString()
            val charges = view?.findViewById<EditText>(R.id.chargeadd)?.text.toString()
            val type = view?.findViewById<Spinner>(R.id.dropdown)?.selectedItem.toString()

            if (productName.isNullOrEmpty()) {
                requireView().findViewById<EditText>(R.id.pnameadd)?.error = "Please enter the product name"
            }

            if (productPrice.isNullOrEmpty()) {
                requireView().findViewById<EditText>(R.id.priceadd)?.error = "Please enter the product price"
            }

            if (productQuantity.isNullOrEmpty()) {
                requireView().findViewById<EditText>(R.id.quantityadd)?.error = "Please enter the quantity"
            }

            if (mobile.isNullOrEmpty()) {
                requireView().findViewById<EditText>(R.id.mobileadd)?.error = "Please enter the mobile number"
            }
            if (charges.isNullOrEmpty()) {
                requireView().findViewById<EditText>(R.id.chargeadd)?.error = "Please enter the delivery charges"
            }


            if (type.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please select a type", Toast.LENGTH_SHORT).show()
            }


            val productId = productDbRef.push().key!!

            val product = Products(productId,productName,productPrice,charges,mobile,type,productQuantity,sImage,userEmail)
            productDbRef.child(productId).setValue(product)
                .addOnCompleteListener{
                    Toast.makeText(requireContext(), "Product is inserted", Toast.LENGTH_LONG).show()
                }.addOnFailureListener{err->
                    Toast.makeText(requireContext(), "Error ${err.message}", Toast.LENGTH_LONG).show()
                }


        }
        imagebutton.setOnClickListener {
            insert_Image()

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
        _binding = null
    }

//    private fun insertProductData(){
////        val firebaseAuth = FirebaseAuth.getInstance()
////        val currentUser = firebaseAuth.currentUser
////
////        val userEmail = currentUser?.email ?: ""
//        val userEmail = "agri@gmail.com"
//        val productName = view?.findViewById<EditText>(R.id.pnameadd)?.text.toString()
//        val productPrice = view?.findViewById<EditText>(R.id.priceadd)?.text.toString()
//        val productQuantity = view?.findViewById<EditText>(R.id.quantityadd)?.text.toString()
//        val mobile = view?.findViewById<EditText>(R.id.mobileadd)?.text.toString()
//        val charges = view?.findViewById<EditText>(R.id.chargeadd)?.text.toString()
//        val type = view?.findViewById<Spinner>(R.id.dropdown)?.selectedItem.toString()
//
//        if (productName.isNullOrEmpty()) {
//            requireView().findViewById<EditText>(R.id.pnameadd)?.error = "Please enter the product name"
//        }
//
//        if (productPrice.isNullOrEmpty()) {
//            requireView().findViewById<EditText>(R.id.priceadd)?.error = "Please enter the product price"
//        }
//
//        if (productQuantity.isNullOrEmpty()) {
//            requireView().findViewById<EditText>(R.id.quantityadd)?.error = "Please enter the quantity"
//        }
//
//        if (mobile.isNullOrEmpty()) {
//            requireView().findViewById<EditText>(R.id.mobileadd)?.error = "Please enter the mobile number"
//        }
//        if (charges.isNullOrEmpty()) {
//            requireView().findViewById<EditText>(R.id.chargeadd)?.error = "Please enter the delivery charges"
//        }
//
//
//        if (type.isNullOrEmpty()) {
//            Toast.makeText(requireContext(), "Please select a type", Toast.LENGTH_SHORT).show()
//        }
//
//
//        val productId = productDbRef.push().key!!
//
//        val product = Products(productId,productName,productPrice,charges,mobile,type,sImage,userEmail)
//        productDbRef.child(productId).setValue(product)
//            .addOnCompleteListener{
//                Toast.makeText(requireContext(), "Product is inserted", Toast.LENGTH_LONG).show()
//            }.addOnFailureListener{err->
//                Toast.makeText(requireContext(), "Error ${err.message}", Toast.LENGTH_LONG).show()
//            }
//
//    }


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
                Toast.makeText(requireActivity(), "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}