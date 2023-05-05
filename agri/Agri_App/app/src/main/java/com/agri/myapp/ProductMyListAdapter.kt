package com.agri.myapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class ProductMyListAdapter ( private val productList: ArrayList<Products>,
                             private val listener: ProductMyListAdapter.OnItemClickListener
): RecyclerView.Adapter<ProductMyListAdapter.ProductListHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Products)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListHolder {
        val productView = LayoutInflater.from(parent.context).inflate(R.layout.seller_item, parent, false)
        return ProductListHolder(productView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductListHolder, position: Int) {
        val currentProduct = productList[position]
        holder.itemSellName.text = currentProduct.productName.toString()
        holder.itemSellPrice.text = currentProduct.productPrice.toString()


        val bytes = android.util.Base64.decode(currentProduct.productImage, android.util.Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        holder.itemSell_img.setImageBitmap(bitmap)
    }

    inner class ProductListHolder(productView: View) : RecyclerView.ViewHolder(productView), View.OnClickListener {
        val itemSellName: TextView = productView.findViewById(R.id.itemSellName)
        val itemSellPrice: TextView = productView.findViewById(R.id.itemSellPrice)
        val itemSell_img: ImageView = productView.findViewById(R.id.itemSell_img)
        val editbtn: ImageView = productView.findViewById(R.id.editbtn)
        val deletebtn: ImageView = productView.findViewById(R.id.deletebtn)
        init {
            productView.setOnClickListener(this)
            editbtn.setOnClickListener {
                val intent = Intent(productView.context, Edit_form_Activity::class.java)
                intent.putExtra("productId", productList[adapterPosition].productId)
                productView.context.startActivity(intent)
            }
            deletebtn.setOnClickListener {
                val dbRef = FirebaseDatabase.getInstance().getReference("Product").child(productList[adapterPosition].productId?:"")
                val mTask = dbRef.removeValue()

                mTask.addOnSuccessListener {
                    Toast.makeText(productView.context, "Product data deleted", Toast.LENGTH_LONG).show()

                    val intent = Intent(productView.context, SellerRecyclerActivity::class.java)

                    productView.context.startActivity(intent)


                }.addOnFailureListener{ error ->
                    Toast.makeText(productView.context, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
                }

            }
        }


        override fun onClick(v: View) {
            listener.onItemClick(productList[adapterPosition])
        }
    }
}
