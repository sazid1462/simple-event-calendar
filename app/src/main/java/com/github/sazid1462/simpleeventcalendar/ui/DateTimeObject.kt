package com.github.sazid1462.simpleeventcalendar.ui

import java.util.*

class DateTimeObject {
    var year: Int = 2018
    var month: Int = 1
    var day: Int = 1
    var hour: Int = 0
    var minute: Int = 0
    var date: Date = Date()

    init {
        val cal = GregorianCalendar()
        this.date = cal.time
        year = cal.get(GregorianCalendar.YEAR)
        month = cal.get(GregorianCalendar.MONTH)
        day = cal.get(GregorianCalendar.DAY_OF_MONTH)
        hour = cal.get(GregorianCalendar.HOUR)
        minute = cal.get(GregorianCalendar.MINUTE)
    }

    fun floorDateObject(): Date {
        val cal = GregorianCalendar()
        cal.set(year, month, day, 0, 0, 0)
        return cal.time
    }

    fun ceilDateObject(): Date {
        val cal = GregorianCalendar()
        cal.set(year, month, day, 23, 59, 59)
        return cal.time
    }

    companion object {
        fun new(date: Date) : DateTimeObject {
            val obj = DateTimeObject()
            val cal = GregorianCalendar()
            cal.time = date
            obj.date = date
            obj.year = cal.get(GregorianCalendar.YEAR)
            obj.month = cal.get(GregorianCalendar.MONTH)
            obj.day = cal.get(GregorianCalendar.DAY_OF_MONTH)
            obj.hour = cal.get(GregorianCalendar.HOUR)
            obj.minute = cal.get(GregorianCalendar.MINUTE)
            return obj
        }

        fun new(year: Int, month: Int, day: Int, hour: Int, minute: Int) : DateTimeObject {
            val obj = DateTimeObject()
            obj.year = year
            obj.month = month
            obj.day = day
            obj.hour = hour
            obj.minute = minute
            val cal = GregorianCalendar()
            cal.set(year, month, day, hour, minute)
            obj.date = cal.time
            return obj
        }
    }
}