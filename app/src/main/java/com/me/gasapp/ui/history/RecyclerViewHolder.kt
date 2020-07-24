package com.me.gasapp.ui.history
//package com.google.firebase.recyclerview;

import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.me.gasapp.R


class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val view: TextView = itemView.findViewById(R.id.recycler)
}
