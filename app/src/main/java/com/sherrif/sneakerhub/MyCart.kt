package com.sherrif.sneakerhub

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.sherrif.sneakerhub.adapters.ShoeCartAdapter
import com.sherrif.sneakerhub.helpers.PrefsHelper
import com.sherrif.sneakerhub.helpers.SQLiteCartHelper

class MyCart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mycart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //fetch recycler view and check out button

        val recyclerview = findViewById<RecyclerView>(R.id.recycler)
        val total = findViewById<MaterialTextView>(R.id.total)
        val btncheckout = findViewById<MaterialButton>(R.id.checkout)
        val btnclear = findViewById<MaterialButton>(R.id.clearallcart)

        btnclear.setOnClickListener {
            val helper = SQLiteCartHelper(applicationContext)
            helper.clearcart()
            Toast.makeText(applicationContext, "Clear Cart Item", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }



         btncheckout.setOnClickListener {
            //usings prefs check if token exists
            val token = PrefsHelper.getPrefs(applicationContext, "access_token")
//            Toast.makeText(applicationContext, "$token", Toast.LENGTH_SHORT).show()
            if (token.isEmpty()) {
//                token does not exists , not logged in
                Toast.makeText(applicationContext, "Not Logged In", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()

            } else {
                if (isLocationEnabled()){
//                return true meaninig the location is enabled
                    //              token exist you are logged in and can proceed to checkout step 1
                    Toast.makeText(applicationContext, "Logged In", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext,Check1Activity::class.java)
                    startActivity(intent)
                    finish()
//
                }else{
//                return false meaning the gps location is not enabled
                    Toast.makeText(applicationContext, "GPS IS OFF", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

                }

            }
        }

        //put total cost in a textview
        val helper = SQLiteCartHelper(applicationContext)


        //        clear all cart items
//        helper.clearcart()
//        get total from the helper
        total.text = "Total Cost" + helper.totalcost()
//
////        if total cost is zero and your cart is empty,hide the check out button
        if (helper.totalcost() == 0.0) {

            btncheckout.visibility = View.GONE


        }// end if
        // set the layout
        val layoutmanager = LinearLayoutManager(applicationContext)
        recyclerview.layoutManager = layoutmanager

        recyclerview.setHasFixedSize(true)
        //fi total items is zero show cart is empty

        if (helper.getNumberOfItems() == 0) {
            Toast.makeText(applicationContext, "Your cart is empty", Toast.LENGTH_LONG).show()
        } else {
            // access adapter and provide it with data using get all items
            val adapter = ShoeCartAdapter(applicationContext)
            adapter.setListIteems(helper.getAllItems())// now we passed our data
            // link your adapetr to recycler
            recyclerview.adapter = adapter
        }
    }//end of on create

    // we want to make sure when the user clicks back button they are redirected to main activity
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        //this clears therunning/opens activities  and go back to main activity
        finishAffinity()
    }//end of onbackpressed

    //function that loads the option menu in the toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //load the cart xml
//        menuInflater.inflate(R.menu.cart, menu)



        return super.onCreateOptionsMenu(menu)
    }

    //    function to make options menu clickable
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //clear cart items
        if (item.itemId == R.id.clearcart) {
            val helper = SQLiteCartHelper(applicationContext)
            helper.clearcart()
            Toast.makeText(applicationContext, "Clear Cart Item", Toast.LENGTH_SHORT).show()

        }//end of if
        //go back tolabs
        if (item.itemId == R.id.backtolabs) {
            startActivity(Intent(applicationContext, MainActivity::class.java))

        }

        return super.onOptionsItemSelected(item)
    }
    private fun isLocationEnabled() :Boolean{
//        get system service - which retrieves the locationmanager
        val locationmanager: LocationManager = getSystemService(Context.LOCATION_SERVICE)as LocationManager
        return locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)  ||
                locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}