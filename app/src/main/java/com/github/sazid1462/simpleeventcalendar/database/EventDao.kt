package com.github.sazid1462.simpleeventcalendar.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.room.*
import java.sql.Date

@Dao
interface EventDao {
    @Query("SELECT * FROM event WHERE user_id = :userId")
    fun loadAllEvents(userId: String): LiveData<List<Event>>

    @Query("SELECT * FROM event WHERE user_id IS NULL")
    fun loadAllEventsOffline(): LiveData<List<Event>>

    @Query("SELECT * FROM event WHERE user_id IS NULL")
    fun loadAllUnsyncedEvents(): List<Event>

    @Query("SELECT * FROM event WHERE (event_schedule BETWEEN :startDate AND :endDate) AND user_id = :userId")
    fun loadAllByScheduleRange(startDate: Long, endDate: Long, userId: String?): LiveData<List<Event>>

    @Query("SELECT * FROM event WHERE (event_schedule BETWEEN :startDate AND :endDate) AND user_id IS NULL")
    fun loadAllByScheduleRangeOffline(startDate: Long, endDate: Long): LiveData<List<Event>>

    @Query("SELECT * FROM event WHERE event_id = :eventId AND user_id = :userId")
    fun loadEvent(eventId: Int, userId: String): LiveData<Event>

    @Query("SELECT * FROM event WHERE event_id = :eventId AND user_id IS NULL")
    fun loadEventOffline(eventId: Int): LiveData<Event>

    @Query("SELECT * FROM event WHERE event_title LIKE :title AND user_id = :userId LIMIT 1")
    fun findByTitle(title: String, userId: String): LiveData<Event>

    @Insert
    fun insertAll(vararg events: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: Event)

    @Delete
    fun delete(event: Event)

    @Update
    fun update(event: Event)
}