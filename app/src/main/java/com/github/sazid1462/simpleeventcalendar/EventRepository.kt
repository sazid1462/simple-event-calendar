package com.github.sazid1462.simpleeventcalendar

import androidx.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.sazid1462.simpleeventcalendar.database.Event
import com.github.sazid1462.simpleeventcalendar.database.EventDao
import com.github.sazid1462.simpleeventcalendar.database.EventRoomDatabase


class EventRepository(eventRoomDatabase: EventRoomDatabase) {
    private var mDatabase: EventRoomDatabase = eventRoomDatabase

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
        InsertAsyncTask(mDatabase.eventDao()).execute(event)
    }

    fun loadEvent(eventId: Int): LiveData<Event> {
        return mDatabase.eventDao().loadEvent(eventId)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: EventDao?) :
        AsyncTask<Event, Void, Void>() {

        override fun doInBackground(vararg params: Event): Void? {
            mAsyncTaskDao?.insert(params[0])
            return null
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: EventRepository? = null

        fun getInstance(database: EventRoomDatabase): EventRepository? {
            if (INSTANCE == null) {
                synchronized(EventRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = EventRepository(database)
                    }
                }
            }
            return INSTANCE
        }
    }
}