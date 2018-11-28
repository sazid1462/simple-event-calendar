package com.github.sazid1462.simpleeventcalendar.ui

import android.os.AsyncTask

class AsyncRunner (val background: () -> Unit, val post: () -> Unit) : AsyncTask<Any, Any, Any>() {
    override fun doInBackground(vararg params: Any?): Any? {
        return background()
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        return post()
    }
}