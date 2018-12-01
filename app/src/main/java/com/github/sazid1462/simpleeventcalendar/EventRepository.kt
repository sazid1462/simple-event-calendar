package com.github.sazid1462.simpleeventcalendar

import android.app.Application
import androidx.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log
import com.github.sazid1462.simpleeventcalendar.database.Event
import com.github.sazid1462.simpleeventcalendar.database.EventDao
import com.github.sazid1462.simpleeventcalendar.database.EventRoomDatabase


class EventRepository(eventRoomDatabase: EventRoomDatabase, executors: AppExecutors, application: EventCalendarApp) {
    private var mDatabase: EventRoomDatabase = eventRoomDatabase
    private var mExecutors: AppExecutors = executors
    private var mApplication: EventCalendarApp = application

    /**
     * Get the list of events from the database and get notified when the data changes.
     */
    fun getAllEvents(): LiveData<List<Event>> {
        return mDatabase.eventDao().loadAllEvents(mApplication.user!!.uid)
    }

    fun getEventsBySchedule(startDate: Long, endDate: Long): LiveData<List<Event>> {
        val newEvents = mDatabase.eventDao().loadAllByScheduleRange(startDate, endDate, mApplication.user!!.uid)
        Log.d("EventRepository get", "start $startDate end $endDate items ${newEvents}")
        return newEvents
    }

    fun insert(event: Event) {
//        InsertAsyncTask(mDatabase.eventDao()).execute(event)
        mExecutors.diskIO().execute {
            mDatabase.eventDao().insert(event)
        }
    }

    fun loadEvent(eventId: Int): LiveData<Event> {
        return mDatabase.eventDao().loadEvent(eventId, mApplication.user!!.uid)
    }

    fun delete(event: Event) {
//        mDatabase.eventDao().delete(event)
        mExecutors.diskIO().execute {
            mDatabase.eventDao().delete(event)
        }
    }

    fun update(event: Event) {
//        UpdateAsyncTask(mDatabase.eventDao()).execute(event)
        mExecutors.diskIO().execute {
            mDatabase.eventDao().update(event)
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: EventRepository? = null

        fun getInstance(database: EventRoomDatabase, executors: AppExecutors?, application: EventCalendarApp): EventRepository? {
            if (INSTANCE == null) {
                synchronized(EventRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = EventRepository(database, executors!!, application)
                    }
                }
            }
            return INSTANCE
        }
    }
}