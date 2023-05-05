package com.agri.myapp

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter (
    private val productList: ArrayList<Products>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ProductAdapter.ProductHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Products)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val productView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ProductHolder(productView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val currentProduct = productList[position]
        holder.itemName.text = currentProduct.productName.toString()
        holder.itemPrice.text = currentProduct.productPrice.toString()

        val bytes = android.util.Base64.decode(currentProduct.productImage, android.util.Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        holder.itemcard_img.setImageBitmap(bitmap)
    }

    inner class ProductHolder(productView: View) : RecyclerView.ViewHolder(productView), View.OnClickListener {
        val itemName: TextView = productView.findViewById(R.id.itemName)
        val itemPrice: TextView = productView.findViewById(R.id.itemPrice)
        val itemcard_img: ImageView = productView.findViewById(R.id.itemcard_img)

        init {
            productView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listener.onItemClick(productList[adapterPosition])
        }
    }
}