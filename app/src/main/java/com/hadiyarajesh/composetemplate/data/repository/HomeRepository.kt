package com.hadiyarajesh.composetemplate.data.repository

import com.hadiyarajesh.composetemplate.data.database.dao.ImageDao
import com.hadiyarajesh.composetemplate.data.database.entity.Image
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository interface for handling home screen data operations.
 *
 * Designed to abstract data access for the home UI, such as fetching and updating
 * the current [Image]. Implementation may interact with local database, network,
 * or both depending on the app's architecture.
 */
interface HomeRepository {
    /**
     * Returns a [Flow] that emits the current [Image], if available.
     *
     * This can be collected to observe changes to the image data.
     */
    fun loadData(): Flow<Image?>

    /**
     * Updates or replaces the current [Image].
     */
    suspend fun changeImage(image: Image)
}

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val imageDao: ImageDao
) : HomeRepository {
    override fun loadData(): Flow<Image?> {
        return imageDao.getImages()
    }

    override suspend fun changeImage(image: Image) {
        imageDao.insertOrUpdateImage(image)
    }
}
