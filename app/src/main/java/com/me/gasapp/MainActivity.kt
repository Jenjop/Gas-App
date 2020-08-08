package com.me.gasapp

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.*
import com.me.gasapp.ui.SharedViewModel
import com.me.gasapp.ui.entry.EntryFragment
import com.me.gasapp.ui.history.epochConvert

//https://developer.android.com/guide/navigation/navigation-ui
//Navigation drawer and stuff

typealias Data = Array<Number>
typealias DataList = MutableList<Data>
enum class Toggles{
    ID,
    DATE,
    DIST,
    GAS
}
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var model: SharedViewModel

    var dataEntries: DataList = mutableListOf()
//    var sortOption: Int = 1

    private lateinit var dbManager: DBManager

    private var _id: Long = -1

    //    private var id: Int = -1
    private var date_val: Long = 0
    private var dist_val: Double = 0.0
    private var gas_val: Double = 0.0

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        model =
            ViewModelProvider(this).get(SharedViewModel::class.java)

        //DB
        dbManager = DBManager(this)
        dbManager.open();
        //Retrieve entries
        val cursor: Cursor? = dbManager.fetch()
        //cursor
        //  0: id
        //  1: date (epoch)
        //  2: Dist
        //  3: Gas
        while (cursor != null && !cursor.isAfterLast) {
            _id = cursor.getLong(0)
            date_val = cursor.getLong(1)
            dist_val = cursor.getDouble(2)
            gas_val = cursor.getDouble(3)
            dataEntries.add(arrayOf(_id, date_val, dist_val, gas_val))
            Log.d("DB Entries", "$_id, $date_val, $dist_val, $gas_val")
            cursor.moveToNext()
        }


        val fab: FloatingActionButton = findViewById(R.id.fab)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_history, R.id.nav_entry
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //When changing fragment from nav drawer
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_entry) {
                fab.visibility = View.VISIBLE

                model.dt.observe(this, Observer {
                    date_val = it
//                    Log.d("Main - SVM", "Change in dt: $it")
                })
                model.dist.observe(this, Observer {
                    dist_val = it
//                    Log.d("Main - SVM", "Change in dist: $it")
                })
                model.gas.observe(this, Observer {
                    gas_val = it
//                    Log.d("Main - SVM", "Change in gas: $it")
                })

                //Fab clicked on the entry page
                fab.setOnClickListener { view ->
                    Snackbar.make(view, "Entry Added", Snackbar.LENGTH_LONG).show()

                    _id++

                    Log.d(
                        "FAB",
                        "Date: " + date_val.toString() + ", Dist: " + dist_val.toString() + ", Gas: " + gas_val.toString()
                    )
                    //Add entry to local mutableList
                    dataEntries.add(
                        arrayOf(
                            _id,
                            date_val,
                            dist_val,
                            gas_val
                        )
                    )
                    //DB

                    dbManager.insert(date_val, dist_val, gas_val)

                }
            } else {
                fab.visibility = View.GONE
                fab.setOnClickListener() { view ->
                    Snackbar.make(view, "Changing View", Snackbar.LENGTH_LONG).show()

                    //TODO:
                    //Clicking fab moves to entry fragment
                }
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

    //Called when the hamburger icon is clicked, not when selecting a fragment
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }




}
