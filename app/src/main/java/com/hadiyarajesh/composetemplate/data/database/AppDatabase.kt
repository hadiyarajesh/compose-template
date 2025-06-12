package com.hadiyarajesh.composetemplate.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hadiyarajesh.composetemplate.data.database.dao.ImageDao
import com.hadiyarajesh.composetemplate.data.database.entity.Image

@Database(
    version = 1,
    entities = [Image::class],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}
