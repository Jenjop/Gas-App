package com.example.gas

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
//import android.database.Cursor

import android.provider.BaseColumns
import android.content.Context
import android.content.ContentValues
import android.view.LayoutInflater
import android.view.ViewGroup

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_data_display.*
import android.widget.TextView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

typealias DataPair = Pair<Int, Float>
typealias DisplayMap = MutableMap<Int, DataPair>


object GDataContract {
    //GasData
    // Table contents are grouped together in an anonymous object.
    object GDataEntry : BaseColumns {
        const val TABLE_NAME = "Entry"
        const val COLUMN_NAME_ONE = "Dist"
        const val COLUMN_NAME_TWO = "Gas"
    }
}

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${GDataContract.GDataEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${GDataContract.GDataEntry.COLUMN_NAME_ONE} INT," +
            "${GDataContract.GDataEntry.COLUMN_NAME_TWO} REAL)"
private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${GDataContract.GDataEntry.TABLE_NAME}"

class GDataDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "GData.db"
    }
}


class MyAdapter(private val myDataset: Array<String?>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_data_display, parent, false) as TextView
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = myDataset[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}



class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    val prefsFilename = "com.jenj.gas.prefs"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val prefs = this.getSharedPreferences(prefsFilename, 0)

        val dbHelper = GDataDbHelper(this)
        //Gets the data repository in write mode
        val db = dbHelper.writableDatabase
        // Define a projection that specifies which columns from the database you will actually use after this query.
        val projection = arrayOf(
            BaseColumns._ID,
            GDataContract.GDataEntry.COLUMN_NAME_ONE,
            GDataContract.GDataEntry.COLUMN_NAME_TWO
        )
        // Filter results WHERE "title" = 'My Title'
        val selection = "${GDataContract.GDataEntry.COLUMN_NAME_ONE} = ?"
        val selectionArgs = arrayOf("My Title")
        // How you want the results sorted in the resulting Cursor
        val sortOrder = "${BaseColumns._ID} ASC"


        //Add data
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Data Added", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            val dist: Int = distInput.text.toString().toInt()
            val gas: Float = gasInput.text.toString().toFloat()
            println("Added Dist: $dist, Gas: $gas")

            // Create a new map of values, where column names are the keys
            val values = ContentValues().apply {
                put(GDataContract.GDataEntry.COLUMN_NAME_ONE, dist)
                put(GDataContract.GDataEntry.COLUMN_NAME_TWO, gas)
            }
            // Insert the new row, returning the primary key value of the new row
            val newRowId = db?.insert(GDataContract.GDataEntry.TABLE_NAME, null, values)
        }

        //Print data to console
        printValues.setOnClickListener { view ->
            val cursor = db.query(
                GDataContract.GDataEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,//selection,              // The columns for the WHERE clause
                null,//selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
            )

            with(cursor) {
                while (moveToNext()) {
                    println("\tID: ${getLong(getColumnIndexOrThrow(BaseColumns._ID))}")
                    println("\t\tDist: ${getInt(getColumnIndex(GDataContract.GDataEntry.COLUMN_NAME_ONE))}")
                    println("\t\tGas: ${getFloat(getColumnIndex(GDataContract.GDataEntry.COLUMN_NAME_TWO))}")
                }
            }
            cursor.close()
        }

        fun createDisplayData(): DisplayMap {
            var displayDataMap: DisplayMap = mutableMapOf()
            val cursor = db.query(
                GDataContract.GDataEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,//selection,              // The columns for the WHERE clause
                null,//selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
            )



            with(cursor) {
                while (moveToNext()) {
                    val id: Long = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                    val dist: Int = getInt(getColumnIndex(GDataContract.GDataEntry.COLUMN_NAME_ONE))
                    val gas: Float =
                        getFloat(getColumnIndex(GDataContract.GDataEntry.COLUMN_NAME_TWO))
                    val curPair: DataPair = DataPair(dist, gas)
                    println("\tID: ${id}")
                    println("\t\tDist: ${dist}")
                    println("\t\tGas: ${gas}")
                    displayDataMap[id.toInt()] = curPair
                }
            }
            cursor.close()
            return displayDataMap
        }
        fun displayDataToArray(): Array<String?>{
            val displayData = createDisplayData()
            var retArray = arrayOfNulls<String?>(displayData.size)

            var i:Int = 0
            for( (k,v) in displayData){
                retArray[i++] = "ID: $k Dist: ${v.first} Gas: ${v.second}"
            }

            return retArray
        }

        /*
            <android.support.v7.widget.CardView
        xmlns:card_view="http://schemas.android.com/apk/res-auto"
        android:id="@+id/card_view"
        android:layout_gravity="center"
        android:layout_width="200dp"
        android:layout_height="200dp"
        card_view:cardCornerRadius="4dp">
         */

        //Change view to display
        pagesInputToDisplay.setOnClickListener { view ->
            //startActivity(Intent(this@MainActivity, DataDisplay::class.java))
            fab.hide()
            vf.setDisplayedChild(1)

            //val displayData = createDisplayData()
            val displayDataArray = displayDataToArray()

            viewManager = LinearLayoutManager(this)
            viewAdapter = MyAdapter(displayDataArray)

            recyclerView = findViewById<RecyclerView>(R.id.displayRecyclerView).apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)

                // use a linear layout manager
                layoutManager = viewManager

                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
            }

//            for ((k, v) in displayData) {
//                val cardView = CardView(this)
//
//                val textView = TextView(this)
//                textView.text = "ID: $k Dist: ${v.first} Gas: ${v.second}"
//                textView.textSize = 24.toFloat()
//
//                cardView.addView(textView)
//                displayLinearLayout.addView(cardView)
//            }

//            val textArray = arrayOf("One", "Two", "Three", "Four")
//            for (i in textArray.indices) {
//                val textView = TextView(this)
//                textView.text = textArray[i]
//                textView.textSize = 24.toFloat()
//                displayLinearLayout.addView(textView)
//            }

        }

        //Change view to input
        pagesDisplayToInput.setOnClickListener { view ->
            vf.setDisplayedChild(2)
            fab.show()
//            displayLinearLayout.removeAllViews()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


}
