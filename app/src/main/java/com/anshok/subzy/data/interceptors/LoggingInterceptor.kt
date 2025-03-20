package com.anshok.subzy.data.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset

object LoggingInterceptor : Interceptor {
    private val charset: Charset = Charset.forName("UTF-8")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        logRequest(request)

        val response = chain.proceed(request)
        logResponse(response)

        return response
    }

    private fun logRequest(request: Request) {
        Log.d("LoggingInterceptor", "Request URL: ${request.url()}")
        request.body()?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            Log.d("LoggingInterceptor", "Request Body: ${buffer.readString(charset)}")
        }
    }

    private fun logResponse(response: Response) {
        Log.d("LoggingInterceptor", "Response Code: ${response.code()}")
        response.body()?.let { body ->
            val source = body.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer.clone()
            Log.d("LoggingInterceptor", "Response Body: ${buffer.readString(charset)}")
        }
    }
}