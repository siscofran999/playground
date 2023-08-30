package com.sisco.playground.coachmark

import android.content.Context
import android.util.TypedValue

object Utils {

    fun dpToPx(context: Context, dp: Int): Float {
        val px: Float = dpToPx(context, dp.toFloat())
        return px
    }

    fun dpToPx(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }

    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }
}