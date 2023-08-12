package com.hadiyarajesh.composetemplate.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hadiyarajesh.composetemplate.data.entity.User

@Dao
interface UserDao {
    @Upsert
    suspend fun insertOrUpdateUser(user: User): Long

    @Query("SELECT * FROM User WHERE userId = :userId")
    suspend fun getUser(userId: Long): User
}
