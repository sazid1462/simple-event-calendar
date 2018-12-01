package com.github.sazid1462.simpleeventcalendar.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.sazid1462.simpleeventcalendar.EventCalendarApp
import com.github.sazid1462.simpleeventcalendar.R
import com.github.sazid1462.simpleeventcalendar.database.Event
import java.util.*


class ShowEventFragment  : DialogFragment() {
    var event: Event? = null
//    var dateTimeObject: DateTimeObject? = null
    private lateinit var rootView: View

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rootView = inflater.inflate(R.layout.fragment_show_event, null)
            builder.setView(rootView)
                .setTitle(event?.eventTitle)
                .setPositiveButton(
                    R.string.edit
                ) { dialog, id ->
                    (targetFragment as CalendarFragment).showCreateEventDialog(event!!)
                }
                .setNeutralButton(
                    R.string.close
                ) { dialog, id ->
                    // No need to do anything
                }
                .setNegativeButton(
                    R.string.delete
                ) { dialog, id ->
                    (activity?.application as EventCalendarApp).repository.delete(event!!)
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val eventNote: TextView = rootView.findViewById(R.id.eventNote)
        eventNote.text = event?.eventNote
        val eventSchedule: TextView = rootView.findViewById(R.id.eventSchedule)
        eventSchedule.text = Date(event?.eventSchedule!!).toString()

        return rootView
    }

    companion object {
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(event: Event): DialogFragment {
            val fragment = ShowEventFragment()
            val args = Bundle()
            fragment.arguments = args

//            val dateTimeObject: DateTimeObject = DateTimeObject.new(Date(event.eventSchedule!!))
//            fragment.dateTimeObject = dateTimeObject
            fragment.event = event

            return fragment
        }
    }
}
