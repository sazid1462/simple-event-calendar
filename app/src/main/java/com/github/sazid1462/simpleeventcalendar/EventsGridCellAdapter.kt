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

class EventsGridCellAdapter (private val context: Context) : BaseAdapter() {

    // First, let's obtain an instance of GregorianCalendar.
    private var cal: GregorianCalendar = GregorianCalendar()

    override fun getCount(): Int {
        // The isLeapYear(int year) method will return true for leap
        // year and otherwise return false. In this example the message
        // will be printed as 2016 is a leap year.
        Log.d("CalendarGridCellAdapter", "Month ${mMonths[cal.get(Calendar.MONTH)]} Year ${cal.get(Calendar.YEAR)}")
//        if (month==1 && cal.isLeapYear(year)) {
//            System.out.println("The year $year is a leap year!")
//            return 29 + 7
//        }
        return 7
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


