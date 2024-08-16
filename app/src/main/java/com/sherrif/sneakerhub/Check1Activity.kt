package com.sherrif.sneakerhub

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.sherrif.sneakerhub.helpers.PrefsHelper
import com.sherrif.sneakerhub.helpers.SQLiteCartHelper
import java.io.IOException

class Check1Activity : AppCompatActivity() {
    private  lateinit var editLatitude: TextInputEditText
    private lateinit var editLongitude: TextInputEditText
    private lateinit var getlocation: MaterialTextView
    private lateinit var progress: ProgressBar
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //Convert cordinates to Address
    fun getAddress(latlng: LatLng, callback: (String) -> Unit) {
        Thread {
            try {
                val geoCoder = Geocoder(this)
                val list = geoCoder.getFromLocation(latlng.latitude, latlng.longitude, 1)
                val address = list?.get(0)?.getAddressLine(0) ?: "Unknown Location"
                runOnUiThread {
                    callback(address)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    callback("Error retrieving address")
                }
            }
        }.start()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_check1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        editLatitude = findViewById(R.id.editlatitude)
        editLongitude = findViewById(R.id.editlongitude)
        progress = findViewById(R.id.progress)
        getlocation = findViewById(R.id.getlocation)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        progress.visibility = View.GONE
        getlocation.setOnClickListener {
            //TODO
            progress.visibility = View.VISIBLE
            requestLocation()
        }//end
        //access the sqlitecarthelper
        val textpay = findViewById<MaterialTextView>(R.id.total)
        val helper = SQLiteCartHelper(applicationContext)
        textpay.text= "KES "+ helper.totalcost()
//        val helper.t = PrefsHelper.savePrefs(this ,"total_amount")

        val complete = findViewById<MaterialButton>(R.id.complete)
        complete.setOnClickListener {
//            Again save the actual coordinates to Prefs
            PrefsHelper.savePrefs(applicationContext, "latitude",
                editLatitude.text.toString())
            PrefsHelper.savePrefs(applicationContext, "longitude",
                editLongitude.text.toString())
            val intent = Intent(applicationContext, CompleteActivity::class.java)
            startActivity(intent)
        }

    }//end onCreate
    //Function to check if user accepted permission or not
    //If user has not accepted permissions, Give them dialog to decide
    fun requestLocation(){
        if(ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION),
                123)
        }//end if
        else {
            getLocation() //get Lat and Lon
        }
    }//end function


    @SuppressLint("MissingPermission")
    fun getLocation(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    editLatitude.setText(it.latitude.toString())
                    editLongitude.setText(it.longitude.toString())
                    progress.visibility = View.GONE

                    getAddress(LatLng(it.latitude, it.longitude)) { place ->
                        Toast.makeText(applicationContext, "here $place", Toast.LENGTH_SHORT).show()
                        // Put the place in a TextView
                        val skip = findViewById<MaterialTextView>(R.id.locationn)
                        skip.text = "Current Location \n $place"
                        requestNewLocation()
                    }
                } ?: run {
                    Toast.makeText(applicationContext, "Searching Location", Toast.LENGTH_SHORT).show()
                    progress.visibility = View.GONE
                    requestNewLocation()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, "Error $e", Toast.LENGTH_SHORT).show()
                progress.visibility = View.GONE
                requestNewLocation()
            }
    }


    lateinit var mLocationCallback: LocationCallback
    @SuppressLint("MissingPermission")
    fun requestNewLocation(){
        progress.visibility = View.VISIBLE
        Log.d("hhhhhh", "Requesting New Location")
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 10000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationCallback = object : LocationCallback(){
            override fun onLocationResult(result: LocationResult) {
                for(location in result.locations){
                    if (location!=null){
                        editLatitude.setText(location.latitude.toString())
                        editLongitude.setText(location.longitude.toString())
                        progress.visibility = View.GONE

                        //Save the coordinates in Prefs
                        PrefsHelper.savePrefs(applicationContext, "latitude", editLatitude.text.toString())
                        PrefsHelper.savePrefs(applicationContext, "longitude", editLongitude.text.toString())

                    }//end if
                    else {
                        Toast.makeText(applicationContext, "Check GPS",
                            Toast.LENGTH_SHORT).show()
                    }//end else
                }//end for
            }//end result
        }//end call back

        fusedLocationClient.requestLocationUpdates(mLocationRequest,
            mLocationCallback, Looper.getMainLooper())

    }//end function


}

