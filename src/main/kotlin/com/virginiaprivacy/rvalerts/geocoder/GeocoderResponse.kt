package com.virginiaprivacy.rvalerts.geocoder

import kotlinx.serialization.Serializable

@Serializable sealed interface Response: java.io.Serializable

@Serializable
object InvalidAddressResponse : Response
@Serializable
data class GeocoderResponse(val lat: Double, val lon: Double, val address: Address) : Response {
    companion object {

    }
}
