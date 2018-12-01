package com.github.sazid1462.simpleeventcalendar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event (
    @ColumnInfo(name = "event_id") @PrimaryKey var eventId: String,
    @ColumnInfo(name = "user_id") var userId: String,
    @ColumnInfo(name = "event_title") var eventTitle: String?,
    @ColumnInfo(name = "event_note") var eventNote: String?,
    @ColumnInfo(name = "event_schedule") var eventSchedule: Long?
)