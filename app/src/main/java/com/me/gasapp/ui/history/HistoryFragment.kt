package com.me.gasapp.ui.history

import android.os.Bundle
//import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
//import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.google.android.material.navigation.NavigationView
import com.me.gasapp.DataList
import com.me.gasapp.MainActivity
import com.me.gasapp.R
import com.me.gasapp.Toggles
//import kotlin.reflect.typeOf


class HistoryFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
//    private lateinit var navView: NavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataEntries: DataList
    //  0: id
    //  1: date (epoch)
    //  2: Dist
    //  3: Gas
    private var sortOption: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        setHasOptionsMenu(true)

        mainActivity = (activity as MainActivity)
//        navView = mainActivity.findViewById(R.id.nav_view)
//        navView.menu.findItem(R.id.nav_history).title = "History - " + Toggles.values()[sortOption].name

        dataEntries = mainActivity.dataEntries
        sortDataEntries()

        recyclerView = root.findViewById(R.id.recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RecyclerAdapter(requireActivity(), dataEntries)
        recyclerView.itemAnimator = DefaultItemAnimator()

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_toggle -> {
                toggleSort()
//                navView.menu.findItem(R.id.nav_history).title = "History - " + Toggles.values()[sortOption].name
//                Log.d("DataEntries", "Pre Sort $sortOption")
//                dataEntries.forEach{
//                    Log.d("DataEntries", "\t" + it[sortOption].toString())
//                }
                sortDataEntries()
//                Log.d("DataEntries", "Post Sort $sortOption")
//                dataEntries.forEach{
//                    Log.d("DataEntries", "\t" + it[sortOption].toString())
//                }
                recyclerView.adapter!!.notifyDataSetChanged()
                true
            }
//            R.id.help -> {
//                showHelp()
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleSort() {
        sortOption = (sortOption + 1).rem(Toggles.values().size)
    }

    private fun sortDataEntries(){

        val sortedEntries: DataList = dataEntries.sortedBy{entry ->
            entry[sortOption].toDouble()
        } as DataList

        dataEntries.clear()
        dataEntries.addAll(sortedEntries)

    }

    /*
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.your_menu_xml, menu)
        super.onCreateOptionsMenu(menu!!, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

     */
}
