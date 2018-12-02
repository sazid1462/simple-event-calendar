package com.github.sazid1462.simpleeventcalendar.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*

/**
 * Class to hold the firebase realtime database live data
 */
class EventFirebaseLiveData: LiveData<DataSnapshot>() {
    private val LOG_TAG = "EventFirebaseLiveData"

    var query: Query? = null
    private val listener: EventValueEventListener = EventValueEventListener()

    override fun onActive() {
        Log.d(LOG_TAG, "onActive");
        query?.addValueEventListener(listener)
    }

    override fun onInactive() {
        Log.d(LOG_TAG, "onInactive");
        query?.removeEventListener(listener)
    }

    fun setReference(firebaseEventReference: DatabaseReference) {
        query = firebaseEventReference
    }

    private inner class EventValueEventListener: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            value = dataSnapshot
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e(LOG_TAG, "Can't listen to query $query. ${databaseError.message}")
        }
    }

    companion object {
        /**
         * Important note: database reference is also a query in terms of firebase database.
         * That's why both function sets the value of query.
         */
        fun getInstance(query: Query): EventFirebaseLiveData {
            val eventFirebaseLiveData = EventFirebaseLiveData()
            eventFirebaseLiveData.query = query
            return eventFirebaseLiveData
        }

        fun getInstance(ref: DatabaseReference): EventFirebaseLiveData {
            val eventFirebaseLiveData = EventFirebaseLiveData()
            eventFirebaseLiveData.query = ref
            return eventFirebaseLiveData
        }
    }
}