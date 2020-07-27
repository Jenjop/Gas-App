package com.me.gasapp.ui.history

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter

//https://coderwall.com/p/fmavhg/android-cursoradapter-with-custom-layout-and-how-to-use-it
class RecyclerCursorAdapter(context: Context, cursor: Cursor, flags: Int): CursorAdapter(context , cursor, flags){
    init{
//        val cursorInflater = (LayoutInflater) context.getSystemService(Context.Layout)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor){

    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View? {
        return null
    }
}