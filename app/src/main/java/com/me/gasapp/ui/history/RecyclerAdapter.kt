package com.me.gasapp.ui.history

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.me.gasapp.DBManager
import com.me.gasapp.DataList
import com.me.gasapp.R
//import com.me.gasapp.SharedViewModel
import java.time.ZoneId
import java.util.*


class RecyclerAdapter(
    private var activity: FragmentActivity,
    private var dataEntries: DataList,
    private var rv: RecyclerView
) :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION
//    private lateinit var model: SharedViewModel
    private lateinit var dbManager: DBManager

    override fun getItemViewType(position: Int): Int {
        return R.layout.recycler_item_layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
//        val view: View = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_layout, null)
        return RecyclerViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
//        model =
//            ViewModelProviders.of(activity).get(SharedViewModel::class.java)

//        if (dataEntries.isNotEmpty()) {
        //Setting text
        holder.view0.text = java.time.format.DateTimeFormatter.ofPattern("E, d MMM yyyy kk:mm:ss")
            .withLocale(Locale.US).withZone(ZoneId.of("PST"))
            .format(java.time.Instant.ofEpochSecond(dataEntries[position][1].toLong()))
        holder.view1.text = "Dist: " + dataEntries[position][2].toString()
        holder.view2.text = "Gas: " + dataEntries[position][3].toString()

        //For selected Item
        holder.itemView.isSelected = selectedPosition == position
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#5462CEFF"))
            holder.actions.visibility = View.VISIBLE
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            holder.actions.visibility = View.GONE
        }

        //Listner for selecting entry
        holder.itemView.setOnClickListener {
            selectedPosition = position
    //                TransitionManager.beginDelayedTransition(holder.layout) //Animate the item_actions, not currently working, can animate entire recyclerview if passed in tho :/
            notifyDataSetChanged()
            Log.d("RecyclerView", "Selected: $selectedPosition")

        }

        //Listner for deleting entry
        holder.delete_button.setOnClickListener {
            //DB
            dbManager = DBManager(activity.applicationContext)
            dbManager.open();

            dbManager.delete(dataEntries[position][0] as Long)
            dataEntries.removeAt(selectedPosition)
//            model.data.postValue(dataEntries)
            notifyDataSetChanged()
            Log.d("RecyclerView", "Deleted: " + dataEntries[position][0].toString())

            dbManager.close()
        }

//        }
//        Log.d("Adapter", "Update Text")

    }

    override fun getItemCount(): Int {
        return dataEntries.size
    }
}


