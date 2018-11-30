package com.github.sazid1462.simpleeventcalendar.ui

import android.content.Context
import android.view.MotionEvent
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.GestureDetector
import android.view.View
import android.view.View.OnTouchListener


open class OnSwipeTouchListener(context: Context) : OnTouchListener {

    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun onTouch(v: View, e: MotionEvent): Boolean {
        if (!gestureDetector.onTouchEvent(e) && e.action==MotionEvent.ACTION_UP) {
            v.performClick()
        }
        return gestureDetector.onTouchEvent(e)
    }

    private inner class GestureListener : SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

//        override fun onDown(e: MotionEvent): Boolean {
//            when (e.action) {
//                MotionEvent.ACTION_MOVE -> {
//                    return true
//                }
//            }// touch down code
//            // touch move code
//            // touch up code
//            return false
//        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                return false
            }

            return true
        }

    }

    open fun onSwipeRight(): Boolean { return false }

    open fun onSwipeLeft(): Boolean { return false }

    open fun onSwipeTop(): Boolean { return false }

    open fun onSwipeBottom(): Boolean { return false }
}