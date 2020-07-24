package com.me.gasapp.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.me.gasapp.R


class RecyclerAdapter() :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return R.layout.recycler_item_layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.view.text = "AdapterPlaceholderText"
    }

    override fun getItemCount(): Int {
        return 100
    }
}

