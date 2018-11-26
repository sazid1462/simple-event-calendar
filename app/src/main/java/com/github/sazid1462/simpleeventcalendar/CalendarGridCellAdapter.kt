package com.github.sazid1462.simpleeventcalendar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.BaseAdapter
import java.util.*
import android.view.LayoutInflater
import android.widget.TextView
import kotlin.collections.ArrayList

class CalendarGridCellAdapter (private val context: Context) : BaseAdapter() {

    // First, let's obtain an instance of GregorianCalendar.
    private var cal: GregorianCalendar = GregorianCalendar()
    private val noOfDays: Int = 7
    private var dateList: ArrayList<Int> = ArrayList(7)

    init {
        // Subtracting 1 from week as the week starts from 1 but month starts from 0
        val todayIndex = cal.get(GregorianCalendar.DAY_OF_WEEK) - 1     // Index of the current Day Of Week
        val currentMonthIndex = cal.get(GregorianCalendar.MONTH)    // Index of the current Month

        var x = cal.get(GregorianCalendar.DAY_OF_MONTH)-todayIndex
        if (x < 1) {
            val prevMonth = (12 + currentMonthIndex - 1) % 12
            x = mDays[prevMonth] - x
        }
        for (i in 0..6) {
            if (x > mDays[currentMonthIndex]) x -= mDays[currentMonthIndex]
            Log.d("CalendarGridCellAdapter", "x $x current Month cap ${mDays[currentMonthIndex]}")
            dateList.add(x)
            x++
        }
    }

    override fun getCount(): Int {
        // The isLeapYear(int year) method will return true for leap
        // year and otherwise return false. In this example the message
        // will be printed as 2016 is a leap year.
        Log.d("CalendarGridCellAdapter", "Week ${cal.get(Calendar.WEEK_OF_YEAR)} Year ${cal.get(Calendar.YEAR)}")
//        if (week==1 && cal.isLeapYear(year)) {
//            System.out.println("The year $year is a leap year!")
//            return 29 + 7
//        }
        return noOfDays
    }

    override fun getItem(position: Int): Any? {
        return mDaysOfWeek[position % 7]
    }

    override fun getItemId(position: Int): Long {
        return (position % 7).toLong()
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
            textViewDay.text = mDaysOfWeek[(cal.firstDayOfWeek + position - 1) % noOfDays]

            val textViewDate = cellView.findViewById(R.id.date_cell_text) as TextView
            textViewDate.text = dateList[position].toString()

            if (dateList[position] == cal.get(GregorianCalendar.DAY_OF_MONTH)) {
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


