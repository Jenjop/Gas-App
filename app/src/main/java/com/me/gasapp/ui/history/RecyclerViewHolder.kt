package com.me.gasapp.ui.history
//package com.google.firebase.recyclerview;

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.me.gasapp.R


class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val view0: TextView = itemView.findViewById(R.id.item_text0)
    val view1: TextView = itemView.findViewById(R.id.item_text1)
    val view2: TextView = itemView.findViewById(R.id.item_text2)
}