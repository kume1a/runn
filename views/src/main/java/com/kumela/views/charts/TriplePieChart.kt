package com.kumela.views.charts

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import com.kumela.views.R
import com.kumela.views.core.BaseView
import com.kumela.views.core.ViewUtils
import com.kumela.views.enums.Ring
import com.kumela.views.model.TriplePieChartItem


@Suppress("MemberVisibilityCanBePrivate")
class TriplePieChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.TriplePieChart,
) : BaseView(context, attrs, defStyleAttr) {

    override val defaultWidth: Float = resources.getDimension(R.dimen.triple_pie_chart_default_size)
    override val defaultHeight: Float = resources.getDimension(R.dimen.triple_pie_chart_default_size)

    // Dynamic Variables
    private val items = mutableMapOf<Ring, TriplePieChartItem>()
    private val ringProgresses = mutableMapOf(
        Ring.OUTER to .001f,
        Ring.MIDDLE to .001f,
        Ring.INNER to .001f
    )

    // Attribute Defaults
    @ColorInt private var _colorOuter = ContextCompat.getColor(context, R.color.triple_pie_chart_default_outer_color)
    @ColorInt private var _colorMiddle = ContextCompat.getColor(context, R.color.triple_pie_chart_default_middle_color)
    @ColorInt private var _colorInner = ContextCompat.getColor(context, R.color.triple_pie_chart_default_inner_color)
    @ColorInt private var _colorInactive = ContextCompat.getColor(context, R.color.triple_pie_chart_default_inactive_color)

    @Dimension private var _strokeWidth = resources.getDimension(R.dimen.triple_pie_chart_default_stroke_width)
    @Dimension private var _strokeMargin = resources.getDimension(R.dimen.triple_pie_chart_default_stroke_margin)

    // Core Attributes
    var colorOuter: Int
        @ColorInt get() = _colorOuter
        set(@ColorInt value) {
            _colorOuter = value
            paintOuter.color = value
            invalidate()
        }

    var colorMiddle: Int
        @ColorInt get() = _colorMiddle
        set(@ColorInt value) {
            _colorMiddle = value
            paintMiddle.color = value
            invalidate()
        }

    var colorInner: Int
        @ColorInt get() = _colorInner
        set(@ColorInt value) {
            _colorInner = value
            paintInner.color = value
            invalidate()
        }

    var colorInactive: Int
        @ColorInt get() = _colorInactive
        set(@ColorInt value) {
            _colorInactive = value
            paintInactive.color = value
            invalidate()
        }

    var strokeWidth: Float
        @Dimension get() = _strokeWidth
        set(@Dimension value) {
            _strokeWidth = value
            paintOuter.strokeWidth = value
            paintMiddle.strokeWidth = value
            paintInner.strokeWidth = value
            paintInactive.strokeWidth = value / 2
            invalidate()
        }

    var strokeMargin: Float
        @Dimension get() = _strokeMargin
        set(@Dimension value) {
            _strokeMargin = value
            invalidate()
        }

    // Paints
    private val paintOuter = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = strokeWidth
        strokeCap = Paint.Cap.ROUND
        color = colorOuter
    }

    private val paintMiddle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = strokeWidth
        strokeCap = Paint.Cap.ROUND
        color = colorMiddle
    }

    private val paintInner = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = strokeWidth
        strokeCap = Paint.Cap.ROUND
        color = colorInner
    }

    private val paintInactive = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = this@TriplePieChart.strokeWidth / 2
        color = colorInactive
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TriplePieChart,
            defStyleAttr,
            0
        )

        try {
            colorOuter = typedArray.getColor(R.styleable.TriplePieChart_colorOuter, colorOuter)
            colorMiddle = typedArray.getColor(R.styleable.TriplePieChart_colorMiddle, colorMiddle)
            colorInner = typedArray.getColor(R.styleable.TriplePieChart_colorInner, colorInner)
            colorInactive = typedArray.getColor(R.styleable.TriplePieChart_colorInactive, colorInactive)
            strokeWidth = typedArray.getDimension(R.styleable.TriplePieChart_strokeWidth, strokeWidth)
            strokeMargin = typedArray.getDimension(R.styleable.TriplePieChart_strokeMargin, strokeMargin)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    private fun recalculateItems() {
        val centerX = width / 2
        val centerY = height / 2

        val startAngle = -90f

        val outerRadius = centerX.coerceAtMost(centerY).toFloat()
        val startTop = strokeWidth / 2
        val endBottom = 2 * outerRadius - startTop
        val outerRect = RectF(startTop, startTop, endBottom, endBottom)

        val middleRadius = outerRadius - strokeWidth - strokeMargin
        val middleTop = startTop + strokeWidth + strokeMargin
        val middleBottom = endBottom - strokeMargin - strokeWidth
        val middleRect = RectF(middleTop, middleTop, middleBottom, middleBottom)

        val innerRadius = outerRadius - 2 * (strokeWidth + strokeMargin)
        val innerTop = middleTop + strokeWidth + strokeMargin
        val innerBottom = middleBottom - strokeMargin - strokeWidth
        val innerRect = RectF(innerTop, innerTop, innerBottom, innerBottom)

        items[Ring.OUTER] = TriplePieChartItem(
            startAngle,
            ringProgresses[Ring.OUTER]!! * 360,
            outerRect,
            outerRadius)

        items[Ring.MIDDLE] = TriplePieChartItem(
            startAngle,
            ringProgresses[Ring.MIDDLE]!! * 360,
            middleRect,
            middleRadius)

        items[Ring.INNER] = TriplePieChartItem(
            startAngle,
            ringProgresses[Ring.INNER]!! * 360,
            innerRect,
            innerRadius)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        recalculateItems()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        for (item in items) {
            canvas.drawArc(item.value.rect, 0f, 360f, false, paintInactive)
            val paint = when (item.key) {
                Ring.OUTER -> paintOuter
                Ring.MIDDLE -> paintMiddle
                Ring.INNER -> paintInner
            }
            canvas.drawArc(item.value.rect,
                item.value.startAngle,
                item.value.sweepAngle,
                false,
                paint)
        }
    }

    private fun animateProgressChange(ring: Ring, to: Float) {
        val currentProgress = items[ring]!!.sweepAngle / 360f
        ValueAnimator.ofFloat(currentProgress, to).apply {
            interpolator = ViewUtils.accelerateDecelerateInterpolator
            addUpdateListener {
                val progress = it.animatedValue as Float
                items[ring]!!.sweepAngle = progress * 360
                invalidate()
            }
            start()
        }
    }

    fun setProgress(ring: Ring, @FloatRange(from = 0.0, to = 1.0) progress: Float) {
        ringProgresses[ring] = progress
        animateProgressChange(ring, progress)
    }
}
