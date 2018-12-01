package com.github.sazid1462.simpleeventcalendar.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.github.sazid1462.simpleeventcalendar.EventCalendarApp
import com.github.sazid1462.simpleeventcalendar.EventRepository
import com.github.sazid1462.simpleeventcalendar.database.Event
import com.github.sazid1462.simpleeventcalendar.ui.AsyncRunner


class EventViewModel(application: EventCalendarApp, private var startTime: Long,
                     private var endTime: Long) : AndroidViewModel(application) {
    private val mRepository: EventRepository = application.repository
    var events: MediatorLiveData<List<Event>> = MediatorLiveData()
    private var dataSource = mRepository.getEventsBySchedule(startTime, endTime)
    var firebaseLiveData = mRepository.getFirebaseDataSnapshotLiveData()

    init {
        events.addSource(dataSource) { value -> events.setValue(value) }
    }

    fun updateDataSet(startTime: Long, endTime: Long) {
        this.startTime = startTime
        this.endTime = endTime
        var newDataSource: LiveData<List<Event>>? = null
        AsyncRunner(
            {
                newDataSource = mRepository.getEventsBySchedule(startTime, endTime)
                Log.d("EventViewModel Backgrnd", "start $startTime end $endTime items $newDataSource")
            }, {
                events.removeSource(dataSource)
                dataSource = newDataSource as LiveData<List<Event>>
                events.addSource(dataSource) { value -> events.setValue(value) }
                Log.d("EventViewModel Post", "start $startTime end $endTime items ${dataSource.value}")
            }).execute()

    }

    fun refresh() {
        var newDataSource: LiveData<List<Event>>? = null
        AsyncRunner(
            {
                newDataSource = mRepository.getEventsBySchedule(startTime, endTime)
                Log.d("EventViewModel Backgrnd", "start $startTime end $endTime items $newDataSource")
            }, {
                events.removeSource(dataSource)
                dataSource = newDataSource as LiveData<List<Event>>
                events.addSource(dataSource) { value -> events.setValue(value) }
                Log.d("EventViewModel Post", "start $startTime end $endTime items ${dataSource.value}")
            }).execute()

    }

    class EventViewModelFactory(
        private val mApplication: Application,
        private val startTime: Long,
        private val endTime: Long
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            Log.d("EventViewModel Factory", "start $startTime end $endTime")
            return EventViewModel(mApplication as EventCalendarApp, startTime, endTime) as T
        }
    }
}