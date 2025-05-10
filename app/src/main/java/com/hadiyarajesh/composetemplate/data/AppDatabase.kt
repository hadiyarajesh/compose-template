package com.hadiyarajesh.composetemplate.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hadiyarajesh.composetemplate.data.dao.ImageDao
import com.hadiyarajesh.composetemplate.data.entity.Image

@Database(
    version = 1,
    entities = [Image::class],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}
