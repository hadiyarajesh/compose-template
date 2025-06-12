package com.hadiyarajesh.composetemplate.data.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity
data class Image(
    @PrimaryKey(autoGenerate = true)
    val imageId: Long = 0,
    val url: String,
    val description: String,
    val altText: String
) : Parcelable
