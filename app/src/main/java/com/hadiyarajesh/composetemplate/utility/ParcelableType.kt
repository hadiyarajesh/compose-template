package com.hadiyarajesh.composetemplate.utility

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * Creates a custom [NavType] for a [Parcelable] and [@Serializable] type `T`.
 *
 * This allows you to pass complex data objects through navigation in a type-safe way
 * by serializing/deserializing the object as a JSON string.
 *
 * The type `T` must be both `@Parcelize` and `@Serializable`.
 *
 * ### Example:
 * ```kotlin
 * @Parcelize
 * @Serializable
 * data class Image(val id: Long, val url: String) : Parcelable
 *
 * val imageNavType = parcelableType<Image>()
 * ```
 *
 * You can then use this in `NavGraphBuilder.composable()` when defining your route.
 *
 * @param isNullableAllowed Whether the value can be null.
 * @param json The [Json] instance to use for (de)serialization.
 * @return A [NavType] that supports storing and retrieving type `T` using a Bundle and JSON.
 */
inline fun <reified T : Parcelable> parcelableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json
): NavType<T> {
    val kSerializer: KSerializer<T> = serializer()

    return object : NavType<T>(isNullableAllowed) {
        override fun get(bundle: Bundle, key: String): T? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, T::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(key)
            }
        }

        override fun parseValue(value: String): T {
            return json.decodeFromString(kSerializer, value)
        }

        override fun serializeAsValue(value: T): String {
            return Uri.encode(json.encodeToString(kSerializer, value))
        }

        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putParcelable(key, value)
        }
    }
}
