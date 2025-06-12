package com.hadiyarajesh.composetemplate.di

import com.hadiyarajesh.composetemplate.BuildConfig
import com.hadiyarajesh.composetemplate.utility.Constants
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Dagger-Hilt module that provides network-related dependencies.
 *
 * This module includes:
 * - A singleton instance of [OkHttpClient] with conditional logging in debug builds.
 * - A singleton instance of [Moshi] for JSON parsing.
 * - A singleton instance of [Retrofit] configured with [OkHttpClient] and [Moshi].
 *
 * Annotated with [@InstallIn(SingletonComponent::class)] to ensure the
 * provided instances live as long as the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .also { okHttpClient ->
                /**
                 * Only add [HttpLoggingInterceptor] on debug build
                 */
                if (BuildConfig.DEBUG) {
                    okHttpClient.addInterceptor(getLoggingInterceptor())
                }
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}
