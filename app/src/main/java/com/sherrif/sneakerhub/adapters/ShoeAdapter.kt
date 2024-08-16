package com.sherrif.sneakerhub.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.sherrif.sneakerhub.MainActivity
import com.sherrif.sneakerhub.R
import com.sherrif.sneakerhub.models.Shoe
import com.sherrif.sneakerhub.SingleShoeActivity

// we provide context in below class to make it be an activity
class ShoeAdapter(private val context: Context) : RecyclerView.Adapter<ShoeAdapter.ViewHolder>() {
    private var itemList: List<Shoe> = listOf()
    var originalList: List<Shoe> = listOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_shoe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = holder.itemView.findViewById<MaterialTextView>(R.id.name)
        val brand = holder.itemView.findViewById<MaterialTextView>(R.id.brand)
        val price = holder.itemView.findViewById<MaterialTextView>(R.id.price)

        val shoe = itemList[position]
        name.text = shoe.name
        brand.text = shoe.brand
        price.text = "KES " + shoe.price

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SingleShoeActivity::class.java)
            intent.putExtra("shoe_id", shoe.shoe_id)
            intent.putExtra("category_id", shoe.category_id)
            intent.putExtra("name", shoe.name)
            intent.putExtra("price", shoe.price)
            intent.putExtra("description", shoe.description)
            intent.putExtra("brand", shoe.brand)
            intent.putExtra("quantity", shoe.quantity)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
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
