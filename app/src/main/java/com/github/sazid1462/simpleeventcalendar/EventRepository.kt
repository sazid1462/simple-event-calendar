package com.github.sazid1462.simpleeventcalendar

import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.github.sazid1462.simpleeventcalendar.database.Event
import com.github.sazid1462.simpleeventcalendar.database.EventFirebaseLiveData
import com.github.sazid1462.simpleeventcalendar.database.EventRoomDatabase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


/**
 * Database repository class to encapsulate the operations across the two different databases.
 */
class EventRepository(eventRoomDatabase: EventRoomDatabase, executors: AppExecutors, private val application: EventCalendarApp) {
    private var mDatabase: EventRoomDatabase = eventRoomDatabase
    private var mExecutors: AppExecutors = executors
    private var firebaseEventReference: DatabaseReference? = null

    private val firebaseLiveData: EventFirebaseLiveData = EventFirebaseLiveData()
    private var currentEvent: Event? = null

    @NonNull
    fun getFirebaseDataSnapshotLiveData(): LiveData<DataSnapshot> {
        return firebaseLiveData
    }

    /**
     * iN case of a login event sync the database and store and update the events that were created anonymously
     */
    fun onUserLogIn(user: FirebaseUser?) {
        if (user == null) return
        currentEvent = null
        firebaseEventReference = FirebaseDatabase.getInstance().getReference("/event/${application.user?.uid}")
        firebaseLiveData.setReference(firebaseEventReference!!)
        mExecutors.networkIO().execute {
            sync(getAllUnsyncedEvents())
        }
    }

    /**
     * Sync remote events to local DB
     */
    fun sync(dataSnapshot: DataSnapshot?) {
        mExecutors.diskIO().execute {
            val dsEvents = dataSnapshot?.children
            for (event in dsEvents!!) {
                val mEvent: Event = event.getValue(Event::class.java)!!
                // Skip if the change notification is due to self change
                if (mEvent.eventId == currentEvent?.eventId) {
                    Log.d("EventRepository", "Event ${mEvent.eventTitle} is currently processed. skipping")
                    continue
                }
                insertIntoLocalDB(mEvent)
            }
        }
    }

    /**
     * Sync local events to remote DB
     */
    fun sync(events: List<Event>?) {
        if (events == null) return
        for (event in events) {
            event.userId = application.user?.uid
            firebaseEventReference?.child(event.eventId)?.setValue(event)
        }
    }

    /**
     * Get the list of events from the database and get notified when the data changes.
     */
    fun getAllEvents(): LiveData<List<Event>> {
        if (application.user?.uid == null) {
            return mDatabase.eventDao().loadAllEventsOffline()
        }
        return mDatabase.eventDao().loadAllEvents(application.user!!.uid)
    }
    /**
     * Get the list of events from the database and get notified when the data changes.
     */
    fun getAllUnsyncedEvents(): List<Event> {
        return mDatabase.eventDao().loadAllUnsyncedEvents()
    }

    /**
     * Get all events in a certain time schedule range.
     * @param startDate is timestamp in millis
     * @param endDate is timestamp in millis
     */
    fun getEventsBySchedule(startDate: Long, endDate: Long): LiveData<List<Event>> {
        val newEvents: LiveData<List<Event>>
        if (application.user?.uid == null)
            newEvents = mDatabase.eventDao().loadAllByScheduleRangeOffline(startDate, endDate)
        else
            newEvents = mDatabase.eventDao().loadAllByScheduleRange(startDate, endDate, application.user?.uid)
        Log.d("EventRepository get", "start $startDate end $endDate items $newEvents")
        return newEvents
    }

    /**
     * Handle insert in both DBs
     */
    fun insert(event: Event) {
        insertIntoLocalDB(event)
        insertIntoRemoteDB(event)
    }

    fun insertIntoLocalDB(event: Event) {
//        InsertAsyncTask(mDatabase.eventDao()).execute(event)
        currentEvent = event
        mExecutors.diskIO().execute {
            mDatabase.eventDao().insert(event)
        }
    }

    fun insertIntoRemoteDB(event: Event) {
        mExecutors.networkIO().execute {
            firebaseEventReference?.child(event.eventId)?.setValue(event)
        }
    }

    fun loadEvent(eventId: Int): LiveData<Event> {
        return mDatabase.eventDao().loadEvent(eventId, application.user!!.uid)
    }

    /**
     * Handle delete from both DBs
     */
    fun delete(event: Event) {
//        mDatabase.eventDao().delete(event)
        mExecutors.diskIO().execute {
            mDatabase.eventDao().delete(event)
        }
        mExecutors.networkIO().execute {
            firebaseEventReference?.child(event.eventId)?.removeValue()
        }
    }

    /**
     * Handle update in both DBs
     */
    fun update(event: Event) {
        updateInLocalDB(event)
        insertIntoRemoteDB(event)
    }

    fun updateInLocalDB(event: Event) {
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