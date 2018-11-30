package com.github.sazid1462.simpleeventcalendar

import androidx.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log
import com.github.sazid1462.simpleeventcalendar.database.Event
import com.github.sazid1462.simpleeventcalendar.database.EventDao
import com.github.sazid1462.simpleeventcalendar.database.EventRoomDatabase


class EventRepository(eventRoomDatabase: EventRoomDatabase, executors: AppExecutors) {
    private var mDatabase: EventRoomDatabase = eventRoomDatabase
    private var mExecutors: AppExecutors = executors

    init {
//        mEvents = mDatabase.eventDao().loadAllEvents()
    }

    /**
     * Get the list of events from the database and get notified when the data changes.
     */
    fun getAllEvents(): LiveData<List<Event>> {
        return mDatabase.eventDao().loadAllEvents()
    }

    fun getEventsBySchedule(startDate: Long, endDate: Long): LiveData<List<Event>> {
        val newEvents = mDatabase.eventDao().loadAllByScheduleRange(startDate, endDate)
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
        return mDatabase.eventDao().loadEvent(eventId)
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

        fun getInstance(database: EventRoomDatabase, executors: AppExecutors?): EventRepository? {
            if (INSTANCE == null) {
                synchronized(EventRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = EventRepository(database, executors!!)
                    }
                }
            }
            return INSTANCE
        }
    }
}