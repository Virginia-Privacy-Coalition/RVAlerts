package com.virginiaprivacy.rvalerts

import com.virginiaprivacy.rvalerts.geocoder.ClientGeocoder
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.Test
import io.kotest.matchers.ints.shouldBeGreaterThan

@OptIn(ExperimentalKotest::class)
@Test
class ClientGeocoderTests : FunSpec({
    test("ClientGeocoder test") {
        val calls = ClientGeocoder.getGeocodedCalls()
        calls.size shouldBeGreaterThan 0

    }

    test("Raw calls list test") {
        shouldNotThrowAny {
            val callsList = ClientGeocoder.getCallsList()
            callsList.size shouldBeGreaterThan 0
        }
    }

//    test("Server geocoder test") {
//        val response = Geocoder.geocodeString("1000 west b st")
//        response::class shouldNotBe InvalidAddressResponse
//        (response as GeocoderResponse) shouldNotBe null
//    }
})