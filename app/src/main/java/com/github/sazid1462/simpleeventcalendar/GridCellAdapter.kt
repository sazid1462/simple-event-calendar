package com.github.sazid1462.simpleeventcalendar

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.BaseAdapter
import android.widget.ImageView
import java.util.*
import androidx.core.content.ContextCompat.getSystemService
import android.view.LayoutInflater
import android.widget.TextView





// references to our images
private val mDays = arrayOf<Int>(
    31, 28, 31, 30, 31, 30,
    31, 31, 30, 31, 30, 31
)

private val mMonths = arrayOf(
    "January", "February", "March", "April",
    "May", "June", "July", "August", "September",
    "October", "November", "December"
)


class GridCellAdapter (private val context: Context, private val month: Int, private val year: Int) : BaseAdapter() {

    // First, let's obtain an instance of GregorianCalendar.
    private var cal: GregorianCalendar = GregorianCalendar()

    override fun getCount(): Int {
        // The isLeapYear(int year) method will return true for leap
        // year and otherwise return false. In this example the message
        // will be printed as 2016 is a leap year.
        Log.d("GridCellAdapter", "Month ${mMonths[month]} year $year")
        if (month==1 && cal.isLeapYear(year)) {
            System.out.println("The year $year is a leap year!")
            return 29
        }
        return mDays[month]
    }

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val dateCellView: View
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            dateCellView = inflater.inflate(R.layout.date_cell, parent, false)
            dateCellView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 85)
            val textViewAndroid = dateCellView.findViewById(R.id.android_gridview_text) as TextView
            textViewAndroid.text = (position+1).toString()
            dateCellView.setPadding(1, 1, 1, 1)
        } else {
            dateCellView = convertView
        }

        return dateCellView
    }

}


