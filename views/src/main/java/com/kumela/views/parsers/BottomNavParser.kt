package com.kumela.views.parsers

import android.content.Context
import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.graphics.drawable.Drawable
import androidx.annotation.XmlRes
import androidx.core.content.ContextCompat
import com.kumela.views.model.BottomNavItem

internal class BottomNavParser(private val context: Context, @XmlRes res: Int) :
    XmlMenuParser<BottomNavItem>(context, res) {

    override fun parseItem(parser: XmlResourceParser): BottomNavItem {
        val attributeCount = parser.attributeCount
        var itemText: String? = null
        var itemDrawable: Drawable? = null

        for (index in 0 until attributeCount) {
            when (parser.getAttributeName(index)) {
                ICON_ATTRIBUTE ->
                    itemDrawable = ContextCompat.getDrawable(
                        context,
                        parser.getAttributeResourceValue(index, 0)
                    )
                TITLE_ATTRIBUTE -> {
                    itemText = try {
                        context.getString(parser.getAttributeResourceValue(index, 0))
                    } catch (notFoundException: Resources.NotFoundException) {
                        parser.getAttributeValue(index)
                    }
                }
            }
        }

        if (itemDrawable == null)
            throw NullPointerException("Item icon can not be null!")

        return BottomNavItem(itemText ?: "", itemDrawable)
    }

    companion object {
        private const val ICON_ATTRIBUTE = "icon"
        private const val TITLE_ATTRIBUTE = "title"
    }
}