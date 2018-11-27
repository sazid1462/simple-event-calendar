package com.github.sazid1462.simpleeventcalendar

import android.os.AsyncTask

class AsyncRunner (val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}