package com.me.gasapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.me.gasapp.R

//Use for stats like avg mpg or smth
class HomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //        val textView: TextView = root.findViewById(R.id.text_home)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}
