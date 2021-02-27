package com.kumela.runn.data.db

import androidx.room.TypeConverter
import com.kumela.runn.core.enums.Gender

class Converters {
    @TypeConverter
    fun toGender(value: Int) = enumValues<Gender>()[value]

    @TypeConverter
    fun fromGender(value: Gender) = value.ordinal
}