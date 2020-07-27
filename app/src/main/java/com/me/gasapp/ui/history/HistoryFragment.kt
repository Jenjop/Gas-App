package com.me.gasapp.ui.history

import android.R
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.me.gasapp.DBManager
import com.me.gasapp.DataList
import com.me.gasapp.DatabaseHelper
import com.me.gasapp.SharedViewModel


class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var model: SharedViewModel
    private var dataEntries: DataList = mutableListOf()


//    //DB
    private lateinit var dbManager: DBManager
    private lateinit var adapter: SimpleCursorAdapter
    val from = arrayOf(
        DatabaseHelper._ID,
        DatabaseHelper.DATE,
        DatabaseHelper.DIST,
        DatabaseHelper.GAS
    )
    val to = intArrayOf(R.id.id, R.id.title, R.id.desc)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        historyViewModel =
//                ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        model =
            ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_history, container, false)
//        val textView: TextView = root.findViewById(R.id.text_history)
//        historyViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//        val a = MainActivity

        model.data.observe(viewLifecycleOwner, Observer {
            copyDataEntries(it)
            Log.d("SVM", "Observed Change")
        })

        //DB
        dbManager = DBManager(context!!);
        dbManager.open()
        val cursor: Cursor? = dbManager.fetch();
        adapter = SimpleCursorAdapter(context, R.layout.recycler_item_layout, cursor, from, to, 0)


//        val data: MutableList<DoubleArray> = root.getData
        recyclerView = root.findViewById(R.id.recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
//        recyclerView.adapter = RecyclerAdapter(mutableListOf(doubleArrayOf(1.23, 4.56)))
        recyclerView.adapter = RecyclerAdapter(dataEntries)
        recyclerView.itemAnimator = DefaultItemAnimator()

        return root
    }

    fun copyDataEntries(newData: DataList) {
        dataEntries.clear()
        newData.forEach { dataEntries.add(it) }
    }
}
