package com.hadiyarajesh.composetemplate.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hadiyarajesh.composetemplate.data.database.entity.Image
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Upsert
    suspend fun insertOrUpdateImage(image: Image): Long

    /**
     * As we're using Kotlin CodeGen for Room, we need to mark [Image] as nullable.
     * Refer @link https://developer.android.com/jetpack/androidx/releases/room#2.6.0 for more info.
     */
    @Query("SELECT * FROM Image LIMIT 1")
    fun getImages(): Flow<Image?>
}
