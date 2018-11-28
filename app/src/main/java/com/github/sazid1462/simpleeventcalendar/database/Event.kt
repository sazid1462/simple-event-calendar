package com.github.sazid1462.simpleeventcalendar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class Event (
    @ColumnInfo(name = "event_id") @PrimaryKey var eventId: Int,
    @ColumnInfo(name = "event_title") var eventTitle: String?,
    @ColumnInfo(name = "event_note") var eventNote: String?,
    @ColumnInfo(name = "event_schedule") var eventSchedule: Date?
)