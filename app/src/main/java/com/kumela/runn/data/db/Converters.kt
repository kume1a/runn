package com.kumela.runn.data.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kumela.views.enums.Gender
import java.io.ByteArrayOutputStream

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun toGender(value: Int) = enumValues<Gender>()[value]

    @TypeConverter
    fun fromGender(value: Gender) = value.ordinal


    @TypeConverter
    fun toBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }


    @TypeConverter
    fun toDoubleList(string: String): List<Double> {
        val type = object : TypeToken<List<Double>>() {}.type
        return gson.fromJson(string, type)
    }

    @TypeConverter
    fun fromDoubleList(list: List<Double>): String {
        return gson.toJson(list)
    }


    @TypeConverter
    fun toLatLngList(string: String): List<LatLng> {
        val type = object : TypeToken<List<LatLng>>() {}.type
        return gson.fromJson(string, type)
    }

    @TypeConverter
    fun fromLatLngList(list: List<LatLng>): String {
        return gson.toJson(list)
    }
}