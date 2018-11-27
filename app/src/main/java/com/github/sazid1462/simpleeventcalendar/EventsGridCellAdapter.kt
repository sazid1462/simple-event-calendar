package com.github.sazid1462.simpleeventcalendar

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.BaseAdapter
import java.util.*
import android.view.LayoutInflater
import android.widget.TextView

class EventsGridCellAdapter (private val context: Context, private val dateList: ArrayList<Pair<Int, Boolean>>) : BaseAdapter() {

    // First, let's obtain an instance of GregorianCalendar.
    private var cal: GregorianCalendar = GregorianCalendar()

    override fun getCount(): Int {
        return NO_OF_DAYS
    }

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val cellView: View
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            cellView = inflater.inflate(R.layout.events_cell, parent, false)
            cellView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 85)
            val textViewEvent = cellView.findViewById(R.id.event_cell_text) as TextView
            textViewEvent.text = (7 * cal.get(GregorianCalendar.WEEK_OF_MONTH) + position+1).toString()

            cellView.setPadding(1, 1, 1, 1)
        } else {
            cellView = convertView
        }

        return cellView
    }

}


