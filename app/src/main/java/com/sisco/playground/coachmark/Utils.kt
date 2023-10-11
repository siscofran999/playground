package com.sisco.playground.coachmark

import android.content.Context
import android.util.TypedValue

object Utils {

    fun dpToPx(context: Context, dp: Int): Float {
        return dpToPx(context, dp.toFloat())
    }

    private fun dpToPx(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }
}

enum class Shape {
    BOX
}

enum class Gravity {
    CENTER,
    START,
    END,
    TOP,
    BOTTOM
}