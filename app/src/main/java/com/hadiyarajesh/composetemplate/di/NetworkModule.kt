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

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .build()
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
            .addConverterFactory(MoshiConverterFactory.create(moshi)) // Register Moshi as a JSON converter for serialization and deserialization of objects.
            .also { retrofit ->
                /**
                 * Register {@link <a href="https://github.com/hadiyarajesh/flower">Flower</a>} as a response converter for supporting method return types other than {@code Call<T>}.
                 * Uncomment below line to add Flower support
                 */
//                retrofit.addCallAdapterFactory(FlowerCallAdapterFactory.create())
            }
            .build()
    }
}
