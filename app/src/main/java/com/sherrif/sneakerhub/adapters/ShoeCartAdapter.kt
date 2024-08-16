package com.sherrif.sneakerhub.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.sherrif.sneakerhub.MyCart
import com.sherrif.sneakerhub.R
import com.sherrif.sneakerhub.helpers.SQLiteCartHelper
import com.sherrif.sneakerhub.models.Shoe



class ShoeCartAdapter (var context :Context):RecyclerView.Adapter<ShoeCartAdapter.ViewHolder>(){
    //create a list and connect it with our model
    var itemList:List<Shoe> = listOf()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // access the single_labtest_cart
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoeCartAdapter.ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_shoe_cart, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ShoeCartAdapter.ViewHolder, position: Int) {
        // fetch three textview sand one button
        val name = holder.itemView.findViewById<MaterialTextView>(R.id.name)
        val brand = holder.itemView.findViewById<MaterialTextView>(R.id.brand)
        val price = holder.itemView.findViewById<MaterialTextView>(R.id.price)
        val btnremove = holder.itemView.findViewById<MaterialButton>(R.id.remove)

        // assign one cart item
        val shoe = itemList[position]
        name.text = shoe.name
        brand.text = shoe.brand
        price.text = shoe.price+ "KES"

        btnremove.setOnClickListener{
            val helper = SQLiteCartHelper(context)
              helper.clearCartById(shoe_id = String())

        }
        Toast.makeText(context, "total cost is ${shoe.price}", Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        // count how many items we have in the cart
        return itemList.size
    }// end of get item count
    // earliier we mentioned itemlist nis empty we will get data from aoi and then bring it to below functio
    // the data you brings must follow the data inlab test model

    fun setListIteems(data:List<Shoe>){
        itemList = data// link the data to itemlist
        notifyDataSetChanged()
//        loaded
    }
}