package com.virginiaprivacy.rvalerts.geocoder

import com.virginiaprivacy.rvalerts.CallAlert

@kotlinx.serialization.Serializable
data class GeocodedCall(val call: CallAlert, val geodata: GeocoderResponse)
