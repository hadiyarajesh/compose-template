package com.hadiyarajesh.composetemplate.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hadiyarajesh.composetemplate.data.entity.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Upsert
    suspend fun insertOrUpdateMessage(message: Message): Long

    @Query("SELECT * FROM Message LIMIT 1")
    fun getMessage(): Flow<Message>
}
