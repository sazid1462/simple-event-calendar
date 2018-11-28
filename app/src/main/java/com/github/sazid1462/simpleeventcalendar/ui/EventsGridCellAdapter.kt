package com.github.sazid1462.simpleeventcalendar.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.BaseAdapter
import java.util.*
import android.view.LayoutInflater
import android.widget.TextView
import com.github.sazid1462.simpleeventcalendar.R

class EventsGridCellAdapter (private val context: Context, private val dateList: ArrayList<Pair<Int, Boolean>>) : BaseAdapter() {

    // First, let's obtain an instance of GregorianCalendar.
    private var cal = GregorianCalendar.getInstance()

    override fun getCount(): Int {
        return 10 * NO_OF_DAYS
    }

    override fun getItem(position: Int): Any? {
        return (position - position/NO_OF_DAYS).toString()
    }

    override fun getItemId(position: Int): Long {
        return (position - position/NO_OF_DAYS).toLong()
    }

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val cellView: View
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // if it's not recycled, initialize some attributes
            cellView = inflater.inflate(R.layout.events_cell, parent, false)
            cellView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 120)

            cellView.setPadding(1, 1, 1, 1)
        } else {
            cellView = convertView
        }

        val textViewEvent = cellView.findViewById(R.id.event_cell_text) as TextView
        textViewEvent.text = (position - position/NO_OF_DAYS).toString()
        cellView.background = context.getDrawable(R.drawable.rect_border)

        return cellView
    }

}


