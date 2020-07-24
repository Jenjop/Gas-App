package com.me.gasapp.ui.history
//package com.google.firebase.recyclerview;

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.me.gasapp.R


class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val view: TextView = itemView.findViewById(R.id.item_text)
}
//class ViewHolder(itemLayoutView: View) :
//    RecyclerView.ViewHolder(itemLayoutView) {
//    var txtViewTitle: TextView
//    private var imgViewIcon: ImageView
//
//    init {
//        txtViewTitle = itemLayoutView.findViewById<View>(R.id.item_title) as TextView
//        imgViewIcon = itemLayoutView.findViewById<View>(R.id.item_icon) as ImageView
//    }
//}