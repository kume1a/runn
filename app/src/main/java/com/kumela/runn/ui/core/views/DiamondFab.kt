package com.kumela.runn.ui.core.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.content.res.getDrawableOrThrow
import com.kumela.runn.R
import com.kumela.runn.ui.core.views.core.BaseView

@Suppress("MemberVisibilityCanBePrivate")
class DiamondFab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.DiamondFab,
) : BaseView(context, attrs, defStyleAttr) {

    override val defaultWidth: Int = DEFAULT_SIZE
    override val defaultHeight: Int = DEFAULT_SIZE

    // Attribute Defaults
    @Dimension private var _iconSize = DEFAULT_ICON_SIZE.dpToPx()
    @ColorInt private var _backgroundColor = Color.parseColor(DEFAULT_BACKGROUND_COLOR)

    // Core Attributes
    var icon: Drawable? = null

    var iconSize: Float
        @Dimension get() = _iconSize
        set(@Dimension value) {
            _iconSize = value
            invalidate()
        }

    var fabBackgroundColor: Int
        @ColorInt get() = _backgroundColor
        set(@ColorInt value) {
            _backgroundColor = value
            paintBackground.color = value
            invalidate()
        }

    // Paints
    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = fabBackgroundColor
    }

    // paths
    private val pathBackground = Path()

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DiamondFab,
            defStyleAttr,
            0
        )

        try {
            icon = typedArray.getDrawableOrThrow(R.styleable.DiamondFab_icon)
            iconSize = typedArray.getDimension(R.styleable.DiamondFab_iconSize, iconSize)
            fabBackgroundColor =
                typedArray.getColor(R.styleable.DiamondFab_backgroundColor, fabBackgroundColor)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    private fun recalculateBackgroundPath() {
        pathBackground.reset()

        pathBackground.moveTo(0f, height / 2f)
        pathBackground.lineTo(width / 2f, 0f)
        pathBackground.lineTo(width.toFloat(), height / 2f)
        pathBackground.lineTo(width / 2f, height.toFloat())
        pathBackground.lineTo(0f, height / 2f)

        pathBackground.close()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        recalculateBackgroundPath()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        canvas.drawPath(pathBackground, paintBackground)

        icon!!.setBounds(
            (width / 2 - iconSize).toInt(),
            (height / 2 - iconSize).toInt(),
            (width / 2 + iconSize).toInt(),
            (height / 2 + iconSize).toInt()
        )
        icon!!.draw(canvas)
    }


    companion object {
        private const val DEFAULT_SIZE = 56

        private const val DEFAULT_ICON_SIZE = 36f
        private const val DEFAULT_BACKGROUND_COLOR = "#61F4F6"
    }
}
