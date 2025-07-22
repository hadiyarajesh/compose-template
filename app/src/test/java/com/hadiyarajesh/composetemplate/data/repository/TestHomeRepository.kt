package com.hadiyarajesh.composetemplate.data.repository

import com.hadiyarajesh.composetemplate.data.database.entity.Image
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class TestHomeRepository : HomeRepository {
    var throwError: Boolean = false
    var imagesToEmit: List<Image> = emptyList()

    override fun loadData(): Flow<Image?> = flow {
        if (throwError) {
            throw RuntimeException("Test Exception!!!")
        }

        imagesToEmit.forEach { emit(it) }
    }

    override suspend fun changeImage(image: Image) {
        // no-op
    }
}
