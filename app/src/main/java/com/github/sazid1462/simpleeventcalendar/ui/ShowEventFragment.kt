package com.github.sazid1462.simpleeventcalendar.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.sazid1462.simpleeventcalendar.R
import java.util.*


class ShowEventFragment  : DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            builder.setView(inflater.inflate(R.layout.fragment_show_event, null))
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

    companion object {
        private val cal = GregorianCalendar()

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(): DialogFragment {
            val fragment = DialogFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
