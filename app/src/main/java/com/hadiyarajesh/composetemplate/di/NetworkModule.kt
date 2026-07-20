package com.hadiyarajesh.composetemplate.di

import com.hadiyarajesh.composetemplate.BuildConfig
import com.hadiyarajesh.composetemplate.utility.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/**
 * Dagger-Hilt module that provides network-related dependencies.
 *
 * This module includes:
 * - A singleton instance of [OkHttpClient] with conditional logging in debug builds.
 * - A singleton instance of [Json] for kotlinx.serialization (de)serialization.
 * - A singleton instance of [Retrofit] configured with [OkHttpClient] and [Json].
 *
 * Annotated with [@InstallIn(SingletonComponent::class)] to ensure the
 * provided instances live as long as the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private fun getLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient
        .Builder()
        .retryOnConnectionFailure(true)
        .also { okHttpClient ->
            // Only add HttpLoggingInterceptor on debug builds.
            if (BuildConfig.DEBUG) {
                okHttpClient.addInterceptor(getLoggingInterceptor())
            }
        }.build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit = Retrofit
        .Builder()
        .baseUrl(Constants.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}
