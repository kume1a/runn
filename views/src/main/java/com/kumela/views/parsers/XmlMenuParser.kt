package com.kumela.views.parsers

import android.content.Context
import android.content.res.XmlResourceParser
import androidx.annotation.XmlRes

internal abstract class XmlMenuParser<T>(context: Context, @XmlRes res: Int) {

    private val parser: XmlResourceParser = context.resources.getXml(res)

    fun parse(): List<T> {
        val items = mutableListOf<T>()
        var eventType: Int?

        do {
            eventType = parser.next()
            if (eventType == XmlResourceParser.START_TAG && parser.name == ITEM_TAG) {
                items.add(parseItem(parser))
            }
        } while (eventType != XmlResourceParser.END_DOCUMENT)

        return items
    }

    protected abstract fun parseItem(parser: XmlResourceParser): T

    companion object {
        private const val ITEM_TAG = "item"
    }
}