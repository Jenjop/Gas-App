package com.me.gasapp.ui.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.android.material.datepicker.MaterialDatePicker
import com.me.gasapp.R

class EntryFragment : Fragment() {

    private var today: Long = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_entry, container, false)
        //https://hackernoon.com/how-to-use-new-material-date-picker-for-android-s7k32w0
        // https://ahsensaeed.com/android-material-design-library-date-picker-dialog/
//        val builder = MaterialDatePicker.Builder.datePicker()
//        val picker = builder.build()
//        picker.show(childFragmentManager, picker.toString())

        //https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/datepicker/DatePickerMainDemoFragment.java
        val dateButton: Button = root.findViewById(R.id.input_date)
        dateButton.setOnClickListener {view->
//            today = MaterialDatePicker.todayInUtcMilliseconds()
//            val builder = MaterialDatePicker.Builder.datePicker()
//            val picker = builder.build()
//            picker.show(childFragmentManager, picker.toString())
        }

        return root
    }
}
