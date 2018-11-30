package com.github.sazid1462.simpleeventcalendar.ui

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.GestureDetector
import android.view.View
import android.view.View.OnTouchListener


open class OnSwipeTouchListener(context: Context) : OnTouchListener {

    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, GestureListener())
        gestureDetector.setIsLongpressEnabled(false)
    }

    override fun onTouch(v: View, e: MotionEvent): Boolean {
        if (!gestureDetector.onTouchEvent(e) && !gestureDetector.onGenericMotionEvent(e)) {
            v.performClick()
            return false
        }
        return true
    }

    private inner class GestureListener : SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        return if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    return if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                return true
            }

            return true
        }

    }

    open fun onSwipeRight(): Boolean { return false }

    open fun onSwipeLeft(): Boolean { return false }

    open fun onSwipeTop(): Boolean { return false }

    open fun onSwipeBottom(): Boolean { return false }
}