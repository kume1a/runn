package com.kumela.views.charts.bar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.kumela.views.R
import com.kumela.views.core.ViewUtils
import com.kumela.views.model.BarChartEntry

@Suppress("MemberVisibilityCanBePrivate", "unused")
class BarChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.BarChart,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val barChartRecyclerView = BarChartRecyclerView(context)

    private val barChartLabelRenderer = BarChartBackgroundRenderer()

    // Dynamic Variables
    private val data = mutableListOf<BarChartEntry>()
    fun bindData(d: List<BarChartEntry>) {
        data.clear()
        data.addAll(d)
        barChartRecyclerView.bindData(data.map { BarChartRecyclerView.BarChartItem(it.id, it.value, false) })
        post { updateLabels() }
    }

    // Attribute Defaults
    @Dimension private var _textSize = resources.getDimension(R.dimen.bar_chart_default_text_size)
    @Dimension private var _backgroundLineHeight = resources.getDimension(R.dimen.bar_chart_default_background_line_height)

    @ColorInt private var _textColor = ContextCompat.getColor(context, R.color.bar_chart_default_text_color)
    @ColorInt private var _inactiveBarColor = ContextCompat.getColor(context, R.color.bar_chart_default_inactive_bar_color)
    @ColorInt private var _activeBarColor = ContextCompat.getColor(context, R.color.bar_chart_default_active_bar_color)
    @ColorInt private var _backgroundLineColor = ContextCompat.getColor(context, R.color.bar_chart_default_background_line_color)


    // Core Attributes
    private var textSize: Float
        @Dimension get() = _textSize
        set(@Dimension value) {
            _textSize = value
            invalidate()
        }

    private var backgroundLineHeight: Float
        @Dimension get() = _backgroundLineHeight
        set(@Dimension value) {
            _backgroundLineHeight = value
            paintBackgroundLine.strokeWidth = value
            invalidate()
        }

    private var textColor: Int
        @ColorInt get() = _textColor
        set(@ColorInt value) {
            _textColor = value
            post { paintText.color = value }
            invalidate()
        }

    private var inactiveBarColor: Int
        @ColorInt get() = _inactiveBarColor
        set(@ColorInt value) {
            _inactiveBarColor = value
            barChartRecyclerView.inactiveColor = value
            barChartRecyclerView.notifyChange()
        }

    private var activeBarColor: Int
        @ColorInt get() = _activeBarColor
        set(@ColorInt value) {
            _activeBarColor = value
            barChartRecyclerView.activeColor = value
            barChartRecyclerView.notifyChange()
        }

    private var backgroundLineColor: Int
        @ColorInt get() = _backgroundLineColor
        set(@ColorInt value) {
            _backgroundLineColor = value
            paintBackgroundLine.color = value
            invalidate()
        }

    // paints
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = this@BarChart.textSize
        textColor = this@BarChart.textColor
        isFakeBoldText = true
        letterSpacing = .05f
    }

    private val paintBackgroundLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = backgroundLineHeight
        strokeCap = Paint.Cap.ROUND
        color = backgroundLineColor
    }

    init {
        setWillNotDraw(false)
        addView(barChartRecyclerView)

        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BarChart,
            defStyleAttr,
            0
        )

        try {
            textSize = typedArray.getDimension(R.styleable.BarChart_android_textSize, textSize)
            backgroundLineHeight = typedArray.getDimension(R.styleable.BarChart_backgroundLineHeight, backgroundLineHeight)
            textColor = typedArray.getColor(R.styleable.BarChart_android_textColor, textColor)
            inactiveBarColor = typedArray.getColor(R.styleable.BarChart_inactiveBarColor, inactiveBarColor)
            activeBarColor = typedArray.getColor(R.styleable.BarChart_activeBarColor, activeBarColor)
            backgroundLineColor = typedArray.getColor(R.styleable.BarChart_backgroundLineColor, backgroundLineColor)

            barChartRecyclerView.activeColor = activeBarColor
            barChartRecyclerView.inactiveColor = inactiveBarColor
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val maximumValue = data.maxByOrNull { it.value }
        if (maximumValue != null) {
            val textWidth = paintText.measureText(maximumValue.value.toString())
            barChartRecyclerView.updatePadding(left = (textWidth + ViewUtils.dpToPx(context, 15f)).toInt())

            updateLabels()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        barChartLabelRenderer.render(
            barChartRecyclerView.paddingStart.toFloat(),
            canvas,
            paintText,
            paintBackgroundLine)
    }

    private fun updateLabels() {
        barChartLabelRenderer.updateValues(
            data.map { it.value },
            resources.getDimension(R.dimen.bar_chart_default_text_margin),
            height,
            paintText
        )
        val interval = barChartLabelRenderer.interval
        val intervalHeight = barChartLabelRenderer.intervalHeight
        if (interval != null && intervalHeight != null) {
            barChartRecyclerView.updateIntervalHeight(barChartLabelRenderer.interval!!, barChartLabelRenderer.intervalHeight!!)
        }
        invalidate()
    }

    companion object {
        private const val DEFAULT_TEXT_SIZE = 16f
    }
}
