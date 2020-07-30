package com.me.gasapp.ui.history

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.me.gasapp.DBManager
import com.me.gasapp.DataList
import com.me.gasapp.R
//import com.me.gasapp.SharedViewModel
import java.time.ZoneId
import java.util.*


class RecyclerAdapter(
    private var activity: FragmentActivity,
    private var dataEntries: DataList,
    private var rv: RecyclerView
) :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION
//    private lateinit var model: SharedViewModel
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
//        model =
//            ViewModelProviders.of(activity).get(SharedViewModel::class.java)

//        if (dataEntries.isNotEmpty()) {
        //Setting text
        holder.view0.text = java.time.format.DateTimeFormatter.ofPattern("E, d MMM yyyy kk:mm:ss")
            .withLocale(Locale.US).withZone(ZoneId.of("PST"))
            .format(java.time.Instant.ofEpochSecond(dataEntries[position][1].toLong()))
        holder.view1.text = "Dist: " + dataEntries[position][2].toString()
        holder.view2.text = "Gas: " + dataEntries[position][3].toString()

        holder.itemView.isSelected = selectedPosition == position
        holder.actions.visibility = View.GONE
        //For multi selected item
        if (multiSelect){
            if(selectedSet.contains(position)){
                holder.itemView.setBackgroundColor(Color.parseColor("#5462CEFF"))
            }
            else{
                holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            }
        }
        //For single selected item
        else {
            if (selectedPosition == position) {
                holder.itemView.setBackgroundColor(Color.parseColor("#5462CEFF"))
                holder.actions.visibility = View.VISIBLE
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT)
//                holder.actions.visibility = View.GONE
            }
        }

        //Listener for long click -> Turns on multiselect on long click
        holder.itemView.setOnLongClickListener{
            if (!multiSelect){
                multiSelect = true
                Log.d("RecyclerView", "MultiSelect Toggled On")
                //Returns false from lambda so onClickListener is called and item is selected
                return@setOnLongClickListener false
            }
            // Returns true from lambda so event is consumed and normal onClickListener is not called
            return@setOnLongClickListener true
        }

        //Listner for short click
        holder.itemView.setOnClickListener {
            //Multi Select -> toggles selection, turns off multiselect when last item is unselected
            if (multiSelect){
                //Toggle selection
                if (selectedSet.contains(position)) {
                    Log.d("RecyclerView", "Position $position removed from MultiSelect")
                    selectedSet.remove(position)
                    //If last item, disable multi select
                    if (selectedSet.isEmpty()) {
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
                selectedPosition = position
                //                TransitionManager.beginDelayedTransition(holder.layout) //Animate the item_actions, not currently working, can animate entire recyclerview if passed in tho :/
                notifyDataSetChanged()
            }

        }

        //Listner for deleting entry
        holder.delete_button.setOnClickListener {
            //DB
            dbManager = DBManager(activity.applicationContext)
            dbManager.open();

            dbManager.delete(dataEntries[position][0] as Long)
            dataEntries.removeAt(selectedPosition)
//            model.data.postValue(dataEntries)
            notifyDataSetChanged()
            selectedPosition = RecyclerView.NO_POSITION

            dbManager.close()
        }

//        }
//        Log.d("Adapter", "Update Text")

    }

    override fun getItemCount(): Int {
        return dataEntries.size
    }
}


