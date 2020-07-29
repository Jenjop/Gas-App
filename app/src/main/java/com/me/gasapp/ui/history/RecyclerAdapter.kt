package com.me.gasapp.ui.history

import android.annotation.SuppressLint
import android.graphics.Color
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.me.gasapp.DataList
import com.me.gasapp.R
import java.time.ZoneId
import java.util.*


class RecyclerAdapter(
    private var dataEntries: DataList
) :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun getItemViewType(position: Int): Int {
        return R.layout.recycler_item_layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
//        val view: View = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_layout, null)
        return RecyclerViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        if (dataEntries.isNotEmpty()) {
            holder.view0.text = java.time.format.DateTimeFormatter.ofPattern("E, d MMM yyyy kk:mm:ss")
//            holder.view0.text = "Date: " + java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
                .withLocale(Locale.US).withZone(ZoneId.of("PST"))
                .format(java.time.Instant.ofEpochSecond(dataEntries[position][0].toLong()))
            holder.view1.text = "Dist: " + dataEntries[position][1].toString()
            holder.view2.text = "Gas: " + dataEntries[position][2].toString()

            holder.itemView.isSelected = selectedPosition == position
            if (selectedPosition == position) {
                holder.itemView.setBackgroundColor(Color.parseColor("#5462CEFF"))
                holder.actions.visibility = View.VISIBLE
            }
            else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT)
                holder.actions.visibility = View.GONE
            }
//            holder.itemView.setBackgroundColor(if (selectedPosition == position) Color.parseColor("#5462CEFF") else Color.TRANSPARENT)
//            holder.actions.visibility = if (selectedPosition == position) View.VISIBLE else View.GONE

            holder.itemView.setOnClickListener(View.OnClickListener {
//                if (position == RecyclerView.NO_POSITION)
//                    return@OnClickListener
//                notifyItemChanged(selectedPosition)
//                selectedPosition = position
//                notifyItemChanged(selectedPosition)

                selectedPosition = position
//                TransitionManager.beginDelayedTransition() #Need to pass in viewgroup of recycler view
                notifyDataSetChanged()
                Log.d("RecyclerView", "Selected: $selectedPosition")
            })
        }
//        Log.d("Adapter", "Update Text")
//        holder.txtViewTitle.setText(dataEntries[position)

    }

    override fun getItemCount(): Int {
        return dataEntries.size
    }
}


