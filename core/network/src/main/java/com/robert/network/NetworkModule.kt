package com.robert.network

import com.robert.network.api.CardApiService
import com.robert.network.datasource.CardRemoteDataSource
import com.robert.network.datasource.CardRemoteDataSourceImpl
import com.robert.network.interceptor.MalformedCardsPayloadInterceptor
import com.robert.network.sanitizer.MalformedCardsPayloadSanitizer
import com.robert.network.sanitizer.MalformedUserProfilePayloadSanitizer
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideMalformedCardsInterceptor(
        sanitizer: MalformedCardsPayloadSanitizer,
    ): MalformedCardsPayloadInterceptor = MalformedCardsPayloadInterceptor(sanitizer)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        malformedCardsPayloadInterceptor: MalformedCardsPayloadInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(NetworkConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NetworkConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NetworkConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)

        builder.addInterceptor(malformedCardsPayloadInterceptor)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        builder.addInterceptor(logging)

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideCardApiService(
        retrofit: Retrofit,
    ): CardApiService = retrofit.create(CardApiService::class.java)

    @Provides
    @Singleton
    fun provideCardRemoteDataSource(
        api: CardApiService,
        moshi: Moshi,
        cardsSanitizer: MalformedCardsPayloadSanitizer,
        userSanitizer: MalformedUserProfilePayloadSanitizer,
    ): CardRemoteDataSource = CardRemoteDataSourceImpl(api, moshi, cardsSanitizer, userSanitizer)

}
