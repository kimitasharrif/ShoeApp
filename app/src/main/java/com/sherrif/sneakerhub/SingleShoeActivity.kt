package com.sherrif.sneakerhub

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.sherrif.sneakerhub.helpers.SQLiteCartHelper

class SingleShoeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_singleshoe)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val shoename = findViewById<MaterialTextView>(R.id.name)
        val shoebrand = findViewById<MaterialTextView>(R.id.brand)
        val shoeprice = findViewById<MaterialTextView>(R.id.price)
        val shoephoto = findViewById<ImageView>(R.id.shoeimage)
        val cartbtn = findViewById<MaterialButton>(R.id.addtocart)

        // Retrieve data from intent extras
        val shoe_id = intent.extras?.getString("shoe_id")
        val category_id = intent.extras?.getString("category_id")
        val name = intent.extras?.getString("name")
        val price = intent.extras?.getString("price")
        val description = intent.extras?.getString("description")
        val brand = intent.extras?.getString("brand")
        val photo = intent.extras?.getString("photo")
        val quantity = intent.extras?.getString("quantity")

        shoename.text = name
        shoebrand.text = brand
        shoeprice.text = "$price  KES"

        // Load the shoe photo using Glide
        Glide.with(this)
            .load(photo)
            .apply(RequestOptions()
                .placeholder(R.drawable.error_image) // Placeholder image while loading
                .error(R.drawable.error_image) // Error image if loading fails
            )
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    Log.e("SingleShoeActivity", "Error loading image", e)
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    // Optionally do something when the resource is ready
                    return false
                }
            })
            .into(shoephoto)
//        cart4.dp

        cartbtn.setOnClickListener {
            val helper = SQLiteCartHelper(applicationContext)
            try {
                helper.insertData(
                    shoe_id!!,
                    category_id!!,
                    name!!,
                    price!!,
                    description!!,
                    brand!!,
                    photo!!,
                    quantity!!
                )
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "An error Occurred", Toast.LENGTH_SHORT).show()
            }
        }

        // Test item count inside the cart
        val helper = SQLiteCartHelper(applicationContext)
        val count = helper.getNumberOfItems()

        // Get all the items
        val items = helper.getAllItems()
        for (item in items) {
            // Optionally display item data or use it as needed
        }

        helper.totalcost()
    }
}
//cart4.db