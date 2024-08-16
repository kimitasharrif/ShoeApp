package com.sherrif.sneakerhub

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        val cartbtn = findViewById<MaterialButton>(R.id.addtocart)

        // Retrieve data from intent extras
        val shoe_id = intent.extras?.getString("shoe_id")
        val category_id = intent.extras?.getString("category_id")
        val name = intent.extras?.getString("name")
        val price = intent.extras?.getString("price")
        val description = intent.extras?.getString("description")
        val brand = intent.extras?.getString("brand")
        val quantity = intent.extras?.getString("quantity")



        shoename.text = name
        shoebrand.text = brand
        shoeprice.text = "$price  KES"


        cartbtn.setOnClickListener {
            //call our class called SQLCart helper
            val helper = SQLiteCartHelper(applicationContext)
            try {
                helper.insertData(
                    shoe_id!!,
                    category_id!!,
                    name!!,
                    price!!,
                    description!!,
                    brand!!,
                    quantity!!
                )
//
            }   // end of try
            catch (e: Exception) {
                Toast.makeText(applicationContext, "An error Occurred", Toast.LENGTH_SHORT).show()

            }   //end of catch
        }//end of on click listener
        // test our item count inside our cart
        val helper = SQLiteCartHelper(applicationContext)
        val count = helper.getNumberOfItems()
//        Toast.makeText(applicationContext, "Item count is $count", Toast.LENGTH_SHORT).show()


//get all the items
        val items = helper.getAllItems()
        for (item in items) {
//                Toast.makeText(applicationContext, "${item.shoe_id}", Toast.LENGTH_SHORT).show()
//
        }//end of for loop
//        helper.clearCartById("2")
            helper.totalcost()


            //e are forced


        }
    }
