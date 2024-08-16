package com.sherrif.sneakerhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textview.MaterialTextView
import com.sherrif.sneakerhub.helpers.PrefsHelper

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val user = findViewById<MaterialTextView>(R.id.user)
        val signout = findViewById<MaterialTextView>(R.id.signout)
        //Link to PrefHelper and Clear Prefs
        // Create an Intent to start MainActivity
        signout.setOnClickListener {
            PrefsHelper.clearPrefs(this)  // Use `this` for context
        val intent = Intent(this, MainActivity::class.java)

        // Start MainActivity
        startActivity(intent)

        // Finish the current activity (optional, if you want to close this activity)
        finishAffinity()  // or just `finish()` if you want to close only the current activity
            }
        val token = PrefsHelper.getPrefs(applicationContext, "access_token")
        if (token.isEmpty()){
            //If user Token does  not exist, Update user TextView with Not Logged In
            user.text = "Not Logged In"
            //Make sign in button visible
        }
        else{
//

            //Access username from Prefs
            val surname = PrefsHelper.getPrefs(applicationContext, "surname")
            //Update user textView with Logged in User
            user.text = "Welcome $surname"
            //Make signout button visble
//            signout.visibility = View.VISIBLE
//
        }
        val back = findViewById<MaterialTextView>(R.id.back)
        back.setOnClickListener{
            PrefsHelper.clearPrefs(applicationContext)
            startActivity(intent)
            finishAffinity()
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
        // Find the MaterialTextView by its ID
        val myCartTextView = findViewById<MaterialTextView>(R.id.mycart)

        // Set an OnClickListener to the MaterialTextView
        myCartTextView.setOnClickListener {
            // Create an intent to start CartActivity
            val intent = Intent(this, MyCart::class.java)
            startActivity(intent)
        }
    }
}