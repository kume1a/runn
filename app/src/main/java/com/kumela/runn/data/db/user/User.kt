package com.kumela.runn.data.db.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kumela.views.enums.Gender

@Entity(tableName = "users")
data class User(
    var gender: Gender,
    var weight: Int,
    var height: Int,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}