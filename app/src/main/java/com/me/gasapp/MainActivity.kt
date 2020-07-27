package com.me.gasapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.*

//https://developer.android.com/guide/navigation/navigation-ui
//Navigation drawer and stuff

typealias Data = DoubleArray
typealias DataList = MutableList<Data>

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    //Store entries [ [dist, gas], [dist, gas], ... ]
    var dataEntries: DataList = mutableListOf()
    private lateinit var model: SharedViewModel

    private lateinit var dbManager: DBManager

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //DB
        dbManager = DBManager(this)
        dbManager.open();

        //Get Shared View Model
        model =
            ViewModelProviders.of(this).get(SharedViewModel::class.java)

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
                fab.setOnClickListener { view ->
                    Snackbar.make(view, "Entry Added", Snackbar.LENGTH_LONG).show()

                    val distance: EditText = findViewById(R.id.input_distance)
                    val gas: EditText = findViewById(R.id.input_gas)
                    Log.d(
                        "FAB",
                        "Dist: " + distance.text.toString() + ", Gas: " + gas.text.toString()
                    )
                    val dist_val = distance.text.toString().toDouble()
                    val gas_val = gas.text.toString().toDouble()
                    dataEntries.add(
                        doubleArrayOf(
                            dist_val,
                            gas_val
                        )
                    )
                    model.data.postValue(dataEntries)
                    //DB
                    val timestamp = System.currentTimeMillis() / 1000
//                    val timestamp = Calendar.getInstance().getTime() / 1000
                    dbManager.insert(timestamp, dist_val, gas_val)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    //Called when the hamburger icon is clicked, not when selecting a fragment
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}
