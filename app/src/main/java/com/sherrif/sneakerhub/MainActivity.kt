package com.sherrif.sneakerhub

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.gson.GsonBuilder
import com.sherrif.sneakerhub.adapters.ShoeAdapter
import com.sherrif.sneakerhub.constants.Constants
import com.sherrif.sneakerhub.helpers.ApiHelper
import com.sherrif.sneakerhub.helpers.PrefsHelper
import com.sherrif.sneakerhub.helpers.SQLiteCartHelper
import com.sherrif.sneakerhub.models.Shoe
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: ShoeAdapter
    private lateinit var progress: ProgressBar
    private lateinit var itemList: List<Shoe>
    private lateinit var swiperrefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Find the MaterialTextView by its ID
        val myCartTextView = findViewById<ImageView>(R.id.imagecart)
        val badge = findViewById<TextView>(R.id.badge)

        val helper = SQLiteCartHelper(applicationContext)
        badge?.text = "" + helper.getNumberOfItems()

        // Set an OnClickListener to the MaterialTextView
        myCartTextView.setOnClickListener {
            // Create an intent to start CartActivity
            val intent = Intent(this, MyCart::class.java)
            startActivity(intent)
        }
        val user = findViewById<MaterialTextView>(R.id.user)
        val signin = findViewById<MaterialButton>(R.id.signin)
        val profile = findViewById<CircleImageView>(R.id.profileimage)

        profile.visibility = View.VISIBLE
        profile.setOnClickListener {
            //Link to Member Profile TODO Later
            startActivity(Intent(applicationContext, ProfileActivity::class.java))
        }//end

        //Set below 3 Views to GONE/Disappear
        signin.visibility = View.GONE
//        signout.visibility = View.GONE
//        profile.visibility = View.GONE

        //Access user access token from Prefs
        val token = PrefsHelper.getPrefs(applicationContext, "access_token")
        if (token.isEmpty()) {
            //If user Token does  not exist, Update user TextView with Not Logged In
            user.text = "Not Logged In"
            //Make sign in button visible
            signin.visibility = View.VISIBLE
            signin.setOnClickListener {
                //Link to Sign in Activity
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        } else {
//            If user Token  exist,
//            Make Profile Button visible
            profile.visibility = View.VISIBLE
            profile.setOnClickListener {
                //Link to Member Profile TODO Later
                startActivity(Intent(applicationContext, ProfileActivity::class.java))
            }//end


            //Access username from Prefs
            val surname = PrefsHelper.getPrefs(applicationContext, "surname")
            //Update user textView with Logged in User
            user.text = "Welcome $surname"
        }

        // Initialize views
        recyclerview = findViewById(R.id.recyclerview)
        progress = findViewById(R.id.progress)
        swiperrefresh = findViewById(R.id.swiperefresh)

        // Set up the GridLayoutManager with 2 columns
        val layoutManager = GridLayoutManager(this, 2)
        recyclerview.layoutManager = layoutManager
        recyclerview.setHasFixedSize(true)

        // Initialize the adapter
        adapter = ShoeAdapter(this)
        recyclerview.adapter = adapter

        // Set the shoe data
        getShoe()

        // Set up button click listeners
        findViewById<MaterialButton>(R.id.button_show_all).setOnClickListener {
            adapter.filterByCategory(-1) // -1 indicates showing all shoes
        }

        findViewById<MaterialButton>(R.id.button_category_sneakers).setOnClickListener {
            adapter.filterByCategory(1) // Replace 1 with the actual category ID for Sneakers
        }

        findViewById<MaterialButton>(R.id.button_category_boots).setOnClickListener {
            adapter.filterByCategory(2) // Replace 2 with the actual category ID for Boots
        }

        // Set up SwipeRefreshLayout to refresh data
        swiperrefresh.setOnRefreshListener {
            getShoe() // Re-fetch data
        }
    }

    private fun getShoe() {
        val api = Constants.BASE_URL + "/shoes"
        val helper = ApiHelper(applicationContext)
        helper.get(api, object : ApiHelper.CallBack {
            override fun onSuccess(result: JSONArray?) {
                val shoegson = GsonBuilder().create()
                itemList = shoegson.fromJson(result.toString(), Array<Shoe>::class.java).toList()
                adapter.setListItems(itemList)
                progress.visibility = View.GONE
                swiperrefresh.isRefreshing = false

                // Set up search functionality
                val shoesearch = findViewById<EditText>(R.id.search)
                shoesearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        filter(s.toString())
                    }
                    override fun afterTextChanged(s: Editable?) {}
                })
            }

            override fun onSuccess(result: JSONObject?) {
                // Handle JSON object success response if needed
                progress.visibility = View.GONE
                swiperrefresh.isRefreshing = false
            }

            override fun onFailure(result: String?) {
                progress.visibility = View.GONE
                swiperrefresh.isRefreshing = false
                Log.d("failureerrors", result.toString())
            }
        })
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<Shoe> = ArrayList()
        for (item in adapter.originalList) {
            if (item.name.lowercase().contains(text.lowercase())) {
                filteredList.add(item)
            }
        }
        adapter.filterList(filteredList)
    }
}
