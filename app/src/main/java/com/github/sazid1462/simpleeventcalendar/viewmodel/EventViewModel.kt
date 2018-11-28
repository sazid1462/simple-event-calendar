package com.github.sazid1462.simpleeventcalendar.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.AndroidViewModel
import com.github.sazid1462.simpleeventcalendar.AppExecutors
import com.github.sazid1462.simpleeventcalendar.EventCalendarApp
import com.github.sazid1462.simpleeventcalendar.EventRepository
import com.github.sazid1462.simpleeventcalendar.database.Event
import com.github.sazid1462.simpleeventcalendar.database.EventRoomDatabase


class EventViewModel(application: Application) : AndroidViewModel(application) {
    init {

    }
    private val mRepository: EventRepository =
        EventRepository.getInstance(EventRoomDatabase.getInstance(application.applicationContext, AppExecutors()))!!
    val allEvents: LiveData<List<Event>> = mRepository.getEvents()

    fun insert(event: Event) {
        mRepository.insert(event)
    }
}