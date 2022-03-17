package com.virginiaprivacy.rvalerts.geocoder

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Address(
    @SerialName("house_number")
    val houseNumber: String = "-1",

    val road: String,
    val city: String = "",
    val state: String,
    @SerialName("postcode")
    val postCode: Int,
    @SerialName("country_code")
    val countryCode: String
) {
    companion object {

    }
}
