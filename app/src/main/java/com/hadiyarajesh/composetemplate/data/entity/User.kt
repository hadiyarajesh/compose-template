package com.hadiyarajesh.composetemplate.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val userId: Long,
    val username: String
)
