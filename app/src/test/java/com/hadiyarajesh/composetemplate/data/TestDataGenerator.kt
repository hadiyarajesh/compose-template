package com.hadiyarajesh.composetemplate.data

import com.hadiyarajesh.composetemplate.data.database.entity.Image
import kotlin.random.Random

object TestDataGenerator {
    fun getRandomImage(): Image {
        val randomId = Random.nextLong(0, 100)

        return Image(
            imageId = randomId,
            url = "https://example.com",
            "Image $randomId description",
            altText = "Image $randomId Alt text"
        )
    }
}
