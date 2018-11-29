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
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.sazid1462.simpleeventcalendar.R
import com.github.sazid1462.simpleeventcalendar.database.Event
import com.github.sazid1462.simpleeventcalendar.viewmodel.EventViewModel
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

    lateinit var monthViewer: TextView
    lateinit var dateGridview: GridView
    lateinit var eventsGridview: GridView

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
            val x = DateTimeObject.new(cal.time)
            val isToday = x.day == TODAY_DAY && x.month == TODAY_MONTH && x.year == TODAY_YEAR

            dateList.add(Pair(x, isToday))
            cal.add(GregorianCalendar.DAY_OF_MONTH, 1)
        }
    }

    fun showCreateEventDialog(pair: Pair<DateTimeObject, Boolean>?) {
        val newFragment = CreateEventFragment.newInstance(pair?.first)
        newFragment.setTargetFragment(this, targetRequestCode)
        newFragment.show(activity?.supportFragmentManager, "createEvent")
    }

    fun showEventDetailsDialog(event: Event) {
        val newFragment = ShowEventFragment.newInstance(event)
        newFragment.setTargetFragment(this, targetRequestCode)
        newFragment.show(activity?.supportFragmentManager, "showEvent")
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_calendar, container, false)

        activity?.runOnUiThread {
            monthViewer = rootView.findViewById(R.id.month_viewer)
            monthViewer.text = monthViewerText
            dateGridview = registerDateGridView(rootView)
            eventsGridview = registerEventsGridView(rootView)
            registerCalendarNavButtons(rootView)
            registerCalendarFragmentGestures(rootView)
        }
        return rootView
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun registerCalendarFragmentGestures(rootView: View) {
        val calendarFragmentLayout = rootView.findViewById<LinearLayoutCompat>(R.id.calendarFragmentLayout)
        calendarFragmentLayout.setOnTouchListener(createOnSwipeTouchListener(rootView))
    }

    private fun registerCalendarNavButtons(rootView: View) {
        val nextWeek: MaterialButton = rootView.findViewById(R.id.next_button)
        nextWeek.setOnClickListener { v ->
            navigateCalendarWeek(0)
        }

        val prevWeek: MaterialButton = rootView.findViewById(R.id.prev_button)
        prevWeek.setOnClickListener { v ->
            navigateCalendarWeek(-14)
        }
    }

    private fun navigateCalendarWeek(distance: Int) {
        activity?.runOnUiThread {
            AsyncRunner(
                {
                    prepareCurrentWeek(distance)
                }, {
                    monthViewer.text = monthViewerText
                    (dateGridview.adapter as CalendarGridCellAdapter).setDateList(dateList)
                    (eventsGridview.adapter as EventsGridCellAdapter).setDateList(dateList)
                }).execute()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun registerEventsGridView(rootView: View): GridView {
        // GridView for showing the events
        val eventGridview: GridView = rootView.findViewById(R.id.events)
        eventGridview.adapter = EventsGridCellAdapter(
            rootView.context,
            this,
            dateList
        )

        eventGridview.onItemClickListener =
                OnItemClickListener { parent, v, position, id ->
//                    Toast.makeText(rootView.context, "$position", Toast.LENGTH_SHORT).show()
                    if (position < NO_OF_DAYS && dateList.size>position) {
                        showCreateEventDialog(dateList[position])
                    } else {
                        if (position - NO_OF_DAYS > (eventGridview.adapter as EventsGridCellAdapter).mEvents.size
                            && (eventGridview.adapter as EventsGridCellAdapter).mEvents[position - NO_OF_DAYS] != null){
                            showEventDetailsDialog((eventGridview.adapter as EventsGridCellAdapter).mEvents[position - NO_OF_DAYS]!!)
                        }
                    }
                }

        eventGridview.setOnTouchListener(createOnSwipeTouchListener(rootView))
        return eventGridview
    }

    private fun createOnSwipeTouchListener(rootView: View): OnSwipeTouchListener {
        return object : OnSwipeTouchListener(rootView.context) {
            override fun onSwipeTop(): Boolean {
                return true
            }

            override fun onSwipeRight(): Boolean {
                navigateCalendarWeek(-14)
                return true
            }

            override fun onSwipeLeft(): Boolean {
                navigateCalendarWeek(0)
                return true
            }

            override fun onSwipeBottom(): Boolean {
                return true
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
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

        dateGridview.setOnTouchListener(createOnSwipeTouchListener(rootView))

        return dateGridview
    }

    override fun onDestroy() {
        Log.d(tag, "Destroying View …")
        super.onDestroy()
    }

    companion object {
        private val cal = GregorianCalendar.getInstance()
        private var dateList: ArrayList<Pair<DateTimeObject, Boolean>> = ArrayList(7)
        private var monthViewerText: String =
            "${mMonths[cal.get(GregorianCalendar.MONTH)]} ${cal.get(GregorianCalendar.YEAR)}"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(): CalendarFragment {
            cal.time = Date()
            val fragment = CalendarFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}