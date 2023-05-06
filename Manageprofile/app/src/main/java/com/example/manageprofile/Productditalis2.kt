package com.example.manageprofile

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private lateinit var productName: TextView
private lateinit var qty: Spinner
private lateinit var price: TextView
private lateinit var stockKg: TextView
private lateinit var shippingdays: TextView
private lateinit var ShippingCharger: TextView

val db = Firebase.firestore
private lateinit var docId: String

class Productditalis2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productditalis2)

        productName = findViewById(R.id.namePro)
        qty = findViewById(R.id.qtyPro)
        price = findViewById(R.id.pricePro)
        stockKg = findViewById(R.id.stockPro)
        shippingdays = findViewById(R.id.shppingdaysPro)
        ShippingCharger = findViewById(R.id.shippingPricePro)

        db.collection("ShoppingCart")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    docId = document.id
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val proName = document.data.get("ProductName").toString()
                    val proQty = document.data.get("Quantity").toString()
                    val proPrice = document.data.get("Price ").toString()
                    val proStock = document.data.get("Stock").toString()
                    val proShppingdays = document.data.get("ShippingDays").toString()
                    val proShppingRs = document.data.get("shippingCharge").toString()

                    productName.setText("product Name :                                  "+proName)
                    price.setText(proPrice+".00")
                    stockKg.setText(proStock)
                    shippingdays.setText(proShppingdays+" Days")
                    ShippingCharger.setText(proShppingRs+".00")


                    findViewById<Button>(R.id.btnQtyUpDate).setOnClickListener(View.OnClickListener {
                        val qtyUpdate = mapOf(
                            "Qty" to qty.selectedItem.toString()
                        )
                        db.collection("ShoppingCart").document(docId).update(qtyUpdate).addOnSuccessListener {
                            Toast.makeText(this,"Successfully Update!",Toast.LENGTH_LONG).show()

                            //Algoretham

                        }.addOnFailureListener {
                            Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()
                        }
                    })


                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        findViewById<Button>(R.id.btnAddTocard).setOnClickListener(View.OnClickListener {
            Toast.makeText(this,"Product Add Card",Toast.LENGTH_LONG).show()
            // navigate Product Page
           // startActivity(Intent(this,/*home Activity*/::class.java))
        })
    }
}