package com.sherrif.sneakerhub.adapters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textview.MaterialTextView
import com.sherrif.sneakerhub.R
import com.sherrif.sneakerhub.models.Shoe
import com.sherrif.sneakerhub.SingleShoeActivity

class ShoeAdapter(private val context: Context) : RecyclerView.Adapter<ShoeAdapter.ViewHolder>() {
    private var itemList: List<Shoe> = listOf()
    var originalList: List<Shoe> = listOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: MaterialTextView = itemView.findViewById(R.id.name)
        val brand: MaterialTextView = itemView.findViewById(R.id.brand)
        val price: MaterialTextView = itemView.findViewById(R.id.price)
        val photo: ImageView = itemView.findViewById(R.id.shoeimage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_shoe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shoe = itemList[position]

        holder.name.text = shoe.name
        holder.brand.text = shoe.brand
        holder.price.text = "KES ${shoe.price}"

        // Load the shoe photo using Glide
        Glide.with(context)
            .load(shoe.photo)
            .apply(RequestOptions()
                .placeholder(R.drawable.error_image) // Placeholder image while loading
                .error(R.drawable.error_image) // Error image if loading fails
            )
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    Log.e("ShoeAdapter", "Error loading image", e)
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    // Optionally do something when the resource is ready
                    return false
                }
            })
            .into(holder.photo)

        Log.d("ShoeAdapter", "Loading image URL: ${shoe.photo}")

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SingleShoeActivity::class.java).apply {
                putExtra("shoe_id", shoe.shoe_id)
                putExtra("category_id", shoe.category_id)
                putExtra("name", shoe.name)
                putExtra("price", shoe.price)
                putExtra("description", shoe.description)
                putExtra("brand", shoe.brand)
                putExtra("photo", shoe.photo)
                putExtra("quantity", shoe.quantity)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setListItems(data: List<Shoe>) {
        originalList = data
        itemList = data
        notifyDataSetChanged()
    }

    fun filterByCategory(categoryId: Int) {
        itemList = if (categoryId == -1) {
            originalList // Show all items
        } else {
            originalList.filter { it.category_id.toInt() == categoryId }
        }
        notifyDataSetChanged()
    }

    fun filterList(filteredList: List<Shoe>) {
        itemList = filteredList
        notifyDataSetChanged()
    }
}
