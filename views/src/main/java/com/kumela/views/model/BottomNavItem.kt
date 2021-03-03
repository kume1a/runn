package com.kumela.views.model

import android.graphics.RectF
import android.graphics.drawable.Drawable

internal data class BottomNavItem(
    var title: String,
    val icon: Drawable,
    var rect: RectF = RectF(),
    var alpha: Int = 0
)