package com.me.gasapp.ui.history

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.me.gasapp.DBManager
import com.me.gasapp.DataList
import com.me.gasapp.R
import java.time.ZoneId
import java.util.*


class RecyclerAdapter(
    private var activity: FragmentActivity,
    private var dataEntries: DataList,
    private var rv: RecyclerView
) :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION
    private lateinit var dbManager: DBManager
    private var multiSelect: Boolean = false
    private var selectedSet: MutableSet<Int> = mutableSetOf()

    override fun getItemViewType(position: Int): Int {
        return R.layout.recycler_item_layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
//        val view: View = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_layout, null)
        return RecyclerViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        //Setting text
        holder.view0.text = epochConvert(dataEntries[position][1].toLong())
        holder.view1.text = "Dist: " + dataEntries[position][2].toString()
        holder.view2.text = "Gas: " + dataEntries[position][3].toString()

        holder.itemView.isSelected = selectedPosition == position
        holder.actions.visibility = View.GONE

        if (selectedPosition == position || selectedSet.contains(position)) {
            holder.itemView.setBackgroundColor(Color.parseColor("#5462CEFF"))

            if (!multiSelect)
                holder.actions.visibility = View.VISIBLE
        }
        else
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)


        //Listener for long click -> Turns on multiselect on long click
        //Lambda return boolean true -> Event consumed and normal onClickListener is not called
        holder.itemView.setOnLongClickListener{
            if (!multiSelect){
                multiSelect = true
                Log.d("RecyclerView", "MultiSelect Toggled On")
                return@setOnLongClickListener false
            }
            return@setOnLongClickListener true
        }

        //Listener for short click
        holder.itemView.setOnClickListener {
            //MultiSelect -> toggles selection, turns off MultiSelect when last item is unselected
            if (multiSelect){
                if (selectedSet.contains(position)) {
                    Log.d("RecyclerView", "Position $position removed from MultiSelect")
                    selectedSet.remove(position)

                    if (selectedSet.isEmpty()) { //If last item, disable MultiSelect
                        Log.d("RecyclerView", "MultiSelect Toggled Off")
                        multiSelect = false
                        selectedPosition = RecyclerView.NO_POSITION
                    }
                    notifyDataSetChanged()
                }
                else {
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
            dbManager.open();

            dbManager.delete(dataEntries[position][0] as Long)
            dataEntries.removeAt(selectedPosition)
            notifyDataSetChanged()
            selectedPosition = RecyclerView.NO_POSITION

            dbManager.close()
        }

        holder.editButton.setOnClickListener{
            //DB
//            dbManager = DBManager(activity.applicationContext)
//            dbManager.open();



            Log.d("editButton", "Creating Dialog")
            activity?.let{
                val builder = AlertDialog.Builder(it)
                val inflater = activity.layoutInflater

                val view = inflater.inflate(R.layout.dialog_edit, null)
                builder.setView(view)
                    .setPositiveButton("Yes",
                    DialogInterface.OnClickListener{ dialog, id ->
//                        Snackbar.make(view, "Entry Added", Snackbar.LENGTH_LONG).show()
//
//                        _id++
//
//                        Log.d(
//                            "FAB",
//                            "Date: " + date_val.toString() + ", Dist: " + dist_val.toString() + ", Gas: " + gas_val.toString()
//                        )
//                        //Add entry to local mutableList
//                        dataEntries.add(
//                            arrayOf(
//                                _id,
//                                date_val,
//                                dist_val,
//                                gas_val
//                            )
//                        )
//                        //DB
//
//                        dbManager.insert(date_val, dist_val, gas_val)

                    })
                    .setNegativeButton("No",
                    DialogInterface.OnClickListener{ dialog, id ->

                    })

                val distText: EditText = view.findViewById(R.id.edit_dialog_dist)
                val gasText: EditText = view.findViewById(R.id.edit_dialog_gas)
                val dateButton: Button = view.findViewById(R.id.edit_dialog_date)

//                distText.addTextChangedListener{
//                    model.dist.postValue(it.toString().toDouble())
//                }
//                gasText.addTextChangedListener{
//                    model.gas.postValue(it.toString().toDouble())
//                }

                val dialog = builder.create()
                dialog.show()


//            val builder: AlertDialog.Builder? = activity.let {
//                AlertDialog.Builder(it)
//            }
//            builder?.setMessage(R.string.edit_dialog_message)
//            builder?.setTitle(R.string.edit_dialog_title)
//            val dialog: AlertDialog? =     builder?.create()
//            dialog?.show()
//            val dialog: AlertDialog = AlertDialog.Builder(activity).setTitle(R.string.edit_dialog_title).setMessage(R.string.edit_dialog_message).create()
//            dialog.show()
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

