package com.hadiyarajesh.composetemplate.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hadiyarajesh.composetemplate.data.dao.MessageDao
import com.hadiyarajesh.composetemplate.data.entity.Message

@Database(
    version = 1,
    entities = [Message::class],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}
