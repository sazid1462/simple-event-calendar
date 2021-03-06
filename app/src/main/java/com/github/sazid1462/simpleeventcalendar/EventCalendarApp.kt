package com.github.sazid1462.simpleeventcalendar

import android.app.Application
import com.github.sazid1462.simpleeventcalendar.database.EventRoomDatabase
import com.google.firebase.auth.FirebaseUser


/**
 * Android Application class. Used for accessing singletons.
 */
class EventCalendarApp : Application() {

    private var mAppExecutors: AppExecutors? = null

    val database: EventRoomDatabase
        get() = EventRoomDatabase.getInstance(this, mAppExecutors)

    val repository: EventRepository
        get() = EventRepository.getInstance(database, mAppExecutors, this)!!

    override fun onCreate() {
        super.onCreate()

        mAppExecutors = AppExecutors()
    }

    var user: FirebaseUser? = null
}