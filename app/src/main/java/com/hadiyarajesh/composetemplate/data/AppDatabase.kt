package com.hadiyarajesh.composetemplate.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hadiyarajesh.composetemplate.data.dao.MessageDao
import com.hadiyarajesh.composetemplate.data.dao.UserDao
import com.hadiyarajesh.composetemplate.data.entity.Message
import com.hadiyarajesh.composetemplate.data.entity.User

@Database(
    version = 1,
    entities = [User::class, Message::class],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val messageDao: MessageDao
}
