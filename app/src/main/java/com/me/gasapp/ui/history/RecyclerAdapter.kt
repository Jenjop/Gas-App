package com.me.gasapp.ui.history

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.me.gasapp.DBManager
import com.me.gasapp.DataList
import com.me.gasapp.R
import java.time.ZoneId
import java.util.*


class RecyclerAdapter(
    private var activity: FragmentActivity,
    private var dataEntries: DataList
) :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION
    private lateinit var dbManager: DBManager

    private var multiSelect: Boolean = false
    private var selectedSet: MutableSet<Int> = mutableSetOf()

    private var _id: Long = -1
    private var dateVal: Long = 0
    private var distVal: Double = 0.0
    private var gasVal: Double = 0.0

    override fun getItemViewType(position: Int): Int {
        return R.layout.recycler_item_layout
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_layout, null)
        return RecyclerViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    private fun displayText(holder: RecyclerViewHolder, position: Int) {
        //Setting text
        holder.view0.text = epochConvert(dataEntries[position][1].toLong())
        holder.view1.text = "Dist: " + dataEntries[position][2].toString()
        holder.view2.text = "Gas: " + dataEntries[position][3].toString()
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        displayText(holder, position)
        holder.itemView.isSelected = selectedPosition == position
        holder.actions.visibility = View.GONE

        //Entry selection
        if (selectedPosition == position || selectedSet.contains(position)) {
            holder.itemView.setBackgroundColor(Color.parseColor("#5462CEFF"))

            if (!multiSelect)
                holder.actions.visibility = View.VISIBLE
        } else
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)


        //Listener for long click -> Turns on multiselect on long click
        //Lambda return boolean true -> Event consumed and normal onClickListener is not called
        holder.itemView.setOnLongClickListener {
            if (!multiSelect) {
                multiSelect = true
                Log.d("RecyclerView", "MultiSelect Toggled On")
                return@setOnLongClickListener false
            }
            return@setOnLongClickListener true
        }

        //Listener for short click
        holder.itemView.setOnClickListener {
            //MultiSelect -> toggles selection, turns off MultiSelect when last item is unselected
            if (multiSelect) {
                if (selectedSet.contains(position)) {
                    Log.d("RecyclerView", "Position $position removed from MultiSelect")
                    selectedSet.remove(position)

                    if (selectedSet.isEmpty()) { //If last item, disable MultiSelect
                        Log.d("RecyclerView", "MultiSelect Toggled Off")
                        multiSelect = false
                        selectedPosition = RecyclerView.NO_POSITION
                    }
                    notifyDataSetChanged()
                } else {
                    Log.d("RecyclerView", "Position $position added to MultiSelect")
                    selectedSet.add(position)
                    notifyDataSetChanged()
                }
            }
            //Single Select
            else {
                Log.d("RecyclerView", "Position $position selected")
//                TransitionManager.beginDelayedTransition(holder.layout) //Animate the item_actions, not currently working, can animate entire recyclerview if passed in tho :/
                selectedPosition = position
                notifyDataSetChanged()
            }
        }

        //Listener for deleting entry
        holder.deleteButton.setOnClickListener {
            //DB
            dbManager = DBManager(activity.applicationContext)
            dbManager.open()

            dbManager.delete(dataEntries[position][0] as Long)
            dataEntries.removeAt(selectedPosition)
            notifyDataSetChanged()
            selectedPosition = RecyclerView.NO_POSITION

            dbManager.close()
        }


        //Listener for editing entry
        holder.editButton.setOnClickListener {
            Log.d("editButton", "Creating Dialog")
            activity.let {
                val builder = AlertDialog.Builder(it)
                val inflater = activity.layoutInflater
                val view = inflater.inflate(R.layout.dialog_edit, null)

                val distText: EditText = view.findViewById(R.id.edit_dialog_dist)
                val gasText: EditText = view.findViewById(R.id.edit_dialog_gas)
                val dateButton: Button = view.findViewById(R.id.edit_dialog_date)

                dateVal = dataEntries[selectedPosition][1] as Long //If date not modified
                //Edit field listeners
                dateButton.setOnClickListener {
                    Log.d("editDialog", "Date Change")
                    val today = MaterialDatePicker.todayInUtcMilliseconds()
                    val dateBuilder = MaterialDatePicker.Builder.datePicker()
                    dateBuilder.setSelection(today)
                    val picker = dateBuilder.build()
                    picker.show(activity.supportFragmentManager, picker.toString())
                    picker.addOnPositiveButtonClickListener { selection ->
                        Log.d("DatePicker", selection.toString())
                        dateVal = selection / 1000
                    }
                }


                builder.setView(view)
                    .setPositiveButton(
                        "Yes"
                    ) { _, _ ->
                        Snackbar.make(view, "Entry Changed", Snackbar.LENGTH_LONG).show()

                        //Set dist/gas val for retrieval
                        if (distText.text.isBlank())
                            distText.append(dataEntries[selectedPosition][2].toString()) //If dist not modified
                        if (gasText.text.isBlank())
                            gasText.append(dataEntries[selectedPosition][3].toString()) //If gas not modified
                        distVal = distText.text.toString().toDouble()
                        gasVal = gasText.text.toString().toDouble()

                        _id = dataEntries[selectedPosition][0] as Long
//                        Log.d(
//                            "Edit New",
//                            "ID: $_id Date: $dateVal, Dist: $distVal, Gas: $gasVal"
//                        )

                        dataEntries.removeAt(selectedPosition)
                        dataEntries.add(
                            arrayOf(
                                _id,
                                dateVal,
                                distVal,
                                gasVal
                            )
                        )
                        notifyDataSetChanged()
                        selectedPosition = RecyclerView.NO_POSITION

                        //DB
                        dbManager = DBManager(activity.applicationContext)
                        dbManager.open()

                        dbManager.update(_id, dateVal, distVal, gasVal)

                        dbManager.close()
                    }
                    .setNegativeButton(
                        "No"
                    ) { _, _ -> }

                val dialog = builder.create()
                dialog.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataEntries.size
    }
}

fun epochConvert(epoch: Long): String {
    return java.time.format.DateTimeFormatter.ofPattern("E, d MMM yyyy kk:mm:ss")
        .withLocale(Locale.US).withZone(ZoneId.of("PST"))
        .format(java.time.Instant.ofEpochSecond(epoch))
}

