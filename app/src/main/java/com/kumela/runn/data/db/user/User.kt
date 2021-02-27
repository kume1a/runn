package com.kumela.runn.data.db.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kumela.runn.core.enums.Gender

@Entity(tableName = "users")
data class User(
    var gender: Gender,
    var weight: Int,
    var height: Int,
    @PrimaryKey var id: Int = 0
)