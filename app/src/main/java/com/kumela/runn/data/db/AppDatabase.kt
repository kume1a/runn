package com.kumela.runn.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kumela.runn.data.db.run.RunSession
import com.kumela.runn.data.db.run.RunSessionDao
import com.kumela.runn.data.db.user.User
import com.kumela.runn.data.db.user.UserDao

@Database(entities = [User::class, RunSession::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun runSessionDao(): RunSessionDao
}