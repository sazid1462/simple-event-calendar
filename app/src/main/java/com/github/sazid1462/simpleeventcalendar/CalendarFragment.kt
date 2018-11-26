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
import java.util.*

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

/**
 * A placeholder fragment containing a simple calendar view.
 */
class CalendarFragment: Fragment() {
    private var rootView: View? = null
    /** Called when the activity is first created. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "On Create")
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val calendar = GregorianCalendar.getInstance()
        val rootView = inflater.inflate(R.layout.fragment_calendar, container, false)

        val monthViewer: TextView = rootView.findViewById(R.id.month_viewer)
        monthViewer.text = "${mMonths[calendar[Calendar.MONTH]]} ${calendar[Calendar.YEAR]}"

        val gridview: GridView = rootView.findViewById(R.id.calendar)
        gridview.adapter = GridCellAdapter(rootView.context, calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))

        gridview.onItemClickListener =
                OnItemClickListener { parent, v, position, id ->
                    Toast.makeText(rootView.context, "$position", Toast.LENGTH_SHORT).show()
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