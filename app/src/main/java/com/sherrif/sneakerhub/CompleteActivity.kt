package com.sherrif.sneakerhub

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.os.Looper
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.textview.MaterialTextView
import com.sherrif.sneakerhub.constants.Constants
import com.sherrif.sneakerhub.helpers.ApiHelper
import com.sherrif.sneakerhub.helpers.PrefsHelper
import com.sherrif.sneakerhub.helpers.SQLiteCartHelper
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Random



class CompleteActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: MaterialTextView
    private val handler = android.os.Handler(Looper.getMainLooper())
    //    fun to generate invoice number
    fun generateInvoiceNumber():String{
        val dateFormat =  SimpleDateFormat("yyyyMMddHHmmss")
        val currentTime = Date()
        val timestamp = dateFormat.format(currentTime)
//    Toast.makeText(applicationContext, "$timestamp", Toast.LENGTH_SHORT).show()
        //you can use a random number or a sequential num to add uniqueness to invoice
        //for example using a random number
        val random = Random()
        val randomNumber = random.nextInt(1000)// 0-999
//     Toast.makeText(applicationContext, "$randomNumber", Toast.LENGTH_SHORT).show()
        //combine the timestamps and random number to create an invoice
        val invoice = "INV-$timestamp-$randomNumber"
//    Toast.makeText(applicationContext, "$invoice", Toast.LENGTH_SHORT).show()
        return invoice
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_complete)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




        val invoice_no =  generateInvoiceNumber()
        //save invoice to sharedpreferences
        PrefsHelper.savePrefs(applicationContext,"invoice_no",invoice_no)
        //access the sqlite cart helper
        val helper = SQLiteCartHelper(applicationContext)
        val itemsincart = helper.getAllItems()//get all items in the cart
//        Toast.makeText(applicationContext, "itemsincart", Toast.LENGTH_SHORT).show()
        itemsincart.forEachIndexed{ index, SHoe ->
            //how many items do you have
//                        Toast.makeText(applicationContext, "$index", Toast.LENGTH_SHORT).show()
//            index returns the item per count
            val shoe_id =SHoe.category_id
//            Toast.makeText(applicationContext, "$shoe_id", Toast.LENGTH_SHORT).show()
//            Toast.makeText(applicationContext, "$invoice_no", Toast.LENGTH_SHORT).show()

            val category_id =SHoe.shoe_id
//            retrieve all detail from a shared prefs
            val member_id = PrefsHelper.getPrefs(this ,"user_id")

//            val total_amount = PrefsHelper.getPrefs(this ,"total_amount")
            val latitude =PrefsHelper.getPrefs(this ,"latitude")
            val longitude =PrefsHelper.getPrefs(this ,"longitude")
//            Toast.makeText(applicationContext, "$latitude", Toast.LENGTH_SHORT).show()
//            Toast.makeText(applicationContext, "$longitude", Toast.LENGTH_SHORT).show()




//            access API helper and post your variables to api
            val api = Constants.BASE_URL +"/makeorder"
            val apiapihelper = ApiHelper(applicationContext)
            val helper = SQLiteCartHelper(applicationContext)

            //create a json object
            val body = JSONObject()
            body.put("user_id",member_id)
            body.put("total_amount",helper.totalcost())
            body.put("latitude",latitude)
            body.put("longitude",longitude)
            body.put("invoice_no",invoice_no)

            apiapihelper.post(api,body,object :ApiHelper.CallBack{
                override fun onSuccess(result: JSONArray?) {
                    Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(result: JSONObject?) {
                    Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(result: String?) {
                    Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()
                }

            })// end of post
//            index count from zero
//                    if we are posting to the api the labtest for making booking, we make sure all the labtest have been post to the api
            if(index == (itemsincart.size - 1)){
                startActivity(Intent(applicationContext, Payment::class.java))
                finish()
            }


        }//end of each
        progressBar = findViewById(R.id.progress)
        statusText = findViewById(R.id.status_text)

        simulateProgress()



    }
    private fun simulateProgress() {
        var progress = 0
        val maxProgress = 100

        Thread {
            while (progress < maxProgress) {
                progress += 1
                handler.post {
                    progressBar.progress = progress
                    statusText.text = "Processing... $progress%"
                }
                Thread.sleep(100)
            }
            handler.post {
                statusText.text = "Complete!"
            }
        }.start()
    }

}