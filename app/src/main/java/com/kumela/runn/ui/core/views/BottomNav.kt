package com.kumela.runn.ui.core.views

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.FontRes
import androidx.annotation.XmlRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.kumela.runn.R
import com.kumela.runn.ui.core.views.parsers.BottomNavParser
import kotlinx.parcelize.Parcelize
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt


@Suppress("MemberVisibilityCanBePrivate")
class BottomNav @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.BottomNav,
) : View(context, attrs, defStyleAttr) {

    data class Item(
        var title: String,
        val icon: Drawable,
        var rect: RectF = RectF(),
        var alpha: Int = 0,
    )

    fun interface OnItemSelectedListener {
        fun onItemSelected(pos: Int)
    }

    // Dynamic Variables
    private var itemWidth: Float = 0F
    private var currentIconTint = itemIconTintActive

    private var items = emptyList<Item>()

    private var _itemActiveIndex: Int = 0

    // Attribute Defaults
    @Dimension private var _barSideMargins = DEFAULT_SIDE_MARGIN.dpToPx()
    @Dimension private var _itemPadding = DEFAULT_ITEM_PADDING.dpToPx()
    @Dimension private var _itemIconSize = DEFAULT_ICON_SIZE.dpToPx()
    @Dimension private var _itemIconMargin = DEFAULT_ICON_MARGIN.dpToPx()
    @Dimension private var _itemTextSize = DEFAULT_TEXT_SIZE.dpToPx()
    @Dimension private var _fabSize = DEFAULT_FAB_SIZE.dpToPx()
    @Dimension private var _fabMargin = DEFAULT_FAB_MARGIN.dpToPx()

    @ColorInt private var _itemIconTint = Color.parseColor(DEFAULT_TINT)
    @ColorInt private var _barBackgroundColor = Color.WHITE
    @ColorInt private var _itemIconTintActive = Color.WHITE
    @ColorInt private var _itemTextColor = Color.WHITE

    @FontRes private var _itemFontFamily: Int = INVALID_RES
    @XmlRes private var _itemMenuRes: Int = INVALID_RES

    // Core Attributes
    var barBackgroundColor: Int
        @ColorInt get() = _barBackgroundColor
        set(@ColorInt value) {
            _barBackgroundColor = value
            paintBackground.color = value
            invalidate()
        }

    var barSideMargins: Float
        @Dimension get() = _barSideMargins
        set(@Dimension value) {
            _barSideMargins = value
            recalculateItemPositions()
            invalidate()
        }

    var itemTextSize: Float
        @Dimension get() = _itemTextSize
        set(@Dimension value) {
            _itemTextSize = value
            paintText.textSize = value
            recalculateItemPositions()
            invalidate()
        }

    var itemTextColor: Int
        @ColorInt get() = _itemTextColor
        set(@ColorInt value) {
            _itemTextColor = value
            paintText.color = value
            invalidate()
        }

    var itemPadding: Float
        @Dimension get() = _itemPadding
        set(@Dimension value) {
            _itemPadding = value
            recalculateItemPositions()
            invalidate()
        }

    var itemIconSize: Float
        @Dimension get() = _itemIconSize
        set(@Dimension value) {
            _itemIconSize = value
            invalidate()
        }

    var itemIconMargin: Float
        @Dimension get() = _itemIconMargin
        set(@Dimension value) {
            _itemIconMargin = value
            recalculateItemPositions()
            invalidate()
        }

    var fabSize: Float
        @Dimension get() = _fabSize
        set(@Dimension value) {
            _fabSize = value
            recalculateBackgroundPath()
            invalidate()
        }

    var fabMargin: Float
        @Dimension get() = _fabMargin
        set(@Dimension value) {
            _fabMargin = value
            recalculateBackgroundPath()
            invalidate()
        }

    var itemIconTint: Int
        @ColorInt get() = _itemIconTint
        set(@ColorInt value) {
            _itemIconTint = value
            invalidate()
        }

    var itemIconTintActive: Int
        @ColorInt get() = _itemIconTintActive
        set(@ColorInt value) {
            _itemIconTintActive = value
            invalidate()
        }

    private var itemFontFamily: Int
        @FontRes get() = _itemFontFamily
        set(@FontRes value) {
            _itemFontFamily = value
            if (value != INVALID_RES) {
                paintText.typeface = ResourcesCompat.getFont(context, value)
                invalidate()
            }
        }

    private var itemMenuRes: Int
        @XmlRes get() = _itemMenuRes
        set(@XmlRes value) {
            _itemMenuRes = value
            if (value != INVALID_RES) {
                items = BottomNavParser(context, value).parse()
                invalidate()
            }
        }

    private var itemActiveIndex: Int
        get() = _itemActiveIndex
        set(value) {
            _itemActiveIndex = value
            applyItemActiveIndex()
        }

    // Listeners
    var onItemSelectedListener: OnItemSelectedListener? = null

    // Paints
    private val paintBackground = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = barBackgroundColor
    }

    private val paintText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = itemTextColor
        textSize = itemTextSize
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    // paths
    private val pathBackground = Path()

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BottomNav,
            defStyleAttr,
            0
        )

        try {
            barSideMargins =
                typedArray.getDimension(R.styleable.BottomNav_sideMargins, barSideMargins)
            itemPadding = typedArray.getDimension(R.styleable.BottomNav_itemPadding, itemPadding)
            itemTextSize = typedArray.getDimension(R.styleable.BottomNav_textSize, itemTextSize)
            itemIconSize = typedArray.getDimension(R.styleable.BottomNav_iconSize, itemIconSize)
            itemIconMargin =
                typedArray.getDimension(R.styleable.BottomNav_iconMargin, itemIconMargin)
            fabSize = typedArray.getDimension(R.styleable.BottomNav_centerFabSize, fabSize)
            fabMargin = typedArray.getDimension(R.styleable.BottomNav_centerFabMargin, fabMargin)
            barBackgroundColor =
                typedArray.getColor(R.styleable.BottomNav_backgroundColor, barBackgroundColor)
            itemTextColor = typedArray.getColor(R.styleable.BottomNav_textColor, itemTextColor)
            itemIconTint = typedArray.getColor(R.styleable.BottomNav_iconTintInactive, itemIconTint)
            itemIconTintActive =
                typedArray.getColor(R.styleable.BottomNav_iconTintActive, itemIconTintActive)
            itemActiveIndex = typedArray.getInt(R.styleable.BottomNav_activeItem, itemActiveIndex)
            itemFontFamily =
                typedArray.getResourceId(R.styleable.BottomNav_itemFontFamily, itemFontFamily)
            itemMenuRes = typedArray.getResourceId(R.styleable.BottomNav_menu, itemMenuRes)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }


    @Parcelize
    private class BottomNavState(
        val superSaveState: Parcelable?,
        val itemActiveIndex: Int,
    ) : View.BaseSavedState(superSaveState), Parcelable

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return BottomNavState(superState, itemActiveIndex)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bottomNavState = state as? BottomNavState
        super.onRestoreInstanceState(bottomNavState?.superSaveState)

        itemActiveIndex = bottomNavState?.itemActiveIndex ?: _itemActiveIndex
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        recalculateItemPositions()
        recalculateBackgroundPath()

        // Set initial active item
        applyItemActiveIndex()
    }

    private fun recalculateItemPositions() {
        var lastX = barSideMargins
        itemWidth = (width - (barSideMargins * 2)) / (items.size + 1)

        for ((index, item) in items.withIndex()) {
            // Prevent text overflow by shortening the item title
            var shorted = false
            while (paintText.measureText(item.title) > itemWidth - itemIconMargin - (itemPadding * 2)) {
                item.title = item.title.dropLast(1)
                shorted = true
            }

            // Add ellipsis character to item text if it is shorted
            if (shorted) {
                item.title = item.title.dropLast(1)
                item.title += context.getString(R.string.ellipsis)
            }

            if (index == 2) {
                lastX += itemWidth
            }
            item.rect = RectF(lastX, 0f, itemWidth + lastX, height.toFloat())
            lastX += itemWidth
        }
    }

    private fun recalculateBackgroundPath() {
        val fabHalfHeight = fabSize / sqrt(2f)

        pathBackground.reset()

        pathBackground.moveTo(0f, 0f)
        pathBackground.lineTo(width / 2f - fabHalfHeight - fabMargin, 0f)
        pathBackground.lineTo(width / 2f, fabHalfHeight + fabMargin)
        pathBackground.lineTo(width / 2f + fabHalfHeight + fabMargin, 0f)
        pathBackground.lineTo(width.toFloat(), 0f)
        pathBackground.lineTo(width.toFloat(), height.toFloat())
        pathBackground.lineTo(0f, height.toFloat())
        pathBackground.lineTo(0f, 0f)

        pathBackground.close()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        canvas.drawPath(pathBackground, paintBackground)

        val textHeight = (paintText.descent() + paintText.ascent()) / 2

        for ((index, item) in items.withIndex()) {
            item.icon.mutate()
            item.icon.setBounds(
                item.rect.centerX().toInt()
                        - itemIconSize.toInt() / 2,
                height / 2 - itemIconSize.toInt() / 2
                        + (textHeight * 2 * (item.alpha / OPAQUE.toFloat())).toInt(),
                item.rect.centerX().toInt()
                        + itemIconSize.toInt() / 2,
                height / 2 + itemIconSize.toInt() / 2
                        + (textHeight * 2 * (item.alpha / OPAQUE.toFloat())).toInt()
            )

            tintAndDrawIcon(item, index, canvas)

            paintText.alpha = item.alpha
            canvas.drawText(
                item.title,
                item.rect.centerX(),
                item.rect.centerY() + textHeight + itemIconSize, paintText
            )
        }
    }

    private fun tintAndDrawIcon(item: Item, index: Int, canvas: Canvas) {
        DrawableCompat.setTint(
            item.icon,
            if (index == itemActiveIndex) currentIconTint else itemIconTint
        )

        item.icon.draw(canvas)
    }

    /**
     * Handle item clicks
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP && abs(event.downTime - event.eventTime) < 500) {
            for ((i, item) in items.withIndex()) {
                if (item.rect.contains(event.x, event.y)) {
                    if (i != itemActiveIndex) {
                        itemActiveIndex = i
                        onItemSelectedListener?.onItemSelected(i)
                    }
                }
            }
        }

        return true
    }

    private fun applyItemActiveIndex() {
        if (items.isNotEmpty()) {
            for ((index, item) in items.withIndex()) {
                if (index == itemActiveIndex) {
                    animateAlpha(item, OPAQUE)
                } else {
                    animateAlpha(item, TRANSPARENT)
                }
            }

            ValueAnimator.ofObject(ArgbEvaluator(), itemIconTint, itemIconTintActive).apply {
                duration = DEFAULT_ANIM_DURATION
                addUpdateListener {
                    currentIconTint = it.animatedValue as Int
                }
                start()
            }
        }
    }

    private fun animateAlpha(item: Item, to: Int) {
        ValueAnimator.ofInt(item.alpha, to).apply {
            duration = DEFAULT_ANIM_DURATION
            addUpdateListener {
                item.alpha = it.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    fun resetIndex() {
        itemActiveIndex = 0
    }

    private fun Float.dpToPx(): Float {
        return (this * resources.displayMetrics.density).roundToInt().toFloat()
    }

    companion object {
        private const val INVALID_RES = -1
        private const val DEFAULT_TINT = "#C8FFFFFF"

        private const val DEFAULT_SIDE_MARGIN = 10F
        private const val DEFAULT_ITEM_PADDING = 10F
        private const val DEFAULT_ANIM_DURATION = 200L
        private const val DEFAULT_ICON_SIZE = 20F
        private const val DEFAULT_ICON_MARGIN = 4F
        private const val DEFAULT_TEXT_SIZE = 12F

        private const val DEFAULT_FAB_SIZE = 56F
        private const val DEFAULT_FAB_MARGIN = 4F

        private const val OPAQUE = 255
        private const val TRANSPARENT = 0
    }
}
