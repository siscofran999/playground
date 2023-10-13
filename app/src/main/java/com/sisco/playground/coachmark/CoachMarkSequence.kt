package com.sisco.playground.coachmark

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import java.util.*

class CoachMarkSequence(private val mContext: Activity) {

    private val mSequenceQueue: Queue<CoachMarkOverlay.Builder> = LinkedList()
    var mCoachMark: CoachMarkOverlay? = null
    private var mSequenceItem: CoachMarkOverlay.Builder? = null
    private var onFinishCallback: OnFinishCallback? = null

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
                    mSequenceItem = mSequenceQueue.poll()
                    if (mSequenceQueue.isEmpty()) {
                        overlay.mBuilder?.setOverlayTransparentPadding(0, 0, 0, 0)
                    }
                    mSequenceItem?.apply {
                        overlay.mBuilder?.let { builder ->
                            builder.setTabPosition(getTabPosition())
                            if (builder.getOverlayTargetView() != null) {
                                builder.setInfoText(getTitle(), getSubTitle(), getLimit())
                                builder.setSkipBtn(getSkipBtn())
                                builder.setTextBtnPositive(getTextBtnPositive())
                                builder.setOverlayTargetView(getOverlayTargetView())
                                builder.setGravityTopBottom(getGravityTopBottom())
                                builder.setGravityStartEnd(getGravityStartEnd())
                            } else {
                                builder.setInfoText("", "", "")
                                builder.setSkipBtn("null")
                                builder.setTextBtnPositive("")
                                builder.setOverlayTargetView(null)
                                builder.setGravityTopBottom(Gravity.NULL)
                                builder.setGravityStartEnd(Gravity.NULL)
                                builder.setOverlayTargetCoordinates(getOverlayTargetCoordinates())
                            }
                            mSequenceListener.apply {
                                onNextItem(overlay, this@CoachMarkSequence)
                            }
                        }
                    }
                } else {
                    (mContext.window.decorView as ViewGroup).removeView(overlay)
                    onFinishCallback?.onFinish()
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
        skipButtonText: String? = "Lewati",
        gravityTopBottom: Gravity = Gravity.NULL,
        gravityStartEnd: Gravity = Gravity.NULL
    ) {
        CoachMarkOverlay.Builder(mContext).apply {
            setOverlayClickListener(mCoachMarkOverlayClickListener)
            setSkipClickListener(mCoachMarkSkipButtonClickListener)
            setOverlayTargetView(targetView)
            setInfoText(title, subTitle, limit)
            setTextBtnPositive(positiveButtonText)
            setSkipBtn(skipButtonText)
            setGravityTopBottom(gravityTopBottom)
            setGravityStartEnd(gravityStartEnd)
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

    fun setOnFinishCallback(onFinishCallback: OnFinishCallback) {
        this.onFinishCallback = onFinishCallback
    }

    fun interface OnFinishCallback {
        fun onFinish()
    }
}