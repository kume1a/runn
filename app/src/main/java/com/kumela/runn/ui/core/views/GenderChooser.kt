package com.kumela.runn.ui.core.views

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.kumela.runn.R
import com.kumela.runn.core.enums.Gender
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt

@Suppress("MemberVisibilityCanBePrivate")
class GenderChooser @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.GenderChooser,
) : View(context, attrs, defStyleAttr) {

    fun interface OnChangeListener {
        fun onGenderChanged(gender: Gender)
    }

    private var onChangeListener: OnChangeListener? = null

    // Dynamic Variables
    var gender: Gender = Gender.FEMALE
        set(value) {
            if (field == value) return
            post {
                when (value) {
                    Gender.FEMALE -> indicatorAnimator.reverse()
                    Gender.MALE -> indicatorAnimator.start()
                }
            }
            field = value
        }
    private var indicatorCenterOffset: Float = 0f

    // Attribute Defaults
    private var _iconMale: Drawable? = ContextCompat.getDrawable(context, DEFAULT_MALE_ICON_RES)
    private var _iconFemale: Drawable? = ContextCompat.getDrawable(context, DEFAULT_FEMALE_ICON_RES)
    @ColorInt private var _colorIcon: Int = DEFAULT_ICON_COLOR
    @ColorInt private var _colorBackground: Int = DEFAULT_BACKGROUND_COLOR
    @ColorInt private var _colorForeground: Int = DEFAULT_FOREGROUND_COLOR
    @Dimension private var _iconSize: Float = DEFAULT_ICON_SIZE.dpToPx()
    private var _animationDuration: Float = DEFAULT_ANIMATION_DURATION

    // Core Attributes
    var iconMale: Drawable?
        get() = _iconMale
        set(value) {
            _iconMale = value
            invalidate()
        }

    var iconFemale: Drawable?
        get() = _iconFemale
        set(value) {
            _iconFemale = value
            invalidate()
        }

    var colorIcon: Int
        @ColorInt get() = _colorIcon
        set(@ColorInt value) {
            _colorIcon = value
            invalidate()
        }

    var colorBackground: Int
        @ColorInt get() = _colorBackground
        set(@ColorInt value) {
            _colorBackground = value
            paintBackground.color = value
            invalidate()
        }

    var colorForeground: Int
        @ColorInt get() = _colorForeground
        set(@ColorInt value) {
            _colorForeground = value
            paintForeground.color = value
            invalidate()
        }

    var iconSize: Float
        @Dimension get() = _iconSize
        set(@Dimension value) {
            _iconSize = value
            invalidate()
        }

    var animationDuration: Float
        get() = _animationDuration
        set(value) {
            _animationDuration = value
        }

    // Paints
    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = colorBackground
    }

    private val paintForeground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = colorForeground
    }

    // animation
    private val indicatorAnimator: ValueAnimator by lazy(LazyThreadSafetyMode.NONE) {
        ValueAnimator.ofFloat(0f, width - height.toFloat()).apply {
            duration = animationDuration.toLong()
            addUpdateListener {
                indicatorCenterOffset = animatedValue as Float
                invalidate()
            }
        }
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GenderChooser,
            defStyleAttr,
            0
        )

        try {
            iconMale = typedArray.getDrawable(R.styleable.GenderChooser_icon_male) ?: _iconMale
            iconFemale = typedArray.getDrawable(R.styleable.GenderChooser_icon_female) ?: iconFemale
            colorIcon = typedArray.getColor(R.styleable.GenderChooser_color_icon, colorIcon)
            colorBackground =
                typedArray.getColor(R.styleable.GenderChooser_color_background, colorBackground)
            colorForeground =
                typedArray.getColor(R.styleable.GenderChooser_color_foreground, colorForeground)
            iconSize = typedArray.getDimension(R.styleable.GenderChooser_size_icon, iconSize)
            animationDuration =
                typedArray.getFloat(R.styleable.GenderChooser_animation_duration, animationDuration)
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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null) {
            val indicatorRadius = height / 2
            val iconMargin = indicatorRadius - iconSize.toInt() / 2

            canvas.drawRoundRect(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                height / 2f,
                height / 2f,
                paintBackground
            )
            canvas.drawCircle(
                indicatorRadius + indicatorCenterOffset,
                indicatorRadius.toFloat(),
                indicatorRadius.toFloat(),
                paintForeground
            )
            tintAndDrawIcon(
                iconMale, canvas,
                iconMargin,
                iconMargin,
                indicatorRadius + iconSize.toInt() / 2,
                indicatorRadius + iconSize.toInt() / 2
            )
            tintAndDrawIcon(
                iconFemale, canvas,
                width - iconMargin - iconSize.toInt(),
                iconMargin,
                width - iconMargin,
                iconMargin + iconSize.toInt()
            )
        }
    }

    private fun tintAndDrawIcon(
        icon: Drawable?,
        canvas: Canvas,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
    ) {
        if (icon != null) {
            DrawableCompat.setTint(icon, colorIcon)
            icon.setBounds(left, top, right, bottom)
            icon.draw(canvas)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null &&
            event.action == MotionEvent.ACTION_DOWN &&
            abs(event.downTime - event.eventTime) < 500
        ) {
            when (gender) {
                Gender.MALE -> {
                    if (event.x < width - height) {
                        gender = Gender.FEMALE
                    }
                }
                Gender.FEMALE -> {
                    if (event.x > height) {
                        gender = Gender.MALE
                    }
                }
            }
            onChangeListener?.onGenderChanged(gender)
        }
        return super.onTouchEvent(event)
    }

    @Suppress("unused")
    fun setOnChangeListener(onChangeListener: OnChangeListener) {
        this.onChangeListener = onChangeListener
    }

    private fun Float.dpToPx(): Float =
        (this * resources.displayMetrics.density).roundToInt().toFloat()

    companion object {
        private const val DEFAULT_WIDTH = 180f
        private const val DEFAULT_HEIGHT = 75f

        private const val DEFAULT_MALE_ICON_RES = R.drawable.ic_male
        private const val DEFAULT_FEMALE_ICON_RES = R.drawable.ic_female
        private const val DEFAULT_ICON_COLOR = Color.WHITE
        private val DEFAULT_BACKGROUND_COLOR = Color.parseColor("#222228")
        private val DEFAULT_FOREGROUND_COLOR = Color.parseColor("#296BD7")
        private const val DEFAULT_ICON_SIZE = 50f
        private const val DEFAULT_ANIMATION_DURATION = 300f
    }
}
