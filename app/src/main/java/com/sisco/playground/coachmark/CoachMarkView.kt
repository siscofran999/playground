package com.sisco.playground.coachmark

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout

class CoachMarkView private constructor(context: Context, view: View?) :
    FrameLayout(context){

    private val BACKGROUND_COLOR = -0x67000000
    private val APPEARING_ANIMATION_DURATION = 400
    private val selfPaint = Paint()
    private val selfRect = Rect()
    private val target: View?
    private var targetRect: RectF? = null
    private val targetPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val X_FER_MODE_CLEAR: Xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    private val RADIUS_SIZE_TARGET_RECT = 15

    init {
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_HARDWARE, null) // setTargetView Transparent
        target = view

        val layoutListener: ViewTreeObserver.OnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            targetRect = if (target is Targetable) {
                (target as Targetable).boundingRect()
            } else {
                val locationTarget = IntArray(2)
                target!!.getLocationOnScreen(locationTarget)
                Log.i("TAG", "width: ${target.width}")
                Log.i("TAG", "height: ${target.height}")
                RectF(
                    locationTarget[0].toFloat(),
                    locationTarget[1].toFloat(),
                    (locationTarget[0] + target.width).toFloat(),
                    (locationTarget[1] + target.height).toFloat()
                )
            }
            Log.i("TAG", ": $targetRect")
            selfRect[paddingLeft, paddingTop, width - paddingRight] = height - paddingBottom
        }
        viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.i("TAG", "onDraw: ")

        // setBackground
        selfPaint.color = BACKGROUND_COLOR
        selfPaint.style = Paint.Style.FILL
        selfPaint.isAntiAlias = true
        canvas.drawRect(selfRect, selfPaint)

        targetPaint.xfermode = X_FER_MODE_CLEAR
        targetPaint.isAntiAlias = true
        if (target is Targetable) {
            (target as Targetable).guidePath()?.let { canvas.drawPath(it, targetPaint) }
        }
        else {
            canvas.drawRoundRect(
                targetRect!!,
                RADIUS_SIZE_TARGET_RECT.toFloat(),
                RADIUS_SIZE_TARGET_RECT.toFloat(),
                targetPaint
            )
        }
    }

    class Builder(private val context: Context){
        private var targetView: View? = null
        fun build() {
            val guideView = CoachMarkView(context,  targetView)
            guideView.show()
        }

        fun setTargetView(view: View?): Builder {
            targetView = view
            return this
        }
    }

    private fun show(){
        this.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        this.isClickable = false
        ((context as Activity).window.decorView as ViewGroup).addView(this)
        val startAnimation = AlphaAnimation(0.0f, 1.0f)
        startAnimation.duration = APPEARING_ANIMATION_DURATION.toLong()
        startAnimation.fillAfter = true
        startAnimation(startAnimation)
    }
}