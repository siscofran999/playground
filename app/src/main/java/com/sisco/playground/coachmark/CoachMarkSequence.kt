package com.sisco.playground.coachmark

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import java.util.*

class CoachMarkSequence(private val mContext: Activity) {

    private val mSequenceQueue: Queue<CoachMarkOverlay.Builder> = LinkedList()
    var mCoachMark: CoachMarkOverlay? = null
    private var mSequenceItem: CoachMarkOverlay.Builder? = null
    private var mSequenceListener: SequenceListener = object : SequenceListener {
        override fun onNextItem(coachMark: CoachMarkOverlay, coachMarkSequence: CoachMarkSequence) {
            super.onNextItem(coachMark, coachMarkSequence)
        }
    }

    private val mCoachMarkOverlayClickListener: CoachMarkOverlay.OverlayClickListener =
        object : CoachMarkOverlay.OverlayClickListener {
            override fun onOverlayClick(overlay: CoachMarkOverlay) {
                mCoachMark = overlay
                if (mSequenceQueue.size > 0) {
                    mSequenceItem = mSequenceQueue.poll()
                    if (mSequenceQueue.isEmpty()) {
                        overlay.mBuilder.setOverlayTransparentPadding(0, 0, 0, 0)
                    }
                    mSequenceItem?.apply {
                        overlay.mBuilder.setTabPosition(getTabPosition())
                        if (getOverlayTargetView() != null) {
                            overlay.mBuilder.setOverlayTargetView(getOverlayTargetView())
                        } else {
                            overlay.mBuilder.setOverlayTargetView(null)
                            overlay.mBuilder.setOverlayTargetCoordinates(getOverlayTargetCoordinates())
                        }
                        mSequenceListener.apply {
                            onNextItem(overlay, this@CoachMarkSequence)
                        }
                    }
                } else {
                    (mContext.window.decorView as ViewGroup).removeView(overlay)
                }
            }
        }

    // set the data to view the next descriptions.
    fun setNextView() {
        if (mCoachMark != null && mSequenceItem != null) {
            mCoachMark?.invalidate()
        }
    }

    fun setSequenceListener(listener: SequenceListener) {
        mSequenceListener = listener
    }

    fun addItem(targetView: View) {
        CoachMarkOverlay.Builder(mContext).apply {
            setOverlayClickListener(mCoachMarkOverlayClickListener)
            setOverlayTargetView(targetView)
            mSequenceQueue.add(this)
        }
    }

    fun start(rootView: ViewGroup? = null) {
        if (mSequenceQueue.size > 0) {
            val firstElement = mSequenceQueue.poll()
            if (rootView == null) {
                firstElement?.build()?.show((mContext).window.decorView as ViewGroup)
            } else {
                firstElement?.build()?.show(rootView)
            }
        }
    }
}