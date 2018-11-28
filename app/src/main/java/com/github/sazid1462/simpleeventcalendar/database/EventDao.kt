package com.github.sazid1462.simpleeventcalendar.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.sql.Date

@Dao
interface EventDao {
    @Query("SELECT * FROM event")
    fun loadAllEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM event WHERE event_id IN (:eventIds)")
    fun loadAllByIds(eventIds: IntArray): LiveData<List<Event>>

    @Query("SELECT * FROM event WHERE event_schedule BETWEEN :startDate AND :endDate")
    fun loadAllByScheduleRange(startDate: Long, endDate: Long): LiveData<List<Event>>

    @Query("SELECT * FROM event WHERE event_id = :eventId")
    fun loadEvent(eventId: Int): LiveData<Event>

    @Query("SELECT * FROM event WHERE event_title LIKE :title LIMIT 1")
    fun findByTitle(title: String): LiveData<Event>

    @Insert
    fun insertAll(vararg events: Event)

    @Insert
    fun insert(event: Event)

    @Delete
    fun delete(event: Event)
}