package com.hadiyarajesh.composetemplate.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hadiyarajesh.composetemplate.data.entity.User

@Dao
interface UserDao {
    @Upsert
    fun insertOrUpdateUser(user: User)

    @Query("SELECT * FROM User WHERE userId = :userId")
    fun getUser(userId: Long): User
}
