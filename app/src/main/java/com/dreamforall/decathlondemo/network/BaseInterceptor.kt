package com.dreamforall.decathlondemo.network

import okhttp3.Interceptor
import okhttp3.Response

class BaseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        chain.let {
            val originalRequest = chain.request()
            val currentRequest =
                when (originalRequest.header(ACCEPT_HEADER_KEY) == null ||
                        originalRequest.header(CONTENT_TYPE_HEADER_KEY) == null) {
                    true ->
                        originalRequest.newBuilder()
                            .addHeader(
                                ACCEPT_HEADER_KEY,
                                ACCEPT_HEADER_VALUE
                            )
                            .addHeader(
                                CONTENT_TYPE_HEADER_KEY,
                                CONTENT_TYPE_VALUE_KEY
                            )
                            .build()

                    false -> originalRequest
                }
            return chain.proceed(currentRequest)
        }
    }

    companion object {
        const val ACCEPT_HEADER_KEY = "Accept"
        const val ACCEPT_HEADER_VALUE = "application/json"
        const val CONTENT_TYPE_HEADER_KEY = "Content-Type"
        const val CONTENT_TYPE_VALUE_KEY = "application/json"
        const val AUTH_HEADER_KEY = "Authorization"
    }
}