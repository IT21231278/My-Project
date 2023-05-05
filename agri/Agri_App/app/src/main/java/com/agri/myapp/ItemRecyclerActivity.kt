package com.agri.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context
import java.util.Locale


class ItemRecyclerActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productArrayList: ArrayList<Products>
    private lateinit var tempArrayList: ArrayList<Products>
    private lateinit var productType: Spinner
    private lateinit var resultvalue: TextView
    private lateinit var search: SearchView



    companion object {
        @JvmField val TAG: String = ItemRecyclerActivity::class.java.simpleName
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_recycler)


        resultvalue = findViewById<TextView>(R.id.noresult)
        productType = findViewById<Spinner>(R.id.itemstype)
        productRecyclerView = findViewById(R.id.all_items)
        productRecyclerView.layoutManager = LinearLayoutManager(this)

        productRecyclerView.hasFixedSize()
        productArrayList = arrayListOf()
        tempArrayList = arrayListOf()
        val locale = Locale.getDefault()

        search = findViewById<SearchView>(R.id.searchall)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Refresh the list with the updated search query
                tempArrayList.clear()

                val searchtext = newText!!.toLowerCase(locale)

                if (searchtext.isNotEmpty()) {
                    productArrayList.forEach {
                        if (it.productName!!.toLowerCase(locale).contains(searchtext))  {
                            tempArrayList.add(it)
                            resultvalue.text = tempArrayList.size.toString()
                        }
                    }
                    if (tempArrayList.isEmpty()) {
                        resultvalue.text = "0"
                        Toast.makeText(this@ItemRecyclerActivity, "No result found", Toast.LENGTH_SHORT).show()
                    }

                    productRecyclerView.adapter!!.notifyDataSetChanged()


                } else {

                    tempArrayList.clear()
                    tempArrayList.addAll(productArrayList)
                    resultvalue.text = tempArrayList.size.toString()
                    productRecyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }
        })
        val selectedType="All"
        productType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedType = parent.getItemAtPosition(position).toString()
                //foodType.setSelection(0)
                getData(selectedType)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //  foodType.setSelection(0)
                getData("All")
            }
        }

        // Set the default selected item to "All"
        getData(selectedType)
        productType.setSelection(0)
        getData(selectedType)

    }


    private fun getData(selectedType: String) {
        val database = Firebase.database
        val query = database.reference.child("Product")


        when(selectedType){
            "All"->{
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        productArrayList.clear() // clear the previous data from the list
                        tempArrayList.clear()
                        if (snapshot.exists()) {
                            for (productsnapshot in snapshot.children) {
                                val item = productsnapshot.getValue(Products::class.java)
                                productArrayList.add(item!!)
                            }
                            tempArrayList.addAll(productArrayList)
                            productRecyclerView.adapter = ProductAdapter(tempArrayList, object : ProductAdapter.OnItemClickListener {
                                override fun onItemClick(item: Products) {



//                                    val intent = Intent(this@ItemRecyclerActivity, SingleProduct::class.java)
//                                    val Id: String? = item.productId
//                                    intent.putExtra("itemId", Id?.toString())
//                                    startActivity(intent)
                                }
                            })


                            resultvalue.text = productArrayList.size.toString()
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
                            productRecyclerView.adapter = ProductAdapter(tempArrayList, object : ProductAdapter.OnItemClickListener {
                                override fun onItemClick(item: Products) {


//                                    val intent = Intent(this@ItemRecyclerActivity, SingleProduct::class.java)
//                                    val Id: String? = item.productId
//                                    intent.putExtra("itemId", Id?.toString())
//                                    startActivity(intent)
                                }
                            })
                            resultvalue.text = productArrayList.size.toString()
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
                            productRecyclerView.adapter = ProductAdapter(tempArrayList, object : ProductAdapter.OnItemClickListener {
                                override fun onItemClick(item: Products) {

//                                    val intent = Intent(this@ItemRecyclerActivity, SingleProduct::class.java)
//                                    val Id: String? = item.productId
//                                    intent.putExtra("itemId", Id?.toString())
//                                    startActivity(intent)
                                }
                            })
                            resultvalue.text = productArrayList.size.toString()
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