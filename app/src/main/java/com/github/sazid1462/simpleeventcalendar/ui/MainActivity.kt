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
import com.github.sazid1462.simpleeventcalendar.database.EventRoomDatabase
import kotlinx.android.synthetic.main.activity_main.*
import com.github.sazid1462.simpleeventcalendar.AppExecutors
import com.github.sazid1462.simpleeventcalendar.EventRepository
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {

    private var user: FirebaseUser? = null
    private lateinit var menu: Menu
    private var mAppExecutors: AppExecutors? = null
    // Choose authentication providers
    val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.AnonymousBuilder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = CalendarFragment.newInstance()
        fragmentTransaction.add(R.id.container, fragment)
        fragmentTransaction.commit()

        fab.setOnClickListener { view ->
            fragment.showCreateEventDialog(null)
        }
        
        mAppExecutors = AppExecutors()

        // Create and launch sign-in intent
        showSignIn()
    }

    private fun showSignIn() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.ic_launcher)      // Set logo drawable
                .setTheme(R.style.AppTheme_Dialog)      // Set theme
                .build(), RC_SIGN_IN
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().currentUser
                // ...
                menu.getItem(0).title = getString(R.string.action_sign_out)
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                if (response == null) {
                    Toast.makeText(this, "Please sign in to store the data in cloud.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Sign in failed! You won't be able to store the data in cloud.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.sign_in -> {
                if (user == null) {
                    showSignIn()
                } else {
                    FirebaseAuth.getInstance().signOut()
                    menu.getItem(0).title = getString(R.string.action_sign_in)
                    user = null
                    Toast.makeText(this, "You've been signed out. Please sign in to store the data in cloud.", Toast.LENGTH_LONG).show()
                }
                true
            }
            else -> {
                false
            }
        }
    }
}
