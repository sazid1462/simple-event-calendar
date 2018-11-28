package com.github.sazid1462.simpleeventcalendar.ui

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
import kotlin.collections.ArrayList

class CalendarGridCellAdapter (private val context: Context, private var dateList: ArrayList<Pair<Int, Boolean>>) : BaseAdapter() {

    // First, let's obtain an instance of GregorianCalendar.
    private var cal = GregorianCalendar.getInstance()

    override fun getCount(): Int {
        return NO_OF_DAYS + 1
    }

    fun setDateList(dateList: ArrayList<Pair<Int, Boolean>>) {
        this.dateList.addAll(dateList)
        notifyDataSetInvalidated()
    }

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    // create a new ImageView for each item referenced by the Adapter
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val cellView: View
        if (position == 0) {
            if (convertView == null) {
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                cellView = inflater.inflate(R.layout.date_cell, parent, false)
                cellView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 185)
            } else {
                cellView = convertView
            }
            val textViewDate = cellView.findViewById(R.id.date_cell_text) as TextView
            textViewDate.text = "24H"
            return cellView
        }
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // if it's not recycled, initialize some attributes
            cellView = inflater.inflate(R.layout.date_cell, parent, false)
            cellView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 185)

            cellView.setPadding(1, 1, 1, 1)
        } else {
            cellView = convertView
        }

        val textViewDay = cellView.findViewById(R.id.day_cell_text) as TextView
        textViewDay.text = mDaysOfWeek[(NO_OF_DAYS + (cal.firstDayOfWeek + position - 2)) % NO_OF_DAYS]

        val textViewDate = cellView.findViewById(R.id.date_cell_text) as TextView
        textViewDate.text = dateList[position-1].first.toString()

        if (dateList[position-1].second) {
            textViewDate.setTextColor(context.getColor(R.color.white))
            textViewDate.background = context.getDrawable(R.drawable.circular_selection)
        } else {
            textViewDate.setTextColor(context.getColor(R.color.black))
            textViewDate.background = context.getDrawable(R.drawable.circular_background)
        }

        return cellView
    }

}


