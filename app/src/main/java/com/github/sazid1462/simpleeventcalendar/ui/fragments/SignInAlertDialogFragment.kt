package com.github.sazid1462.simpleeventcalendar.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.sazid1462.simpleeventcalendar.R
import com.github.sazid1462.simpleeventcalendar.ui.MainActivity

/**
 * Simple dialog for showing SingIn msgs
 */
class SignInAlertDialogFragment : DialogFragment() {

    private lateinit var msg: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            builder
                .setTitle(getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton(
                    getString(R.string.action_sign_in)
                ) { dialog, id ->
                    (activity as MainActivity).showSignIn()
                }
                .setNegativeButton(
                    R.string.skip
                ) { dialog, id ->
                    (activity as MainActivity).startOffline()
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        /**
         * Returns a new instance of this fragment for the given msg
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
