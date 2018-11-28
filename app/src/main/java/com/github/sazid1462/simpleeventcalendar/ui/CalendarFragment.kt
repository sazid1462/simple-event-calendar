package com.github.sazid1462.simpleeventcalendar.ui

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
import com.github.sazid1462.simpleeventcalendar.R
import com.google.android.material.button.MaterialButton
import java.util.*
import kotlin.math.abs


/**
 * A placeholder fragment containing a simple calendar view.
 */
class CalendarFragment : Fragment() {

    private val TODAY_DAY = cal.get(GregorianCalendar.DAY_OF_MONTH)
    private val TODAY_MONTH = cal.get(GregorianCalendar.MONTH)
    private val TODAY_YEAR = cal.get(GregorianCalendar.YEAR)

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
            val isToday = cal.get(GregorianCalendar.DAY_OF_MONTH) == TODAY_DAY &&
                    cal.get(GregorianCalendar.MONTH) == TODAY_MONTH && cal.get(GregorianCalendar.YEAR) == TODAY_YEAR

            dateList.add(Pair(x, isToday))
            cal.add(GregorianCalendar.DAY_OF_MONTH, 1)
        }
    }

    private fun showCreateEventDialog() {
        val newFragment = CreateEventFragment()
        newFragment.setTargetFragment(this, targetRequestCode)
        newFragment.show(activity?.supportFragmentManager, "createEvent")
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_calendar, container, false)

        activity?.runOnUiThread {
            val monthViewer: TextView = rootView.findViewById(R.id.month_viewer)
            monthViewer.text = monthViewerText
            val dateGridview: GridView = registerDateGridView(rootView)
            val eventsGridview: GridView = registerEventsGridView(rootView)
            registerCalendarNavButtons(rootView, monthViewer, dateGridview, eventsGridview)
        }
        return rootView
    }

    private fun registerCalendarNavButtons(
        rootView: View,
        monthViewer: TextView,
        dateGridview: GridView,
        eventsGridview: GridView
    ) {
        val nextWeek: MaterialButton = rootView.findViewById(R.id.next_button)
        nextWeek.setOnClickListener { v ->
            activity?.runOnUiThread {
                AsyncRunner({
                    prepareCurrentWeek(0)
                }, {
                    monthViewer.text =
                            monthViewerText
                    (dateGridview.adapter as CalendarGridCellAdapter).setDateList(
                        dateList
                    )
                }).execute()
            }
        }

        val prevWeek: MaterialButton = rootView.findViewById(R.id.prev_button)
        prevWeek.setOnClickListener { v ->
            activity?.runOnUiThread {
                AsyncRunner({
                    prepareCurrentWeek(-14)
                }, {
                    monthViewer.text =
                            monthViewerText
                    (dateGridview.adapter as CalendarGridCellAdapter).setDateList(
                        dateList
                    )
                }).execute()
            }
        }
    }

    private fun registerEventsGridView(rootView: View): GridView {
        // GridView for showing the events
        val eventGridview: GridView = rootView.findViewById(R.id.events)
        eventGridview.adapter = EventsGridCellAdapter(
            rootView.context,
            dateList
        )

        eventGridview.onItemClickListener =
                OnItemClickListener { parent, v, position, id ->
                    Toast.makeText(rootView.context, "$position", Toast.LENGTH_SHORT).show()
                    showCreateEventDialog()
                }
        return eventGridview
    }

    private fun registerDateGridView(rootView: View): GridView {
        // GridView for showing the dates
        val dateGridview: GridView = rootView.findViewById(R.id.calendar)
        dateGridview.adapter = CalendarGridCellAdapter(
            rootView.context,
            dateList
        )

        dateGridview.onItemClickListener =
                OnItemClickListener { parent, v, position, id ->
                    Toast.makeText(rootView.context, "$position", Toast.LENGTH_SHORT).show()
                }
        return dateGridview
    }

    override fun onDestroy() {
        Log.d(tag, "Destroying View …")
        super.onDestroy()
    }

    companion object {
        private val cal = GregorianCalendar.getInstance()
        private var dateList: ArrayList<Pair<Int, Boolean>> = ArrayList(7)
        private var monthViewerText: String =
            "${mMonths[cal.get(GregorianCalendar.MONTH)]} ${cal.get(GregorianCalendar.YEAR)}"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(): CalendarFragment {
            val fragment = CalendarFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}