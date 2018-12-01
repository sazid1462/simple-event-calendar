package com.github.sazid1462.simpleeventcalendar.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.sazid1462.simpleeventcalendar.R
import com.github.sazid1462.simpleeventcalendar.database.Event
import com.github.sazid1462.simpleeventcalendar.ui.MainActivity
import java.util.*
import android.content.Intent




class SignInAlertDialogFragment : DialogFragment() {

    private lateinit var rootView: View
    private lateinit var msg: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout

            builder.setView(rootView)
                .setTitle(getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton(
                    getString(R.string.action_sign_in)
                ) { dialog, id ->
                    (activity as MainActivity).showSignIn()
                }
                .setNegativeButton(
                    R.string.close
                ) { dialog, id ->
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    activity?.finish()
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(msg: String): SignInAlertDialogFragment {
            val fragment = SignInAlertDialogFragment()
            val args = Bundle()
            fragment.arguments = args

            fragment.msg = msg

            return fragment
        }
    }
}
