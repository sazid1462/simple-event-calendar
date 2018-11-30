package com.github.sazid1462.simpleeventcalendar.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.GridView
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.sazid1462.simpleeventcalendar.AppExecutors
import com.github.sazid1462.simpleeventcalendar.EventRepository
import com.github.sazid1462.simpleeventcalendar.R
import com.github.sazid1462.simpleeventcalendar.database.Event
import com.github.sazid1462.simpleeventcalendar.database.EventRoomDatabase
import java.sql.Date
import java.util.*


class CreateEventFragment : DialogFragment() {

    private var dateTimeObject: DateTimeObject? = null
    private var event: Event? = null
    private var mode: String = CREATE_EVENT_DIALOG_MODE

    private lateinit var rootView: View

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rootView = inflater.inflate(R.layout.fragment_create_event, null)

            builder.setView(rootView)
                .setTitle(if (mode == CREATE_EVENT_DIALOG_MODE) getString(R.string.add_evnt) else getString(R.string.edit_event))
                .setPositiveButton(
                    if (mode == CREATE_EVENT_DIALOG_MODE) getString(R.string.create) else getString(R.string.update)
                ) { dialog, id ->
                    val event_id = if (mode == CREATE_EVENT_DIALOG_MODE) event!!.eventId else UUID.randomUUID().toString()
                    val title = rootView.findViewById<EditText>(R.id.eventTitle).text
                    val note = rootView.findViewById<EditText>(R.id.eventNote).text
                    val datePicker = rootView.findViewById<DatePicker>(R.id.datePicker)
                    val timePicker = rootView.findViewById<TimePicker>(R.id.timePicker)
                    val schedule = DateTimeObject.new(datePicker.year,
                        datePicker.month,
                        datePicker.dayOfMonth,
                        timePicker.hour,
                        timePicker.minute)
                    val er: EventRepository? =
                        EventRepository.getInstance(EventRoomDatabase.getInstance(context!!, AppExecutors()))

                    if (mode == CREATE_EVENT_DIALOG_MODE) er?.insert(Event(event_id, title.toString(), note.toString(), schedule.date.time))
                    else er?.update(Event(event_id, title.toString(), note.toString(), schedule.date.time))

                    Log.d("CreateEvent", "schedule ${schedule.date} month ${schedule.month} day ${schedule.day}")
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, id ->
                    // No need to do anything
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        if (dateTimeObject != null) {
            val datePicker: DatePicker? = rootView.findViewById(R.id.datePicker)
            datePicker?.updateDate(
                dateTimeObject!!.year,
                dateTimeObject!!.month,
                dateTimeObject!!.day
            )
            val timePicker: TimePicker? = rootView.findViewById(R.id.timePicker)
            timePicker?.hour = dateTimeObject!!.hour
            timePicker?.minute = dateTimeObject!!.minute
        }
        return rootView
    }

    companion object {
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(dateTimeObject: DateTimeObject?): CreateEventFragment {
            val fragment = CreateEventFragment()
            val args = Bundle()
            fragment.arguments = args

            fragment.dateTimeObject = dateTimeObject
            fragment.mode = CREATE_EVENT_DIALOG_MODE

            return fragment
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(event: Event): CreateEventFragment {
            val fragment = CreateEventFragment()
            val args = Bundle()
            fragment.arguments = args

            fragment.event = event
            fragment.mode = EDIT_EVENT_DIALOG_MODE

            return fragment
        }
    }
}
