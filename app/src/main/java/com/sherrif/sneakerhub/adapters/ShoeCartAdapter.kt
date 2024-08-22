package com.sherrif.sneakerhub.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.sherrif.sneakerhub.R
import com.sherrif.sneakerhub.helpers.SQLiteCartHelper
import com.sherrif.sneakerhub.models.Shoe
import com.squareup.picasso.Picasso

class ShoeCartAdapter(private val context: Context) : RecyclerView.Adapter<ShoeCartAdapter.ViewHolder>() {
    private var itemList: List<Shoe> = listOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_shoe_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Fetch the views
        val name = holder.itemView.findViewById<MaterialTextView>(R.id.name)
        val brand = holder.itemView.findViewById<MaterialTextView>(R.id.brand)
        val price = holder.itemView.findViewById<MaterialTextView>(R.id.price)
        val photo = holder.itemView.findViewById<ImageView>(R.id.shoeimage)
        val btnRemove = holder.itemView.findViewById<MaterialButton>(R.id.remove)

        // Assign one cart item
        val shoe = itemList[position]
        name.text = shoe.name
        brand.text = shoe.brand
        price.text = "${shoe.price} KES"

        // Load the image using Picasso
        val imageUrl = shoe.photo
        if (imageUrl.isNullOrEmpty()) {
            // Handle empty or null image URL
            photo.setImageResource(R.drawable.error_image) // Set a default image
        } else {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.shoe) // Placeholder image
                .error(R.drawable.error_image) // Error image
                .into(photo)
        }

        btnRemove.setOnClickListener {
            val shoeId = shoe.shoe_id
            val helper = SQLiteCartHelper(context)
            helper.clearCartById(shoeId)
            // Update the itemList and notify the adapter of the change
            val updatedList = itemList.filter { it.shoe_id != shoeId }
            setListItems(updatedList)
        }

        Toast.makeText(context, "Total cost is ${shoe.price}", Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setListItems(data: List<Shoe>) {
        itemList = data
        notifyDataSetChanged()
    }
}
