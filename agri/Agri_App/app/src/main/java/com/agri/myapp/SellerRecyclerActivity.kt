package com.agri.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Locale

class SellerRecyclerActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productArrayList: ArrayList<Products>
    private lateinit var tempArrayList: ArrayList<Products>
    private lateinit var productType: Spinner
    private lateinit var totalvalue: TextView
    private lateinit var search: SearchView
    val default="All"
    var selectedType = "All"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_recycler)

        totalvalue = findViewById<TextView>(R.id.result)
        productType = findViewById<Spinner>(R.id.itemtype)
        productRecyclerView = findViewById(R.id.seller_item)
        productRecyclerView.layoutManager = LinearLayoutManager(this)

        productRecyclerView.hasFixedSize()
        productArrayList = arrayListOf()
        tempArrayList = arrayListOf()
        val locale = Locale.getDefault()

        search = findViewById<SearchView>(R.id.search)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()

                val searchtext = newText!!.toLowerCase(locale)

                if (searchtext.isNotEmpty()) {
                    productArrayList.forEach {
                        if (it.productName!!.toLowerCase(locale).contains(searchtext))  {
                            tempArrayList.add(it)
                            totalvalue.text = tempArrayList.size.toString()
                        }
                    }
                    if (tempArrayList.isEmpty()) {
                        totalvalue.text = "0"
                        Toast.makeText(this@SellerRecyclerActivity, "No result found", Toast.LENGTH_SHORT).show()
                    }

                    productRecyclerView.adapter!!.notifyDataSetChanged()


                } else {

                    tempArrayList.clear()
                    tempArrayList.addAll(productArrayList)
                    totalvalue.text = tempArrayList.size.toString()
                    productRecyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })



        productType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedType = parent.getItemAtPosition(position).toString()
                getData(selectedType)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                getData(selectedType)
            }
        }



// Set the default selected item to "All"
        productType.setSelection(0)


        getData(selectedType)


    }

    private fun getData(selectedType: String) {
        val database = Firebase.database
        val myRef = database.reference.child("Product")
//        val firebaseAuth = FirebaseAuth.getInstance()
//        val currentUser = firebaseAuth.currentUser

        //   val userEmail = currentUser?.email ?: ""
        val userEmail = "agri@gmail.com"
        var query = myRef.orderByChild("email").equalTo(userEmail)
        Log.d("all", query.toString())

        when(selectedType){
            "All"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        productArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (productsnapshot in snapshot.children) {
                                val item = productsnapshot.getValue(Products::class.java)
                                Log.d("item", item.toString())
                                productArrayList.add(item!!)
                            }
                            tempArrayList.addAll(productArrayList)
                            productRecyclerView.adapter = ProductMyListAdapter(tempArrayList, object : ProductMyListAdapter.OnItemClickListener {
                                override fun onItemClick(item: Products) {



//                                    val intent = Intent(this@ItemRecyclerActivity, SingleProduct::class.java)
//                                    val Id: String? = item.productId
//                                    intent.putExtra("itemId", Id?.toString())
//                                    startActivity(intent)
                                }
                            })


                            totalvalue.text = productArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Fruit"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        productArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (psnapshot in snapshot.children) {
                                val item = psnapshot.getValue(Products::class.java)
                                if (item?.productType == "Fruit") {
                                    productArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(productArrayList)
                            productRecyclerView.adapter = ProductMyListAdapter(tempArrayList, object : ProductMyListAdapter.OnItemClickListener {
                                override fun onItemClick(item: Products) {


//                                    val intent = Intent(this@ItemRecyclerActivity, SingleProduct::class.java)
//                                    val Id: String? = item.productId
//                                    intent.putExtra("itemId", Id?.toString())
//                                    startActivity(intent)
                                }
                            })
                            totalvalue.text = productArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
            "Vegetable"->{

                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        productArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (psnapshot in snapshot.children) {
                                val item = psnapshot.getValue(Products::class.java)
                                if (item?.productType == "Vegetable") {
                                    productArrayList.add(item)
                                }
                            }
                            tempArrayList.addAll(productArrayList)
                            productRecyclerView.adapter = ProductMyListAdapter(tempArrayList, object : ProductMyListAdapter.OnItemClickListener {
                                override fun onItemClick(item: Products) {

//                                    val intent = Intent(this@ItemRecyclerActivity, SingleProduct::class.java)
//                                    val Id: String? = item.productId
//                                    intent.putExtra("itemId", Id?.toString())
//                                    startActivity(intent)
                                }
                            })
                            totalvalue.text = productArrayList.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancel event
                    }
                })
            }
        }
    }

}
