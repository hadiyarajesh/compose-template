package com.hadiyarajesh.composetemplate.utility

import kotlin.random.Random

/**
 * Utility object for generating random image URLs from the [picsum.photos](https://picsum.photos) service.
 */
object ImageUtility {
    private const val IMAGE_WIDTH = "720"
    private const val IMAGE_HEIGHT = "720"

    /**
     * Returns a random image ID between 1 and 200 (inclusive).
     */
    private val randomImageId: Int
        get() = Random.nextInt(1, 201)

    fun getRandomImageUrl(): String {
        return "https://picsum.photos/id/${randomImageId}/${IMAGE_WIDTH}/${IMAGE_HEIGHT}"
    }
}
