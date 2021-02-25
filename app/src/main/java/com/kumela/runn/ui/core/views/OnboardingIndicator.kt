package com.kumela.runn.ui.core.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.kumela.runn.R
import kotlin.math.min
import kotlin.math.roundToInt

@Suppress("MemberVisibilityCanBePrivate")
class OnboardingIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.OnboardingIndicator
) : View(context, attrs, defStyleAttr) {

    sealed class IndicatorShape {
        data class Circle(val cx: Float, val cy: Float, val radius: Float) : IndicatorShape()
        data class RectF(val left: Float, val top: Float, val right: Float, val bottom: Float) :
            IndicatorShape()
    }

    // Dynamic Variables
    private val shapes: MutableList<IndicatorShape> = ArrayList()

    private var _indicatorIndex: Int = 0
    var indicatorIndex: Int
        get() = _indicatorIndex
        set(value) {
            if (value in 0 until indicatorCount) {
                _indicatorIndex = value
                computeShapes(width, height)
                invalidate()
            }
        }

    // Attribute Defaults
    @ColorInt private var _colorActive = DEFAULT_ACTIVE_COLOR
    @ColorInt private var _colorInactive = DEFAULT_INACTIVE_COLOR
    private var _indicatorCount = 0

    // Core Attributes
    var colorActive: Int
        @ColorInt get() = _colorActive
        set(@ColorInt value) {
            _colorActive = value
            paintActive.color = value
            invalidate()
        }

    var colorInactive: Int
        @ColorInt get() = _colorInactive
        set(@ColorInt value) {
            _colorInactive = value
            paintInactive.color = value
            invalidate()
        }

    var indicatorCount: Int
        get() = _indicatorCount
        set(value) {
            _indicatorCount = value
            computeShapes(width, height)
            invalidate()
        }

    // Paints
    private val paintActive = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = colorActive
    }

    private val paintInactive = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = colorInactive
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OnboardingIndicator,
            defStyleAttr,
            0
        )

        try {
            colorInactive =
                typedArray.getColor(R.styleable.OnboardingIndicator_colorInactive, colorInactive)
            colorActive =
                typedArray.getColor(R.styleable.OnboardingIndicator_colorActive, colorActive)
            indicatorCount =
                typedArray.getInteger(
                    R.styleable.OnboardingIndicator_indicatorCount,
                    indicatorCount
                )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth: Int = DEFAULT_WIDTH.dpToPx().toInt()
        val desiredHeight: Int = DEFAULT_HEIGHT.dpToPx().toInt()

        //Measure Width
        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        //Measure Height
        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize)
            else -> desiredHeight
        }
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        computeShapes(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (shape in shapes) {
            when (shape) {
                is IndicatorShape.Circle -> canvas?.drawCircle(
                    shape.cx,
                    shape.cy,
                    shape.radius,
                    paintInactive
                )
                is IndicatorShape.RectF -> canvas?.drawRoundRect(
                    shape.left,
                    shape.top,
                    shape.right,
                    shape.bottom,
                    100f,
                    100f,
                    paintActive
                )
            }
        }
    }

    @Suppress("LiftReturnOrAssignment")
    private fun computeShapes(w: Int, h: Int) {
        val inactiveIndicatorWidth = w / (indicatorCount * 2)
        val activeIndicatorWidth = inactiveIndicatorWidth * 2
        var lastX = 0f
        shapes.clear()
        for (i in 0 until indicatorCount) {
            if (i == indicatorIndex) {
                val shape =
                    IndicatorShape.RectF(lastX, 0f, lastX + activeIndicatorWidth, h.toFloat())
                shapes.add(shape)
                lastX += inactiveIndicatorWidth * 2
            } else {
                val shape = IndicatorShape.Circle(
                    lastX + activeIndicatorWidth / 2,
                    h.toFloat() / 2,
                    inactiveIndicatorWidth / 2f
                )
                shapes.add(shape)
                lastX += inactiveIndicatorWidth * 2
            }
        }
    }

    private fun Float.dpToPx(): Float =
        (this * resources.displayMetrics.density).roundToInt().toFloat()

    companion object {
        private const val DEFAULT_WIDTH = 60f
        private const val DEFAULT_HEIGHT = 10f

        private val DEFAULT_INACTIVE_COLOR = Color.parseColor("#222228")
        private val DEFAULT_ACTIVE_COLOR = Color.parseColor("#D7D7D8")
    }
}
