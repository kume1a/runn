package com.kumela.runn.data.db

import androidx.room.TypeConverter
import com.kumela.views.enums.Gender

class Converters {
    @TypeConverter
    fun toGender(value: Int) = enumValues<Gender>()[value]

    @TypeConverter
    fun fromGender(value: Gender) = value.ordinal
}