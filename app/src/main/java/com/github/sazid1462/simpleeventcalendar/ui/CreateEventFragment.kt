package com.github.sazid1462.simpleeventcalendar.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.GridView
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.sazid1462.simpleeventcalendar.R
import java.util.*


class CreateEventFragment : DialogFragment() {

    var dateTimeObject: DateTimeObject? = null

    private var rootView: View? = null

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
                .setTitle("Add Event")
                .setPositiveButton(
                    R.string.create
                ) { dialog, id ->
                    // FIRE ZE MISSILES! TODO
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, id ->
                    // User cancelled the dialog TODO
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        if (dateTimeObject != null) {
            val datePicker: DatePicker? = rootView?.findViewById(R.id.datePicker)
            datePicker?.updateDate(
                dateTimeObject!!.year,
                dateTimeObject!!.month,
                dateTimeObject!!.day
            )
            val timePicker: TimePicker? = rootView?.findViewById(R.id.timePicker)
            timePicker?.hour = dateTimeObject!!.hour
            timePicker?.minute = dateTimeObject!!.minute
        }
        return rootView
    }

    private fun setDateTime(dateTimeObject: DateTimeObject) {
        this.dateTimeObject = dateTimeObject
    }

    companion object {
        private val cal = GregorianCalendar()

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(dateTimeObject: DateTimeObject?): CreateEventFragment {
            val fragment = CreateEventFragment()
            val args = Bundle()
            fragment.arguments = args
            if (dateTimeObject != null) {
                fragment.setDateTime(dateTimeObject)
            }
            return fragment
        }
    }
}
