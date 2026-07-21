package com.hadiyarajesh.composetemplate.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Image(
    @PrimaryKey(autoGenerate = true)
    val imageId: Long = 0,
    val url: String,
    val description: String,
    val altText: String
)
