package com.github.sazid1462.simpleeventcalendar.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.github.sazid1462.simpleeventcalendar.R
import kotlinx.android.synthetic.main.activity_main.*
import com.github.sazid1462.simpleeventcalendar.AppExecutors
import com.github.sazid1462.simpleeventcalendar.EventCalendarApp
import com.github.sazid1462.simpleeventcalendar.ui.fragments.CalendarFragment
import com.github.sazid1462.simpleeventcalendar.ui.fragments.SignInAlertDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * The MainActivity of the application
 */
class MainActivity : AppCompatActivity() {
    private var isInit: Boolean = false
    private var user: FirebaseUser? = null
    private lateinit var menu: Menu
    private var mAppExecutors: AppExecutors? = null
    var fragment: CalendarFragment? = null
    // Choose authentication providers
    val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mAppExecutors = AppExecutors()
    }

    /**
     * To show the Sign In activity
     */
    internal fun showSignIn() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.ic_launcher)      // Set logo drawable
                .setTheme(R.style.AppTheme_Dialog)      // Set theme
                .setIsSmartLockEnabled(true)
                .enableAnonymousUsersAutoUpgrade()
                .build(), RC_SIGN_IN
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // already signed in
            onUserLogIn(auth.currentUser!!)
        } else {
            // not signed in
            // Create and launch sign-in intent
            showSignIn()
        }
        return true
    }

    /**
     * Update the UI and the menu item according to the logged in status of user
     */
    private fun updateUI(user: FirebaseUser?, menu: Menu) {
        this.user = user
        (application as EventCalendarApp).user = user
        // ...
        if (!isInit) { // If not already created the calendar fragment create and initialize
            isInit = true
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragment = CalendarFragment.newInstance()
            fragmentTransaction.add(R.id.container, fragment!!)
            fragmentTransaction.commit()

            // Floating action button
            fab.setOnClickListener { view ->
                fragment!!.showCreateEventDialog(null)
            }
        } else {
            fragment?.refresh()
        }
        if (user != null) menu.getItem(0).title = getString(R.string.action_sign_out)
        else menu.getItem(0).title = getString(R.string.action_sign_in)
    }

    fun showSignInAlertDialog(msg: String) {
        val newFragment = SignInAlertDialogFragment.newInstance(msg)
        newFragment.show(supportFragmentManager, "signIn")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                onUserLogIn(FirebaseAuth.getInstance().currentUser!!)
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                if (response == null) {
                    Toast.makeText(this, "Please sign in to store the data in cloud.", Toast.LENGTH_LONG).show()
                    showSignInAlertDialog("Please sign in first.")
                } else {
                    Toast.makeText(this, "Sign in failed!", Toast.LENGTH_LONG).show()
                    showSignInAlertDialog("Sign in failed! Retry?")
                }
            }
        }
    }

    private fun onUserLogIn(user: FirebaseUser) {
        (application as EventCalendarApp).user = user
        (application as EventCalendarApp).repository.onUserLogIn(user)
        updateUI(user, menu)
    }

    fun startOffline() {
        (application as EventCalendarApp).user = null
        (application as EventCalendarApp).repository.onUserLogIn(null)
        updateUI(null, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.sign_in -> {
                if (user == null) {
                    showSignIn()
                } else {
                    FirebaseAuth.getInstance().signOut()
                    startOffline()
                }
                true
            }
            else -> {
                false
            }
        }
    }
}
