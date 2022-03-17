package com.virginiaprivacy.rvalerts.geocoder

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.json.Json
import java.net.URL
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.nio.charset.Charset
import java.time.Duration
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object Geocoder {

    private const val apiUrl = "https://nominatim.openstreetmap.org/search?q="
    private val client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(Duration.ofSeconds(10L)).build()
    private val requestBuilder = HttpRequest.newBuilder().GET().setHeader("User-Agent", "com.virginiaprivacy.rvaalerts")
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private fun getRequest(url: String) = requestBuilder.copy().uri(URL(url).toURI()).build()
    private fun getFullUrl(address: String) = "$apiUrl$address&format=json&addressdetails=1"
    private fun appendCityState(address: String): String = URLEncoder.encode("$address RICHMOND, VA", Charset.forName("UTF-8"))
    @ExperimentalSerializationApi
    suspend fun geocodeString(address: String): Response {
        val result = suspendCancellableCoroutine<Response> { cont ->
            val fullAddress  = appendCityState(address = if (address.contains("-BLK")) address.replace("-BLK", "") else address)
            val request = getRequest(getFullUrl(fullAddress))
            val handler = BodyHandlers.ofString()
            val body = client.send(request, handler).body()
                if (body.isNullOrBlank()) InvalidAddressResponse
            try {
                val response = json.decodeFromString(
                    deserializer = ArraySerializer(GeocoderResponse.serializer()),
                    string = body
                ).firstOrNull() ?: InvalidAddressResponse
                cont.resume(response)
            } catch (e: Throwable) {
                cont.resumeWithException(e)
            }
        }
        return result
    }
}




