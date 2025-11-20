package com.robert.network.interceptor

import com.robert.network.sanitizer.MalformedCardsPayloadSanitizer
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class MalformedCardsPayloadInterceptor @Inject constructor(
    private val sanitizer: MalformedCardsPayloadSanitizer,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!request.url.encodedPath.endsWith("getCards")) {
            return response
        }

        val body = response.body ?: return response
        val mediaType = body.contentType()
        val originalPayload = body.string()
        val normalizedPayload = sanitizer.normalize(originalPayload)

        return response.newBuilder()
            .body(normalizedPayload.toResponseBody(mediaType))
            .build()
    }

}

