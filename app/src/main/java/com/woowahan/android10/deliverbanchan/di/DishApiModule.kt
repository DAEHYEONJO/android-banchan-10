package com.woowahan.android10.deliverbanchan.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.woowahan.android10.deliverbanchan.BuildConfig
import com.woowahan.android10.deliverbanchan.data.remote.dao.DishApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DishApiModule {

    private const val BASE_URL = "https://api.codesquad.kr/"

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun providesHeaderInterceptor() = Interceptor { chain ->
        with(chain) {
            val request = request().newBuilder()
                .addHeader("Accept", "application/json")
                .build()
            proceed(request)
        }
    }

    @Provides
    @Singleton
    fun providesLoggerInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        headerInterceptor: Interceptor,
        loggerInterceptor: HttpLoggingInterceptor
    ) =
        OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggerInterceptor)
            .build()

    @Provides
    @Singleton
    fun providesConvertorFactory() = json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient, converterFactory: Converter.Factory) =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

    @Provides
    @Singleton
    fun providesDishApi(retrofit: Retrofit) = retrofit.create(DishApi::class.java)

}