package com.github.sazid1462.simpleeventcalendar.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.sazid1462.simpleeventcalendar.R
import com.github.sazid1462.simpleeventcalendar.database.EventRoomDatabase
import kotlinx.android.synthetic.main.activity_main.*
import com.github.sazid1462.simpleeventcalendar.AppExecutors
import com.github.sazid1462.simpleeventcalendar.EventRepository


class MainActivity : AppCompatActivity() {

    private var mAppExecutors: AppExecutors? = null

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
    }

}
