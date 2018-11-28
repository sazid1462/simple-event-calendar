package com.github.sazid1462.simpleeventcalendar

import androidx.lifecycle.LiveData
import android.app.Application
import android.os.AsyncTask
import com.github.sazid1462.simpleeventcalendar.database.Event
import com.github.sazid1462.simpleeventcalendar.database.EventDao
import com.github.sazid1462.simpleeventcalendar.database.EventRoomDatabase
import androidx.lifecycle.MediatorLiveData


class EventRepository(eventRoomDatabase: EventRoomDatabase) {
    private var mDatabase: EventRoomDatabase = eventRoomDatabase
    private var mObservableEvents: MediatorLiveData<List<Event>> = MediatorLiveData()

    init {
        mObservableEvents.addSource(
            mDatabase.eventDao().loadAllEvents()
        ) { events ->
            if (mDatabase.getDatabaseCreated().getValue() != null) {
                mObservableEvents.postValue(events)
            }
        }
    }

    /**
     * Get the list of events from the database and get notified when the data changes.
     */
    fun getEvents(): LiveData<List<Event>> {
        return mObservableEvents
    }

    fun insert(event: Event) {
        return mDatabase.eventDao().insert(event)
    }

    fun loadEvent(eventId: Int): LiveData<Event> {
        return mDatabase.eventDao().loadEvent(eventId)
    }

    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: EventDao?) :
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