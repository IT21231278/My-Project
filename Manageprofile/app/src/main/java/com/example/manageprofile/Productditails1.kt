package com.example.manageprofile


import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Document

class Productditails1 : AppCompatActivity() {


    private lateinit var productName: TextView
    private lateinit var qty: TextView
    private lateinit var description: TextView
    private lateinit var subTot: TextView
    private lateinit var shippingfee: TextView
    private lateinit var totAmo: TextView

    val db = Firebase.firestore
    private lateinit var docId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productditails1)

        productName = findViewById(R.id.productName)
        qty = findViewById(R.id.qty)
        description = findViewById(R.id.description)
        subTot = findViewById(R.id.subtot)
        shippingfee = findViewById(R.id.shippingfree)
        totAmo = findViewById(R.id.totamo)


        description.isEnabled = false


        //data read .....................................

        db.collection("mycart")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                     docId = document.id
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val pName = document.data.get("ProductName").toString()
                    val pQty = document.data.get("Quantity").toString()
                    val pDes = document.data.get("Description").toString()
                    val pSub = document.data.get("SubTot").toString()
                    val pShipp = document.data.get("ShippingFree").toString()
                    val pAm = document.data.get("TotalAmo").toString()

                    productName.setText("product Name                                "+pName)
                    qty.setText("Quantity                                "+pQty)
                    description.setText(pDes)
                    subTot.setText("Sub Total :                   "+pSub+".00")
                    shippingfee.setText("Shipping Fee :             "+pShipp+".00")
                    totAmo.setText("Total Amount :            "+pAm+".00")


                    Toast.makeText(this, "",Toast.LENGTH_LONG).show()


                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        findViewById<Button>(R.id.btndelete).setOnClickListener(View.OnClickListener {
            db.collection("mycart").document(docId).delete().addOnSuccessListener {
                Toast.makeText(this,"Delete Success Full..",Toast.LENGTH_LONG).show()
                startActivity(Intent(this,Productditails1::class.java))
            }.addOnFailureListener {
                Toast.makeText(this,"",Toast.LENGTH_LONG).show()
            }
        })

        findViewById<Button>(R.id.btnChangeItem).setOnClickListener(View.OnClickListener {
            Toast.makeText(this,"Update Ui",Toast.LENGTH_LONG).show()
        })

        findViewById<Button>(R.id.btncontinewShopping).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,Productditalis2::class.java))
        })
        findViewById<Button>(R.id.btnoder).setOnClickListener(View.OnClickListener {
            Toast.makeText(this,"Update Ui",Toast.LENGTH_LONG).show()
        })
    }

    //model class Use..........................................

    /* val TaskList = ArrayList<modelclass>()
    db.collection("mycart/")
        .get()
        .addOnSuccessListener {
            if(it.isEmpty){
                Toast.makeText(this,"No Task Found", Toast.LENGTH_LONG).show()
            }
            for (doc in it){
                val taskmodel = doc.toObject(modelclass::class.java)
                TaskList.add(taskmodel)
                TaskList.get(0)
                pNamed.text = taskmodel.productName
                pQty.text = taskmodel.qty
                pDes.text = taskmodel.description
                pSub.text = taskmodel.subTot
                pShipp.text = taskmodel.shppingFree
                pTotA.text = taskmodel.totAmo
            }

        }
        .addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents.", exception)
        }

}*/
}
