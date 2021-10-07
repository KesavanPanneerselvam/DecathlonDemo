package com.dreamforall.decathlondemo.network

import com.dreamforall.decathlondemo.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private const val CONNECTION_TIMEOUT: Long = 20
private const val READ_TIMEOUT: Long = 30
private const val WRITE_TIMEOUT: Long = 20

class NetworkModule(
    baseUrl: String,
    apiInterceptors: Array<out Interceptor> = arrayOf(BaseInterceptor()),
) {

    private val retrofit: Retrofit

    init {
        retrofit = buildRetrofit(baseUrl, buildOkHttpClient(apiInterceptors))
    }

    fun <T> getApiInstance(apiServiceClass: Class<T>): T = retrofit.create(apiServiceClass)

    private fun buildOkHttpClient(apiInterceptors: Array<out Interceptor>): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()

        val logger = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG)
            logger.level = HttpLoggingInterceptor.Level.BODY

        for (interceptor in apiInterceptors) {
            okHttpBuilder.addInterceptor(interceptor)
        }

        okHttpBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        okHttpBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        okHttpBuilder.addInterceptor(logger)

        return okHttpBuilder.build()
    }

    private fun buildRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

}