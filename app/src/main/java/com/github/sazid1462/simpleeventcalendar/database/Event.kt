package com.github.sazid1462.simpleeventcalendar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Event(@ColumnInfo(name = "event_id") @PrimaryKey var eventId: String,
                 @ColumnInfo(name = "user_id") var userId: String? = null,
                 @ColumnInfo(name = "event_title") var eventTitle: String? = null,
                 @ColumnInfo(name = "event_note") var eventNote: String? = null,
                 @ColumnInfo(name = "event_schedule") var eventSchedule: Long? = null) {


    constructor(): this(UUID.randomUUID().toString()) {
        // This is needed for the firebase database
    }

    companion object {
        fun getInstance(eventId: String, userId: String?, eventTitle: String?, eventNote: String?, eventSchedule: Long?): Event {
            val event = Event()
            event.eventId = eventId
            event.userId = userId
            event.eventTitle = eventTitle
            event.eventNote = eventNote
            event. eventSchedule = eventSchedule
            return event
        }
    }
}