package com.virginiaprivacy.rvalerts.geocoder

import com.virginiaprivacy.rvalerts.CallAlert
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.jvm.Throws


private const val API_BASE_URL = "api.virginiaprivacy.com"

object ClientGeocoder {

    private val client = OkHttpClient()

    private val json = Json {
        ignoreUnknownKeys = true
        this.coerceInputValues = true
        this.prettyPrint = true
    }

    private val mapJson = Json {
        ignoreUnknownKeys = true
        this.coerceInputValues = true
        this.prettyPrint = true
        this.allowStructuredMapKeys = true
    }
    private fun getCallsUrl(): HttpUrl {
        return HttpUrl.Builder().host(API_BASE_URL).scheme("https").addPathSegment("calls").build()
    }

    private fun getCallListUrl(): HttpUrl {
        return HttpUrl.Builder().host(API_BASE_URL).scheme("https").addPathSegment("calls").addPathSegment("list").build()
    }
    private fun getRequest(url: HttpUrl) = client.newCall(Request.Builder().url(url).build())
    @Throws(SerializationException::class, IOException::class) suspend fun getGeocodedCalls(): List<GeocodedCall> {
        val req = suspendCancellableCoroutine<List<GeocodedCall>> { continuation ->
            try {
                continuation.resume(getRequest(getCallsUrl()).execute().use { response ->
                    json.decodeFromString(ListSerializer(GeocodedCall.serializer()), response.body?.use { it.string() }?: "")
                })
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
        return req
    }

    @Throws(SerializationException::class, IOException::class) suspend fun getCallsList(): Map<CallAlert, Response> {
        val req = suspendCancellableCoroutine<Map<CallAlert, Response>> { continuation ->
            try {
                continuation.resume(getRequest(getCallListUrl()).execute().use { response ->
                    mapJson.decodeFromString(MapSerializer(CallAlert.serializer(), Response.serializer()), response.body?.use { it.string() }?: "")
                })
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
        return req
    }


}