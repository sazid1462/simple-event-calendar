package com.github.sazid1462.simpleeventcalendar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.AndroidViewModel
import com.github.sazid1462.simpleeventcalendar.EventCalendarApp
import com.github.sazid1462.simpleeventcalendar.EventRepository
import com.github.sazid1462.simpleeventcalendar.database.Event


class EventViewModel(application: EventCalendarApp, private val mRepository: EventRepository) : AndroidViewModel(application) {

    internal val allEvents: LiveData<List<Event>> = mRepository.getEvents()

    fun insert(event: Event) {
        mRepository.insert(event)
    }
}