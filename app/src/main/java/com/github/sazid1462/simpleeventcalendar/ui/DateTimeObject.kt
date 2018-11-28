package com.github.sazid1462.simpleeventcalendar.ui

import java.util.*

class DateTimeObject(private val date: Date) {
    var year: Int = 2018
    var month: Int = 1
    var day: Int = 1
    var hour: Int = 0
    var minute: Int = 0

    init {
        val cal = GregorianCalendar()
        cal.time = date
        year = cal.get(GregorianCalendar.YEAR)
        month = cal.get(GregorianCalendar.MONTH)
        day = cal.get(GregorianCalendar.DAY_OF_MONTH)
        hour = cal.get(GregorianCalendar.HOUR)
        minute = cal.get(GregorianCalendar.MINUTE)
    }

    fun getDateObject(): Date {
        return date
    }
}