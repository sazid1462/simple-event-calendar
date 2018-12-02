package com.github.sazid1462.simpleeventcalendar.ui

/**
 * Kotlin script file for holding the constants
 */

val NO_OF_DAYS: Int = 7

// Days each month has, starting from January
val mDays = arrayOf(
    31, 28, 31, 30, 31, 30,
    31, 31, 30, 31, 30, 31
)

// Long name of the months
val mMonths = arrayOf(
    "January", "February", "March", "April",
    "May", "June", "July", "August", "September",
    "October", "November", "December"
)

// Short days of week names
val mDaysOfWeek = arrayOf(
    "SUN", "MON", "TUE",
    "WED", "THU", "FRI", "SAT"
)

// Week to month mapping, It means weekToMonth[n]'th week (n+1)th month has come.
val weekToMonth = arrayOf(
    4, 8, 13, 17, 22, 26, 30, 35, 39, 43, 48, 52
)

val CREATE_EVENT_DIALOG_MODE = "create"
val EDIT_EVENT_DIALOG_MODE = "edit"
val RC_SIGN_IN = 100