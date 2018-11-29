package com.github.sazid1462.simpleeventcalendar.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.github.sazid1462.simpleeventcalendar.AppExecutors
import com.github.sazid1462.simpleeventcalendar.EventRepository
import com.github.sazid1462.simpleeventcalendar.database.Event
import com.github.sazid1462.simpleeventcalendar.database.EventRoomDatabase
import com.github.sazid1462.simpleeventcalendar.ui.AsyncRunner


class EventViewModel(application: Application, private var startTime: Long,
                     private var endTime: Long) : AndroidViewModel(application) {
    private val mRepository: EventRepository =
        EventRepository.getInstance(EventRoomDatabase.getInstance(application.applicationContext, AppExecutors()))!!
    var events: MediatorLiveData<List<Event>> = MediatorLiveData()
    var dataSource = mRepository.getEventsBySchedule(startTime, endTime)

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

    class EventViewModelFactory(
        private val mApplication: Application,
        private val startTime: Long,
        private val endTime: Long
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            Log.d("EventViewModel Factory", "start $startTime end $endTime")
            return EventViewModel(mApplication, startTime, endTime) as T
        }
    }
}