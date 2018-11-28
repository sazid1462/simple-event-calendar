package com.github.sazid1462.simpleeventcalendar.ui

import java.util.*

class DateTimeObject() {
    var year: Int = 2018
    var month: Int = 1
    var day: Int = 1
    var hour: Int = 0
    var minute: Int = 0
    var date: Date = Date()

    init {
        val cal = GregorianCalendar()
        cal.time = date
        year = cal.get(GregorianCalendar.YEAR)
        month = cal.get(GregorianCalendar.MONTH)
        day = cal.get(GregorianCalendar.DAY_OF_MONTH)
        hour = cal.get(GregorianCalendar.HOUR)
        minute = cal.get(GregorianCalendar.MINUTE)
    }

    constructor(date: Date) : this() {
        val cal = GregorianCalendar()
        cal.time = date
        this.date = date
        year = cal.get(GregorianCalendar.YEAR)
        month = cal.get(GregorianCalendar.MONTH)
        day = cal.get(GregorianCalendar.DAY_OF_MONTH)
        hour = cal.get(GregorianCalendar.HOUR)
        minute = cal.get(GregorianCalendar.MINUTE)
    }

    constructor(year: Int, month: Int, day: Int, hour: Int, minute: Int) : this() {
        this.year = year
        this.month = month
        this.day = day
        this.hour = hour
        this.minute = minute
        this.date = getDateObject(this)
    }

    companion object {
        @JvmStatic
        fun getDateObject(dto: DateTimeObject): Date {
            val cal = GregorianCalendar()
            cal.set(GregorianCalendar.YEAR, dto.year)
            cal.set(GregorianCalendar.MONTH, dto.month)
            cal.set(GregorianCalendar.DAY_OF_MONTH, dto.day)
            cal.set(GregorianCalendar.HOUR, dto.hour)
            cal.set(GregorianCalendar.MINUTE, dto.minute)
            return cal.time
        }

        @JvmStatic
        fun getDateObject(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date {
            val cal = GregorianCalendar()
            cal.set(GregorianCalendar.YEAR, year)
            cal.set(GregorianCalendar.MONTH, month)
            cal.set(GregorianCalendar.DAY_OF_MONTH, day)
            cal.set(GregorianCalendar.HOUR, hour)
            cal.set(GregorianCalendar.MINUTE, minute)
            return cal.time
        }
    }
}