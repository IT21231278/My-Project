package com.agri.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // val foodId: String? = item.foodId
        // intent.putExtra("itemId", foodId?.toString())
        val home= findViewById<ImageView>(R.id.home)
        val add= findViewById<ImageView>(R.id.add)
        val mylist= findViewById<ImageView>(R.id.profile)
        add.setOnClickListener {


            val fragment = AddItemFragment()

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit()
        }

        home.setOnClickListener{
            val intent = Intent(this@MainActivity, ItemRecyclerActivity::class.java)
            startActivity(intent)
        }
        mylist.setOnClickListener{
            val intent = Intent(this@MainActivity, SellerRecyclerActivity::class.java)
            startActivity(intent)
        }


    }
}