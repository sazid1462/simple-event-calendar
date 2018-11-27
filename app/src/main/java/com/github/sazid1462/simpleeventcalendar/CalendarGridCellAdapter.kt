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
import kotlin.collections.ArrayList

class CalendarGridCellAdapter (private val context: Context, private val dateList: ArrayList<Pair<Int, Boolean>>) : BaseAdapter() {

    // First, let's obtain an instance of GregorianCalendar.
    private var cal = GregorianCalendar.getInstance()

    override fun getCount(): Int {
        return NO_OF_DAYS
    }

    override fun getItem(position: Int): Any? {
        return mDaysOfWeek[(cal.firstDayOfWeek + position - 1) % NO_OF_DAYS]
    }

    override fun getItemId(position: Int): Long {
        return ((cal.firstDayOfWeek + position - 1) % NO_OF_DAYS).toLong()
    }

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val cellView: View
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            cellView = inflater.inflate(R.layout.date_cell, parent, false)
            cellView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 185)

            val textViewDay = cellView.findViewById(R.id.day_cell_text) as TextView
            textViewDay.text = mDaysOfWeek[(cal.firstDayOfWeek + position - 1) % NO_OF_DAYS]

            val textViewDate = cellView.findViewById(R.id.date_cell_text) as TextView
            textViewDate.text = dateList[position].first.toString()

            if (dateList[position].second) {
                textViewDate.setTextColor(context.getColor(R.color.white))
                textViewDate.background = context.getDrawable(R.drawable.circular_selection)
            } else {
                textViewDate.background = context.getDrawable(R.drawable.circular_background)
            }

            cellView.setPadding(1, 1, 1, 1)
        } else {
            cellView = convertView
        }

        return cellView
    }

}


