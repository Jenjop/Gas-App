package com.me.gasapp.ui.entry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.datepicker.MaterialDatePicker
import com.me.gasapp.R
import com.me.gasapp.ui.SharedViewModel
import com.me.gasapp.ui.history.epochConvert

class EntryFragment : Fragment() {

    private var today: Long = 0
    private lateinit var model: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        model = ViewModelProviders.of(this).get(SharedViewModel::class.java)
//        model = ViewModelProvider(this).get(SharedViewModel::class.java)
//        Log.d("Entry", model.toString())
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        Log.d("Entry", model.toString())
//        Log.d("this", "$this")
        Log.d("ReqAct", requireActivity().toString())
        val root = inflater.inflate(R.layout.fragment_entry, container, false)
        //https://hackernoon.com/how-to-use-new-material-date-picker-for-android-s7k32w0
        // https://ahsensaeed.com/android-material-design-library-date-picker-dialog/
//        val builder = MaterialDatePicker.Builder.datePicker()
//        val picker = builder.build()
//        picker.show(childFragmentManager, picker.toString())

        //https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/datepicker/DatePickerMainDemoFragment.java
        val distText: EditText = root.findViewById(R.id.input_distance)
        val gasText: EditText = root.findViewById(R.id.input_gas)
        val dateButton: Button = root.findViewById(R.id.input_date)

        model.dt.observe(viewLifecycleOwner, Observer{
            dateButton.text = epochConvert(it)
            Log.d("SVM", "Change in dt: $it")
        })
        model.dist.observe(viewLifecycleOwner, Observer{
            Log.d("SVM", "Change in dist: $it")
        })
        model.gas.observe(viewLifecycleOwner, Observer{
            Log.d("SVM", "Change in gas: $it")
        })

        dateButton.setOnClickListener { view ->
            today = MaterialDatePicker.todayInUtcMilliseconds()
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setSelection(today)
            val picker = builder.build()
            picker.show(childFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener { selection ->
                Log.d("DatePicker", selection.toString())
//                dateButton.text = epochConvert(selection)
                model.dt.postValue(selection)

            }

        }
        distText.addTextChangedListener{
            model.dist.postValue(it.toString().toDouble())
        }
        gasText.addTextChangedListener{
            model.gas.postValue(it.toString().toDouble())
        }

        return root
    }
}
