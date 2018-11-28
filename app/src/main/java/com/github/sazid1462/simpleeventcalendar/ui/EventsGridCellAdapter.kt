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
import com.github.sazid1462.simpleeventcalendar.database.Event

class EventsGridCellAdapter (private val context: Context, private var dateList: ArrayList<Pair<DateTimeObject, Boolean>>) : BaseAdapter() {

    // First, let's obtain an instance of GregorianCalendar.
    private var cal = GregorianCalendar.getInstance()
    private var mEvents: List<Event> = ArrayList()

    init {
        // TODO update mEvents
    }

    override fun getCount(): Int {
        return mEvents.size + NO_OF_DAYS
    }

    override fun getItem(position: Int): Any? {
        return mEvents[(position - position/NO_OF_DAYS)].eventTitle
    }

    override fun getItemId(position: Int): Long {
        return (position - position/NO_OF_DAYS).toLong()
    }

    fun setDateList(dateList: ArrayList<Pair<DateTimeObject, Boolean>>) {
        this.dateList = dateList
        // TODO update mEvents
        notifyDataSetInvalidated()
    }

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val cellView: View
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // if it's not recycled, initialize some attributes
            cellView = inflater.inflate(R.layout.events_cell, parent, false)
            cellView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 150)

            cellView.setPadding(1, 1, 1, 1)
        } else {
            cellView = convertView
        }
        val textViewEvent = cellView.findViewById(R.id.event_cell_text) as TextView
        if (position < NO_OF_DAYS) {
            textViewEvent.background = context.getDrawable(android.R.drawable.ic_input_add)
        } else {
            textViewEvent.text = (position - position / NO_OF_DAYS).toString()
            textViewEvent.background = null
//            cellView.background = context.getDrawable(R.drawable.rect_border)
        }
        return cellView
    }

}


