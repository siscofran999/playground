package com.sisco.playground.coachmark

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import kotlin.math.roundToInt

class CoachMarkOverlay(context: Context, builder: Builder) : FrameLayout(context) {

    var mBuilder: Builder
    private var mBaseBitmap: Bitmap? = null
    private var mLayer: Canvas? = null
    private val mOverlayTintPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mOverlayTransparentPaint = Paint()

    init {
        this.setWillNotDraw(false)
        mBuilder = builder
        mOverlayTransparentPaint.color = Color.TRANSPARENT
        mOverlayTransparentPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        setOnClickListener {
            mBuilder.getOverlayClickListener()?.apply {
                onOverlayClick(this@CoachMarkOverlay)
                mShouldRender = true
            }
        }
    }

    override fun invalidate() {
        mShouldRender = true
        super.invalidate()
    }

    private var mShouldRender = true
    override fun onDraw(canvas: Canvas?) {
        drawOverlayTint()
        drawTransparentOverlay()
        mBaseBitmap?.apply { canvas?.drawBitmap(this, 0f, 0f, null) }
        super.onDraw(canvas)
    }

    private fun drawOverlayTint() {
        mBaseBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mLayer = Canvas(mBaseBitmap!!)
        mOverlayTintPaint.color = mBuilder.getOverlayColor()
        val alpha = mBuilder.getOverlayOpacity()
        mOverlayTintPaint.alpha = alpha
        mLayer?.drawRect(Rect(0, 0, width, height), mOverlayTintPaint)
    }

    private fun drawTransparentOverlay() {
        val targetViewSize = Rect()
        if (mBuilder.getOverlayTargetView() != null) {
            mBuilder.getOverlayTargetView()?.getGlobalVisibleRect(targetViewSize)
        } else {
            targetViewSize.set(mBuilder.getOverlayTargetCoordinates())
        }
        targetViewSize.left -= mBuilder.getOverlayTransparentPadding().left
        targetViewSize.top -= mBuilder.getOverlayTransparentPadding().top
        targetViewSize.right += mBuilder.getOverlayTransparentPadding().right
        targetViewSize.bottom += mBuilder.getOverlayTransparentPadding().bottom

        targetViewSize.left += mBuilder.getOverlayTransparentMargin().left
        targetViewSize.top += mBuilder.getOverlayTransparentMargin().top
        targetViewSize.right += mBuilder.getOverlayTransparentMargin().right
        targetViewSize.bottom += mBuilder.getOverlayTransparentMargin().bottom
        when (mBuilder.getOverlayTransparentShape()) {
            Shape.BOX -> {
                mLayer?.drawRoundRect(
                    RectF(targetViewSize),
                    mBuilder.getOverlayTransparentCornerRadius(),
                    mBuilder.getOverlayTransparentCornerRadius(),
                    mOverlayTransparentPaint
                )
            }
            Shape.CIRCLE -> {
                var radius: Float = mBuilder.getOverlayTransparentCircleRadius()
                when (mBuilder.getOverLayType()) {
                    ShapeType.INSIDE -> {
                        if (radius < 0) {
                            radius = (targetViewSize.height() / 2).toFloat()
                        }
                    }
                    ShapeType.OUTSIDE -> {
                        if (radius < 0) {
                            radius = (targetViewSize.width() / 2).toFloat()
                        }
                    }
                }
                when (mBuilder.getOverlayTransparentGravity()) {
                    Gravity.CENTER -> mLayer?.drawCircle(
                        targetViewSize.exactCenterX(),
                        targetViewSize.exactCenterY(),
                        radius,
                        mOverlayTransparentPaint
                    )
                    Gravity.START -> mLayer?.drawCircle(
                        targetViewSize.left.toFloat(),
                        targetViewSize.exactCenterY(),
                        radius,
                        mOverlayTransparentPaint
                    )
                    Gravity.END -> mLayer?.drawCircle(
                        targetViewSize.exactCenterX(),
                        targetViewSize.right.toFloat(),
                        radius,
                        mOverlayTransparentPaint
                    )
                    else -> {}
                }
            }
            else -> {}
        }
    }

    fun show(root: ViewGroup) {
        root.addView(this)
    }

    class Builder(private val mContext: Context) {
        private var mOverlayTargetView: View? = null
        private var mOverlayColor: Int = Color.BLACK
        private var mOverlayOpacity: Int = 150
        private var mOverlayTransparentShape: Shape = Shape.BOX
        private var mOverlayTransparentCircleRadius: Float = 0f
        private var mOverlayTransparentCornerRadius: Float = 8f
        private var mOverlayTransparentGravity: Gravity = Gravity.CENTER
        private var mOverlayTransparentMargin: Rect = Rect()
        private var mOverlayTransparentPadding: Rect = Rect()
        private var mOverLayType: ShapeType = ShapeType.OUTSIDE
        private var mOverlayClickListener: OverlayClickListener? = null
        private var mTargetCoordinates: Rect = Rect()
        private var mBaseTabPosition: Int = -1

        fun getOverlayTargetView(): View? = mOverlayTargetView
        fun getOverlayColor(): Int = mOverlayColor
        fun getOverlayOpacity(): Int = mOverlayOpacity
        fun getOverlayTransparentShape(): Shape = mOverlayTransparentShape
        fun getOverlayTransparentCircleRadius(): Float = mOverlayTransparentCircleRadius
        fun getOverlayTransparentCornerRadius(): Float = mOverlayTransparentCornerRadius
        fun getOverlayTransparentGravity(): Gravity = mOverlayTransparentGravity
        fun getOverLayType(): ShapeType = mOverLayType
        fun getTabPosition(): Int = mBaseTabPosition
        fun getOverlayTransparentMargin(): Rect = mOverlayTransparentMargin
        fun getOverlayTransparentPadding(): Rect = mOverlayTransparentPadding
        fun getOverlayTargetCoordinates(): Rect = mTargetCoordinates
        fun getOverlayClickListener(): OverlayClickListener? = mOverlayClickListener

        fun setOverlayTargetCoordinates(coordinates: Rect): Builder {
            mTargetCoordinates.set(coordinates)
            return this
        }

        fun setOverlayTargetView(view: View?): Builder {
            mOverlayTargetView = view
            return this
        }

        fun setTabPosition(position: Int): Builder {
            mBaseTabPosition = position
            return this
        }

        fun setOverlayTransparentPadding(left: Int, top: Int, right: Int, bottom: Int): Builder {
            mOverlayTransparentPadding.left = Utils.dpToPx(mContext, left).roundToInt()
            mOverlayTransparentPadding.top = Utils.dpToPx(mContext, top).roundToInt()
            mOverlayTransparentPadding.right = Utils.dpToPx(mContext, right).roundToInt()
            mOverlayTransparentPadding.bottom = Utils.dpToPx(mContext, bottom).roundToInt()
            return this
        }

        fun setOverlayClickListener(listener: OverlayClickListener): Builder {
            mOverlayClickListener = listener
            return this
        }

        fun build(): CoachMarkOverlay {
            return CoachMarkOverlay(mContext, this)
        }
    }

    interface OverlayClickListener {
        fun onOverlayClick(overlay: CoachMarkOverlay)
    }
}

enum class Shape {
    BOX,
    CIRCLE,
    TRIANGLE
}

enum class Gravity {
    CENTER,
    START,
    END,
    TOP,
    BOTTOM
}

enum class ShapeType {
    INSIDE,
    OUTSIDE
}

enum class Orientation {
    UP,
    DOWN,
    LEFT,
    RIGHT,
}