package com.github.sazid1462.simpleeventcalendar.ui.fragments

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
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.sazid1462.simpleeventcalendar.EventCalendarApp
import com.github.sazid1462.simpleeventcalendar.EventRepository
import com.github.sazid1462.simpleeventcalendar.R
import com.github.sazid1462.simpleeventcalendar.database.Event
import com.github.sazid1462.simpleeventcalendar.ui.CREATE_EVENT_DIALOG_MODE
import com.github.sazid1462.simpleeventcalendar.ui.DateTimeObject
import com.github.sazid1462.simpleeventcalendar.ui.EDIT_EVENT_DIALOG_MODE
import java.util.*

/**
 * DialogFragment to show create/edit event dialog
 */
class CreateEventFragment : DialogFragment() {

    lateinit var datePicker: DatePicker
    lateinit var timePicker: TimePicker
    lateinit var titleEditText: EditText
    lateinit var noteEditText: EditText
    private var dateTimeObject: DateTimeObject? = null
    private var event: Event? = null
    private var mode: String = CREATE_EVENT_DIALOG_MODE // Same fragment is used for create and edit. Hence the mode.

    private lateinit var rootView: View

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        rootView = inflater.inflate(R.layout.fragment_create_event, null)
        titleEditText = rootView.findViewById(R.id.eventTitle)
        noteEditText = rootView.findViewById(R.id.eventNote)
        datePicker = rootView.findViewById(R.id.datePicker)
        timePicker = rootView.findViewById(R.id.timePicker)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            builder.setView(rootView) // set the custom layout
                .setTitle(if (mode == CREATE_EVENT_DIALOG_MODE) getString(R.string.add_evnt) else getString(R.string.edit_event))
                .setPositiveButton(
                    if (mode == CREATE_EVENT_DIALOG_MODE) getString(R.string.create) else getString(R.string.update)
                ) { dialog, id ->
                    val event_id =
                        if (mode == CREATE_EVENT_DIALOG_MODE) UUID.randomUUID().toString() else event!!.eventId
                    val title = titleEditText.text
                    val note = noteEditText.text
                    val schedule = DateTimeObject.new(
                        datePicker.year,
                        datePicker.month,
                        datePicker.dayOfMonth,
                        timePicker.hour,
                        timePicker.minute
                    )
                    // Insert new event in the database or update the existing one.
                    val er: EventRepository? = (activity?.application as EventCalendarApp).repository
                    if (mode == CREATE_EVENT_DIALOG_MODE) run {
                        val nEvent = Event(
                            event_id,
                            (activity?.application as EventCalendarApp).user?.uid,
                            title.toString(),
                            note.toString(),
                            schedule.date.time
                        )
                        er?.insert(nEvent)
                    } else run {
                        val nEvent = Event(
                            event_id,
                            (activity?.application as EventCalendarApp).user?.uid,
                            title.toString(),
                            note.toString(),
                            schedule.date.time
                        )
                        er?.update(nEvent)
                    }
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

        // Update the UI if the event creation is initialized with an initial date.
        if (dateTimeObject != null) {
            datePicker.updateDate(
                dateTimeObject!!.year,
                dateTimeObject!!.month,
                dateTimeObject!!.day
            )
            timePicker.hour = dateTimeObject!!.hour
            timePicker.minute = dateTimeObject!!.minute
        }
        // Show the appropriate title depending on the mode
        if (mode == EDIT_EVENT_DIALOG_MODE) {
            titleEditText.setText(event?.eventTitle)
            noteEditText.setText(event?.eventNote)
        }
        return rootView
    }

    companion object {
        /**
         * Returns a new instance of this fragment of Create Event Mode
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
         * Returns a new instance of this fragment of Edit Event Mode
         */
        fun newInstance(event: Event): CreateEventFragment {
            val fragment = CreateEventFragment()
            val args = Bundle()
            fragment.arguments = args

            fragment.dateTimeObject =
                    DateTimeObject.new(Date(event.eventSchedule!!))
            fragment.event = event
            fragment.mode = EDIT_EVENT_DIALOG_MODE

            return fragment
        }
    }
}
