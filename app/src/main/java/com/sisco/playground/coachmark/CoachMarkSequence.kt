package com.sisco.playground.coachmark

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import java.util.*

class CoachMarkSequence(private val mContext: Activity, onFinish: () -> Unit) {

    private val mSequenceQueue: Queue<CoachMarkOverlay.Builder> = LinkedList()
    var mCoachMark: CoachMarkOverlay? = null
    private var mSequenceItem: CoachMarkOverlay.Builder? = null

    private val mCoachMarkSkipButtonClickListener: CoachMarkOverlay.SkipClickListener =
        object : CoachMarkOverlay.SkipClickListener {
            override fun onSkipClick(view: View) {
                (mContext.window.decorView as ViewGroup).removeView(view)
                mSequenceQueue.clear()
            }
        }
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
                    overlay.mBuilder?.apply {
                        mSequenceItem = mSequenceQueue.poll()
                        if (mSequenceQueue.isEmpty()) {
                            setOverlayTransparentPadding(0, 0, 0, 0)
                        }
                        mSequenceItem?.apply {
                            setTabPosition(getTabPosition())
                            if (getOverlayTargetView() != null) {
                                setInfoText(getTitle(), getSubTitle(), getLimit())
                                setSkipBtn(getSkipBtn())
                                setTextBtnPositive(getTextBtnPositive())
                                setOverlayTargetView(getOverlayTargetView())
                            } else {
                                setInfoText("", "", "")
                                setSkipBtn("null")
                                setTextBtnPositive("")
                                setOverlayTargetView(null)
                                setOverlayTargetCoordinates(getOverlayTargetCoordinates())
                            }
                            mSequenceListener.apply {
                                onNextItem(overlay, this@CoachMarkSequence)
                            }
                        }
                    }
                } else {
                    (mContext.window.decorView as ViewGroup).removeView(overlay)
                    onFinish.invoke()
                }
            }
        }

    // set the data to view the next descriptions.
    fun setNextView() {
        if (mCoachMark != null && mSequenceItem != null) {
            mCoachMark?.invalidate()
        }
    }

    fun addItem(
        targetView: View,
        title: String = "",
        subTitle: String = "",
        limit: String = "",
        positiveButtonText: String = "Berikutnya",
        skipButtonText: String = "Lewati"
    ) {
        CoachMarkOverlay.Builder(mContext).apply {
            setOverlayClickListener(mCoachMarkOverlayClickListener)
            setSkipClickListener(mCoachMarkSkipButtonClickListener)
            setOverlayTargetView(targetView)
            setInfoText(title, subTitle, limit)
            setTextBtnPositive(positiveButtonText)
            setSkipBtn(skipButtonText)
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