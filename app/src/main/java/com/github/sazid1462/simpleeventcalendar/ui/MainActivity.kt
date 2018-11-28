package com.github.sazid1462.simpleeventcalendar.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.sazid1462.simpleeventcalendar.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

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
    }

}
