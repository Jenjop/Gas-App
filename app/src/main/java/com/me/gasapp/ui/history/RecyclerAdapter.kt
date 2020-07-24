package com.me.gasapp.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.me.gasapp.R


class RecyclerAdapter(
    private var dataEntries: MutableList<DoubleArray>
) :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return R.layout.recycler_item_layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
//        val view: View = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_layout, null)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.view.text = dataEntries[position][0].toString() + dataEntries[position][1].toString()
//        holder.txtViewTitle.setText(dataEntries[position)

    }

    override fun getItemCount(): Int {
        return dataEntries.size
    }
}


