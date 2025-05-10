package com.hadiyarajesh.composetemplate.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Image(
    @PrimaryKey(autoGenerate = true)
    val imageId: Long = 0,
    val description: String,
    val altText: String,
    val url: String
)
