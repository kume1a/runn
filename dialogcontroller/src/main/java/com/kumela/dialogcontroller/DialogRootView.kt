package com.kumela.dialogcontroller

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.core.math.MathUtils.clamp

class DialogRootView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
) : ViewGroup(context, attrs, defStyleAttr), DialogRoot {

    private var backgroundDimAmount: Float = 0f
    internal var dialog: DialogController? = null
    private var content: View? = null
    var cancellableOnTouchOutside: Boolean = false
    private var dialogWidth: Int = 0

    init {
        dialogWidth = context.resources.getDimensionPixelSize(R.dimen.dialog_width)
        backgroundDimAmount = ResourceUtils.getAttrFloat(context, android.R.attr.backgroundDimAmount)
        backgroundDimAmount = clamp(backgroundDimAmount, 0.0f, 1.0f)
        val alpha = (255 * backgroundDimAmount).toInt()
        setBackgroundColor(Color.argb(alpha, 0, 0, 0))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        ensureContent()
        val content = this.content!!
        val contentWidth = content.measuredWidth
        val contentHeight = content.measuredHeight
        val left = (width - contentWidth) / 2
        val top = (height - contentHeight) / 2
        content.layout(left, top, left + contentWidth, top + contentHeight)
    }

    override fun getDialogContent(): View {
        ensureContent()
        return content!!
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (cancellableOnTouchOutside && event != null && event.actionMasked == MotionEvent.ACTION_DOWN) {
            ensureContent()
            if (!isUnderView(content!!, event)) {
                dialog?.cancel()
            }
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        ensureContent()
        val content = this.content!!
        var baseWidth = dialogWidth
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val lp = content.layoutParams

        val resizeWidth: Boolean
        var childWidthMeasureSpec: Int
        if (lp.width == LayoutParams.WRAP_CONTENT && (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY) && widthSize > baseWidth) {
            resizeWidth = true
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(baseWidth, MeasureSpec.AT_MOST)
        } else {
            resizeWidth = false
            childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width)
        }
        val childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height)
        content.measure(childWidthMeasureSpec, childHeightMeasureSpec)

        if (resizeWidth && (content.measuredWidthAndState and View.MEASURED_STATE_TOO_SMALL) != 0) {
            baseWidth = (baseWidth + widthSize) / 2
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(baseWidth, MeasureSpec.AT_MOST)
            content.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        }

        if (resizeWidth && content.measuredWidthAndState and MEASURED_STATE_TOO_SMALL != 0) {
            childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width)
            content.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        }

        val width: Int = getMeasuredDimension(widthMeasureSpec, content.measuredWidth)
        val height: Int = getMeasuredDimension(heightMeasureSpec, content.measuredHeight)
        setMeasuredDimension(width, height)
    }

    override fun getBackgroundDimAmount(): Float = backgroundDimAmount

    private fun ensureContent() {
        if (content == null) {
            if (childCount == 0) {
                throw IllegalStateException("DialogRoot should contain a DialogContent")
            }
        }
        content = getChildAt(0)
    }

    private fun isUnderView(view: View, event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        return x >= view.left && x <= view.right && y >= view.top && y <= view.bottom
    }

    private fun getMeasuredDimension(spec: Int, childDimension: Int): Int {
        val size = MeasureSpec.getSize(spec)
        val mode = MeasureSpec.getMode(spec)
        return if (mode == MeasureSpec.EXACTLY || mode == MeasureSpec.AT_MOST) {
            size
        } else {
            childDimension
        }
    }
}