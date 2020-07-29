package com.me.gasapp.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.me.gasapp.DataList
import com.me.gasapp.MainActivity
import com.me.gasapp.R
//import com.me.gasapp.SharedViewModel

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var recyclerView: RecyclerView
//    private lateinit var model: SharedViewModel
    private lateinit var dataEntries: DataList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        model =
//            ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_history, container, false)

        //Copy entries to local mutablelist to update recyclerview
//        model.data.observe(viewLifecycleOwner, Observer {
//            copyDataEntries(it)
//            Log.d("SVM", "Observed Change")
//        })
//        Log.d("HistFrag", (activity as MainActivity).dataEntries.toString())
        dataEntries = (activity as MainActivity).dataEntries


        recyclerView = root.findViewById(R.id.recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RecyclerAdapter(activity!!, dataEntries, recyclerView)
        recyclerView.itemAnimator = DefaultItemAnimator()

        return root
    }

    private fun copyDataEntries(newData: DataList) {
        dataEntries.clear()
        newData.forEach { dataEntries.add(it) }
    }
}
