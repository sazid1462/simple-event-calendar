package com.github.sazid1462.simpleeventcalendar.ui.adapters

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.BaseAdapter
import java.util.*
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.sazid1462.simpleeventcalendar.R
import com.github.sazid1462.simpleeventcalendar.database.Event
import com.github.sazid1462.simpleeventcalendar.viewmodel.EventViewModel
import androidx.lifecycle.ViewModelProviders
import com.github.sazid1462.simpleeventcalendar.ui.DateTimeObject
import com.github.sazid1462.simpleeventcalendar.ui.NO_OF_DAYS
import java.sql.Date
import kotlin.collections.ArrayList


class EventsGridCellAdapter (private val context: Context, hostFragment: Fragment, private var dateList: ArrayList<Pair<DateTimeObject, Boolean>>) : BaseAdapter() {

    // First, let's obtain an instance of GregorianCalendar.
    private var cal = GregorianCalendar.getInstance()
    internal var mEvents: MutableList<Event?> = ArrayList()
    private var mEventViewModel: EventViewModel = ViewModelProviders.of(hostFragment,
        EventViewModel.EventViewModelFactory(hostFragment.activity!!.application,
            dateList[0].first.floorDateObject().time,
            dateList[NO_OF_DAYS -1].first.ceilDateObject().time)).get(EventViewModel::class.java)

    init {
//        mEventViewModel.events.observe(hostFragment,
        mEventViewModel.events.observe(hostFragment,
            Observer<List<Event>> { events ->
                // Update the cached copy of the words in the adapter.
                mEvents = ArrayList(70)
                mEvents.addAll(arrayOfNulls(70))
                val cnt: Array<Int> = arrayOf(0, 0, 0, 0, 0, 0, 0)
                if (events != null) {
                    events.forEach { event ->
                        run {
                            val schedule =
                                DateTimeObject.new(Date(event.eventSchedule!!))
                            val idx = (schedule.day - dateList[0].first.day) + (cnt[schedule.day - dateList[0].first.day] * NO_OF_DAYS)
                            if (idx >= mEvents.size) {
                                val shortage = (idx - mEvents.size + 1)
                                mEvents.addAll(arrayOfNulls(((shortage+7)/7)*7))
                            }
                            mEvents[idx] = event
                            cnt[schedule.day - dateList[0].first.day] ++
                        }
                    }
                    notifyDataSetInvalidated()
                }
            })
    }

    override fun getCount(): Int {
        return mEvents.size + NO_OF_DAYS
    }

    override fun getItem(position: Int): Any? {
        return mEvents[(position - NO_OF_DAYS)]?.eventTitle
    }

    override fun getItemId(position: Int): Long {
        return (position - NO_OF_DAYS).toLong()
    }

    fun setDateList(dateList: ArrayList<Pair<DateTimeObject, Boolean>>) {
        this.dateList = dateList
        Log.d("CellAdapter setDate", "start ${dateList[0].first.floorDateObject().time} end ${dateList[NO_OF_DAYS -1].first.ceilDateObject().time}")
        mEventViewModel.updateDataSet(dateList[0].first.floorDateObject().time,
            dateList[NO_OF_DAYS -1].first.ceilDateObject().time)
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
            textViewEvent.text = ""
        } else {
            if (mEvents[position- NO_OF_DAYS] != null) {
                Log.d("CellAdapter getView",
                    "position $position \nId ${mEvents[position- NO_OF_DAYS]?.eventId}" +
                            "\nTitle ${mEvents[position- NO_OF_DAYS]?.eventTitle}" +
                            "\nNote ${mEvents[position- NO_OF_DAYS]?.eventNote}" +
                            "\nSchedule ${mEvents[position- NO_OF_DAYS]?.eventSchedule}" +
                            "\nrange ${dateList[0].first.floorDateObject().time} ${dateList[NO_OF_DAYS -1].first.ceilDateObject().time}")
                textViewEvent.text = mEvents[(position - NO_OF_DAYS)]?.eventTitle
            } else {
                textViewEvent.text = ""
            }
            textViewEvent.background = null
//            cellView.background = context.getDrawable(R.drawable.rect_border)
        }
        return cellView
    }

}


