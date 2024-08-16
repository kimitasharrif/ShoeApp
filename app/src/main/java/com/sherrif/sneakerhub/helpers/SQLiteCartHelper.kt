package com.sherrif.sneakerhub.helpers

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.sherrif.sneakerhub.MainActivity
import com.sherrif.sneakerhub.MyCart
import com.sherrif.sneakerhub.models.Shoe


//our class name is called SQLiteCartHelper and its going to accept 1 parameter called context
class SQLiteCartHelper(context : Context) : SQLiteOpenHelper(context, "cart3.db",null,1) {
    //    make context visible to other functions
    val context = context
    override fun onCreate(p0: SQLiteDatabase?) {
        // create a table if it does not exist
        val createtable = """
    CREATE TABLE IF NOT EXISTS cart(
        shoe_id INTEGER PRIMARY KEY AUTOINCREMENT,
        category_id INTEGER,
        name VARCHAR,
        price INTEGER,
        description TEXT,
        brand VARCHAR,
        quantity INTEGER
        
        
    )
""".trimIndent()

        p0?.execSQL(createtable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS cart")
        onCreate(p0)
    }//end of on upgrade



    //check the number of item saved in our table
    fun getNumberOfItems(): Int {
//        get permission to read the db
        val p0 = this.readableDatabase
        val result: Cursor = p0.rawQuery("select* from cart", null)
        //return result count
        return result.count

    }//end of get all number of items
    //function to clear cart
    fun clearcart(){
        // get permission to write the database
        val p0 =this.writableDatabase
        p0.delete("cart",null,null)
        println("cart cleared")
        Toast.makeText(context, "cart cleared", Toast.LENGTH_SHORT).show()
        //reload
        val intent =Intent(context,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }// end of clear cart

    // function to retrieve all record in cart
    //loop them in arraylist
    fun getAllItems():ArrayList<Shoe> {
        // get permission to write database
        val p0 = this.writableDatabase
        val items = ArrayList<Shoe>()
        //create a cursor for our results
        val result: Cursor = p0.rawQuery("select* from cart", null)
        // lets add all the rows into the items in our arraylist
        while (result.moveToNext()) {
//            access the lab test model
            val model = Shoe()
            model.shoe_id = result.getString(0)//assume one row
            model.category_id = result.getString(1)
            model.name = result.getString(2)
            model.price = result.getString(3)
            model.description = result.getString(4)
            model.brand = result.getString(5)
            model.quantity = result.getString(6)



            //add our model to arraylist
            items.add(model)
        }///end of while loop
        return items


    }// end of get all items

        // delete records by id..... this deletes one record at a time

        //fun to get the total cost of cart items
        fun totalcost(): Double {
//        permission to read database
            val p0 = this.readableDatabase
            val result: Cursor = p0.rawQuery("select price from cart", null)

//    create a variable called total and asign it 0.0
            var total: Double = 0.0
            while (result.moveToNext()) {
//        the cursor results return a list of test _cost
//        we loop through as we add the totals

                total += result.getDouble(0)
//        total = total + result.getDouble(0)


            }//end of while loop

//        Toast.makeText(context, "The total cost is $total", Toast.LENGTH_SHORT).show()
//    return the updated total

            return total
        }
//function to remove one item

    fun clearCartById(shoe_id: String) {
        //write permission
        val p0 = this.writableDatabase

        //provide your testing id when deleting
        p0.delete("cart", "shoe_id=?", arrayOf(shoe_id))
        println("item Id $shoe_id Removed")
        Toast.makeText(context, "item Id $shoe_id Removed", Toast.LENGTH_SHORT).show()
        // reload
        var intent = Intent(context, MyCart::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)


    }// end of clear cart by id
    //    insert/ Save to cart table

    fun insertData(shoe_id: String, category_id: String, name: String, price: String, description: String, brand: String,
                   quantity:String) {
        //ask permission to write our db
        val p0 = this.writableDatabase
//    select before insert to see if ID already exists
        val values = ContentValues()
        values.put("shoe_id", shoe_id)
        values.put("category_id", category_id)
        values.put("name", name)
        values.put("price", price)
        values.put("description", description)
        values.put("brand", brand)
        values.put("quantity", quantity)



//    save/ inset to cart table
        val result: Long = p0.insert("cart", null, values)
        if (result < 1) {
            println("Failed to Add")
            Toast.makeText(context, "Item Already in Cart", Toast.LENGTH_SHORT).show()
        } else {
            println("Item Added to Cart")
            Toast.makeText(context, "Item Added to Cart", Toast.LENGTH_SHORT).show()
        }//end of else statement

    }
//end of insert data


}





