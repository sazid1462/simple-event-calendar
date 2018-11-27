package com.github.sazid1462.simpleeventcalendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import java.util.*
import kotlin.math.abs



const val NO_OF_DAYS: Int = 7

// references to our images
val mDays = arrayOf<Int>(
    31, 28, 31, 30, 31, 30,
    31, 31, 30, 31, 30, 31
)

val mMonths = arrayOf(
    "January", "February", "March", "April",
    "May", "June", "July", "August", "September",
    "October", "November", "December"
)

val mDaysOfWeek = arrayOf(
    "SUN", "MON", "TUE",
    "WED", "THU", "FRI", "SAT"
)

val weekToMonth = arrayOf(
    4, 8, 13, 17, 22, 26, 30, 35, 39, 43, 48, 52
)

/**
 * A placeholder fragment containing a simple calendar view.
 */
class CalendarFragment: Fragment() {

    private var rootView: View? = null
    private val cal = GregorianCalendar.getInstance()
    private val TODAY_DAY = cal.get(GregorianCalendar.DAY_OF_MONTH)
    private val TODAY_MONTH = cal.get(GregorianCalendar.MONTH)
    private val TODAY_YEAR = cal.get(GregorianCalendar.YEAR)
//    private var dayDistance = 0
    private var dateList: ArrayList<Pair<Int, Boolean>> = ArrayList(7)
    private var monthViewerText: String = "${mMonths[cal.get(GregorianCalendar.MONTH)]} ${cal.get(GregorianCalendar.YEAR)}"

    /** Called when the activity is first created. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "On Create")
        prepareCurrentWeek(-abs(cal.get(GregorianCalendar.DAY_OF_WEEK) - cal.firstDayOfWeek))
    }

    private fun prepareCurrentWeek(distance: Int) {
        dateList.clear()

        cal.add(GregorianCalendar.DAY_OF_MONTH, distance)

        monthViewerText = "${mMonths[cal.get(GregorianCalendar.MONTH)]} ${cal.get(GregorianCalendar.YEAR)}"
        Log.d("CalendarFragment", cal.time.toString())
        for (i in 0..6) {
            val x = cal.get(GregorianCalendar.DAY_OF_MONTH)
            val isToday = cal.get(GregorianCalendar.DAY_OF_MONTH)==TODAY_DAY &&
                    cal.get(GregorianCalendar.MONTH)==TODAY_MONTH && cal.get(GregorianCalendar.YEAR)==TODAY_YEAR

            dateList.add(Pair(x, isToday))
            cal.add(GregorianCalendar.DAY_OF_MONTH, 1)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_calendar, container, false)

        val monthViewer: TextView = rootView.findViewById(R.id.month_viewer)
        monthViewer.text = monthViewerText

        // GridView for showing the dates
        val dateGridview: GridView = rootView.findViewById(R.id.calendar)
        dateGridview.adapter = CalendarGridCellAdapter(rootView.context, dateList)

        dateGridview.onItemClickListener =
                OnItemClickListener { parent, v, position, id ->
                    Toast.makeText(rootView.context, "$position", Toast.LENGTH_SHORT).show()
                }

        // GridView for showing the events
        val eventGridview: GridView = rootView.findViewById(R.id.events)
        eventGridview.adapter = EventsGridCellAdapter(rootView.context, dateList)

        eventGridview.onItemClickListener =
                OnItemClickListener { parent, v, position, id ->
                    Toast.makeText(rootView.context, "$position", Toast.LENGTH_SHORT).show()
                }

        val nextWeek: MaterialButton = rootView.findViewById(R.id.next_button)
        nextWeek.setOnClickListener { v ->
            prepareCurrentWeek(0)
            monthViewer.text = monthViewerText
            dateGridview.adapter = CalendarGridCellAdapter(rootView.context, dateList)
        }

        val prevWeek: MaterialButton = rootView.findViewById(R.id.prev_button)
        prevWeek.setOnClickListener { v ->
            prepareCurrentWeek(-14)
            monthViewer.text = monthViewerText
            dateGridview.adapter = CalendarGridCellAdapter(rootView.context, dateList)
        }

        return rootView
    }

    override fun onDestroy() {
        Log.d(tag, "Destroying View …")
        super.onDestroy()
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(sectionNumber: Int): CalendarFragment {
            val fragment = CalendarFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}