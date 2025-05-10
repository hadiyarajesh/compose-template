package com.hadiyarajesh.composetemplate.repository

import com.hadiyarajesh.composetemplate.data.dao.ImageDao
import com.hadiyarajesh.composetemplate.data.entity.Image
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface HomeRepository {
    fun loadData(): Flow<Image?>
}

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val imageDao: ImageDao
) : HomeRepository {
    override fun loadData(): Flow<Image?> {
        return imageDao.getImages()
    }
}
