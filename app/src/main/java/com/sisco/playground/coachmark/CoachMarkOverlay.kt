package com.sisco.playground.coachmark

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sisco.playground.R
import com.sisco.playground.databinding.ItemCoachmarkBinding
import kotlin.math.roundToInt

class CoachMarkOverlay(private val context: Context, builder: Builder) : FrameLayout(context){

    var mBuilder: Builder
    private var mContext: Context? = context
    private var mBaseBitmap: Bitmap? = null
    private var mLayer: Canvas? = null
    private val mOverlayTintPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mOverlayTransparentPaint = Paint()

    private val binding = ItemCoachmarkBinding.inflate(LayoutInflater.from(mContext), this, true)

    init {
        this.setWillNotDraw(false)
        mBuilder = builder
        mOverlayTransparentPaint.color = Color.TRANSPARENT
        mOverlayTransparentPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        // hide
        binding.btnNext.setOnClickListener {
            mBuilder.getOverlayClickListener()?.apply {
                onOverlayClick(this@CoachMarkOverlay)
            }
        }

        binding.btnSkip.setOnClickListener {
            mBuilder.getSkipClickListener()?.apply {
                onSkipClick(this@CoachMarkOverlay)
            }
        }
    }

    override fun invalidate() {
        super.invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        drawOverlayTint()
        drawTransparentOverlay()
        mBaseBitmap?.apply { canvas?.drawBitmap(this, 0f, 0f, null) }
        super.onDraw(canvas)
    }

    private fun drawOverlayTint() {
        mBaseBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mBaseBitmap?.apply {
            mLayer = Canvas(this)
            mOverlayTintPaint.color = mBuilder.getOverlayColor()
            val alpha = mBuilder.getOverlayOpacity()
            mOverlayTintPaint.alpha = alpha
            mLayer?.drawRect(Rect(0, 0, width, height), mOverlayTintPaint)
        }
    }

    private fun drawTransparentOverlay() {
        mLayer?.let { layer ->
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

            Log.i("TAG", "drawTransparentOverlay: $targetViewSize")
            Log.i("TAG", "drawTransparentOverlay top: ${targetViewSize.top}")
            Log.i("TAG", "drawTransparentOverlay left: ${targetViewSize.left}")
            Log.i("TAG", "drawTransparentOverlay right: ${targetViewSize.right}")
            Log.i("TAG", "drawTransparentOverlay bottom: ${targetViewSize.bottom}")
            Log.i("TAG", "drawTransparentOverlay y: ${targetViewSize.exactCenterY()}")
            Log.i("TAG", "drawTransparentOverlay x: ${targetViewSize.exactCenterX()}")
            val layerWidth = layer.width
            val layerHeight = layer.height
            when (mBuilder.getOverlayTransparentShape()) {
                Shape.BOX -> {
                    layer.drawRoundRect(
                        RectF(targetViewSize),
                        mBuilder.getOverlayTransparentCornerRadius(),
                        mBuilder.getOverlayTransparentCornerRadius(),
                        mOverlayTransparentPaint
                    )

                    Log.i("TAG", "drawTransparentOverlay width screen: $layerWidth")
                    Log.i("TAG", "drawTransparentOverlay height screen: $layerHeight")
                    val halfWidthScreen = layerWidth/2
                    val halfHeightScreen = layerHeight/2
                    Log.i("TAG", "drawTransparentOverlay half width screen: $halfWidthScreen")
                    Log.i("TAG", "drawTransparentOverlay half height screen: $halfHeightScreen")

                    val leftMiddleRight = positionLeftMiddleRight(targetViewSize.left, targetViewSize.right ,halfWidthScreen)
                    val topBottom = positionTopBottom(targetViewSize.bottom, halfHeightScreen)
                    Log.i("TAG", "drawTransparentOverlay position: $leftMiddleRight")
                    Log.i("TAG", "drawTransparentOverlay position: $topBottom")
                    binding.apply {
                        txvTitle.text = mBuilder.getTitle()
                        txvSubTitle.text = mBuilder.getSubTitle()
                        if (mBuilder.getLimit().isEmpty()) {
                            txvLimit.visibility = View.GONE
                        }else {
                            txvLimit.apply {
                                text = mBuilder.getLimit()
                                visibility = View.VISIBLE
                            }
                        }
                        btnNext.text = mBuilder.getTextBtnPositive()
                        if (mBuilder.getSkipBtn() == "null") {
                            btnSkip.visibility = View.GONE
                        }else btnSkip.text = mBuilder.getSkipBtn()
                        Log.i("TAG", "drawTransparentOverlay: btnText -> ${mBuilder.getTextBtnPositive()}")
                        if (topBottom == Gravity.TOP) {
                            when(leftMiddleRight) {
                                Gravity.START -> {
                                    itemDashed.visibility = View.VISIBLE
                                    itemDashed.setImageResource(R.drawable.img_dashed_coachmark_left)
                                    itemDashed.translationX = targetViewSize.right.plus(30).toFloat()
                                    itemDashed.translationY = targetViewSize.bottom.minus(targetViewSize.bottom.minus(targetViewSize.top).div(2)).toFloat()
                                    itemRoot.visibility = View.VISIBLE
                                    itemRoot.translationY = itemDashed.translationY.plus(itemDashed.height)
                                    itemRoot.translationX = 0f
                                }
                                Gravity.CENTER -> {
                                    itemDashed.visibility = View.VISIBLE
                                    itemDashed.translationX = layerWidth.div(2).toFloat()
                                    itemDashed.translationY = targetViewSize.bottom.plus(30).toFloat()
                                    itemDashed.setImageResource(R.drawable.img_dashed_coachmark_top)
                                    itemRoot.visibility = View.VISIBLE
                                    itemRoot.translationY = itemDashed.translationY.plus(itemDashed.height)
                                    itemRoot.translationX = 0f
                                }
                                Gravity.END -> {
                                    itemDashed.visibility = View.VISIBLE
                                    itemDashed.setImageResource(R.drawable.img_dashed_coachmark_right)
                                    itemDashed.translationX = targetViewSize.left.minus(120).toFloat()
                                    itemDashed.translationY = targetViewSize.bottom.minus(targetViewSize.bottom.minus(targetViewSize.top).div(2)).toFloat()
                                    itemRoot.visibility = View.VISIBLE
                                    itemRoot.translationY = itemDashed.translationY.plus(itemDashed.height)
                                    itemRoot.translationX = 0f
                                }
                                else -> {}
                            }
                        }else {
                            when(leftMiddleRight) {
                                Gravity.START -> {
                                    itemDashed.visibility = View.VISIBLE
                                    itemDashed.setImageResource(R.drawable.img_dashed_coachmark_left_bottom)
                                    itemDashed.translationX = targetViewSize.right.plus(30).toFloat()
                                    itemDashed.translationY = (targetViewSize.bottom.minus(itemDashed.height)).minus(targetViewSize.bottom.minus(targetViewSize.top).div(2)).plus(4).toFloat()
                                    itemRoot.visibility = View.VISIBLE
                                    itemRoot.translationY = itemDashed.translationY.minus(itemRoot.height)
                                    itemRoot.translationX = 0f
                                }
                                Gravity.CENTER -> {
                                    itemDashed.visibility = View.VISIBLE
                                    itemDashed.setImageResource(R.drawable.img_dashed_coachmark_bottom)
                                    itemDashed.translationX = layerWidth.div(2).toFloat()
                                    itemDashed.translationY = targetViewSize.top.minus(120).toFloat()
                                    itemRoot.visibility = View.VISIBLE
                                    itemRoot.translationY = (itemDashed.translationY - itemRoot.height)-6
                                    itemRoot.translationX = 0f
                                }
                                Gravity.END -> {
                                    itemDashed.visibility = View.VISIBLE
                                    itemDashed.setImageResource(R.drawable.img_dashed_coachmark_end_bottom)
                                    itemDashed.translationX = targetViewSize.left.minus(120).toFloat()
                                    itemDashed.translationY = (targetViewSize.bottom.minus(itemDashed.bottom)).minus(targetViewSize.bottom.minus(targetViewSize.top).div(2)).toFloat()
                                    itemRoot.visibility = View.VISIBLE
                                    itemRoot.translationY = itemDashed.translationY.minus(itemRoot.height)
                                    itemRoot.translationX = 0f
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }

    private fun positionLeftMiddleRight(left: Int, right: Int ,halfWidth: Int): Gravity {
        return if (halfWidth in left..right) {
            Gravity.CENTER
        }else if (left in 1 until halfWidth) {
            Gravity.START
        }else {
            Gravity.END
        }
    }

    private fun positionTopBottom(bottom: Int, halfHeight: Int) : Gravity {
        return if (bottom in 1 until halfHeight) {
            Gravity.TOP
        }else {
            Gravity.BOTTOM
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

    }

    fun show(root: ViewGroup) {
        root.addView(this)
    }

    class Builder(private val mContext: Context) {
        private var mOverlayTargetView: View? = null
        private var mOverlayColor: Int = Color.BLACK
        private var mOverlayOpacity: Int = 150
        private var mOverlayTransparentShape: Shape = Shape.BOX
        private var mOverlayTransparentCornerRadius: Float = 8f
        private var mOverlayTransparentMargin: Rect = Rect()
        private var mOverlayTransparentPadding: Rect = Rect()
        private var mOverlayClickListener: OverlayClickListener? = null
        private var mSkipClickListener: SkipClickListener? = null
        private var mTargetCoordinates: Rect = Rect()
        private var mBaseTabPosition: Int = -1
        private var mSetTitle: String = ""
        private var mSetSubTitle: String = ""
        private var mLimit: String = ""
        private var mTextBtnPositive: String = ""
        private var mSkipBtn: String = ""

        fun getOverlayTargetView(): View? = mOverlayTargetView
        fun getOverlayColor(): Int = mOverlayColor
        fun getOverlayOpacity(): Int = mOverlayOpacity
        fun getOverlayTransparentShape(): Shape = mOverlayTransparentShape
        fun getOverlayTransparentCornerRadius(): Float = mOverlayTransparentCornerRadius
        fun getTabPosition(): Int = mBaseTabPosition
        fun getOverlayTransparentMargin(): Rect = mOverlayTransparentMargin
        fun getOverlayTransparentPadding(): Rect = mOverlayTransparentPadding
        fun getOverlayTargetCoordinates(): Rect = mTargetCoordinates
        fun getOverlayClickListener(): OverlayClickListener? = mOverlayClickListener
        fun getSkipClickListener(): SkipClickListener? = mSkipClickListener
        fun getTitle(): String = mSetTitle
        fun getSubTitle(): String = mSetSubTitle
        fun getLimit(): String = mLimit
        fun getTextBtnPositive(): String = mTextBtnPositive
        fun getSkipBtn(): String = mSkipBtn

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

        fun setSkipClickListener(listener: SkipClickListener): Builder {
            mSkipClickListener = listener
            return this
        }

        fun setInfoText(title: String, subTitle: String, limit: String): Builder {
            mSetTitle = title
            mSetSubTitle = subTitle
            mLimit = limit
            return this
        }

        fun setTextBtnPositive(text: String): Builder {
            mTextBtnPositive = text
            Log.i("TAG", "setTextBtnPositive: $text")
            return this
        }

        fun setSkipBtn(text: String): Builder {
            mSkipBtn = text
            return this
        }

        fun build(): CoachMarkOverlay {
            return CoachMarkOverlay(mContext, this)
        }
    }

    interface OverlayClickListener {
        fun onOverlayClick(overlay: CoachMarkOverlay)
    }

    interface SkipClickListener {
        fun onSkipClick(view: View)
    }
}