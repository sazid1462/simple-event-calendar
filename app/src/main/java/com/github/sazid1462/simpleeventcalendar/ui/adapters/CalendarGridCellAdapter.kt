package com.github.sazid1462.simpleeventcalendar.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.BaseAdapter
import java.util.*
import android.view.LayoutInflater
import android.widget.TextView
import com.github.sazid1462.simpleeventcalendar.R
import com.github.sazid1462.simpleeventcalendar.ui.DateTimeObject
import com.github.sazid1462.simpleeventcalendar.ui.NO_OF_DAYS
import com.github.sazid1462.simpleeventcalendar.ui.mDaysOfWeek
import kotlin.collections.ArrayList

/**
 * The Adapter class fo the header of the calendar.
 */
class CalendarGridCellAdapter(private val context: Context, private var dateList: ArrayList<Pair<DateTimeObject, Boolean>>) :
    BaseAdapter() {

    // First, let's obtain an instance of GregorianCalendar.
    private var cal = GregorianCalendar.getInstance()

    /**
     * @returns total number of data for the grid.
     */
    override fun getCount(): Int {
        return NO_OF_DAYS
    }

    /**
     * Sets the new set of dates/week to display
     */
    fun setDateList(dateList: ArrayList<Pair<DateTimeObject, Boolean>>) {
        this.dateList = dateList
        notifyDataSetInvalidated()
    }

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val cellView: View
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // inflate the custo cell view
            cellView = inflater.inflate(R.layout.date_cell, parent, false)
            cellView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 185)

            cellView.setPadding(1, 1, 1, 1)
        } else {
            cellView = convertView
        }

        // Consider the first day of the week of the Gregorian Calendar and show the days of week accordingly
        val textViewDay = cellView.findViewById(R.id.day_cell_text) as TextView
        textViewDay.text = mDaysOfWeek[(NO_OF_DAYS + (cal.firstDayOfWeek + position - 1)) % NO_OF_DAYS]

        // Put day of month under appropriate day of week
        val textViewDate = cellView.findViewById(R.id.date_cell_text) as TextView
        textViewDate.text = dateList[position].first.day.toString()

        // Decorate the date representing today
        if (dateList[position].second) {
            textViewDate.setTextColor(context.getColor(R.color.white))
            textViewDate.background = context.getDrawable(R.drawable.circular_selection)
        } else {
            textViewDate.setTextColor(context.getColor(R.color.black))
            textViewDate.background = context.getDrawable(R.drawable.circular_background)
        }

        return cellView
    }

}


