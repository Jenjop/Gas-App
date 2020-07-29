package com.me.gasapp.ui.history
//package com.google.firebase.recyclerview;

import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.me.gasapp.R


class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val layout: LinearLayout = itemView.findViewById(R.id.item_layout)
    val view0: TextView = itemView.findViewById(R.id.item_text0)
    val view1: TextView = itemView.findViewById(R.id.item_text1)
    val view2: TextView = itemView.findViewById(R.id.item_text2)
    val actions: LinearLayout = itemView.findViewById(R.id.item_actions)
    val delete_button: ImageButton = itemView.findViewById(R.id.delete_button)
}