package com.virginiaprivacy.rvalerts

import kotlinx.serialization.Serializable

@Serializable
data class CallAlert(
    private val elements: List<String>,
    val timeReceived: String = elements[0],
    val agency: Agency = parseAgency(elements[1]), val dispatchArea: DispatchArea = parseDispatchArea(elements[2]),
    val unit: String = elements[3], val callType: String = elements[4], val location: String = elements[5],
    val callStatus: CallStatus = parseCallStatus(elements[6])
) : java.io.Serializable


interface ParsedItem : java.io.Serializable {
    val text: String

}

@Serializable
enum class DispatchArea(override val text: String) : ParsedItem {
    FIRE("FIRE"),
    Precinct1("Precinct 1"),
    Precinct2("Precinct 2"),
    Precinct3("Precinct 3"),
    Precinct4("Precinct 4"),
    Unknown("Unknown");
}

fun parseDispatchArea(text: String): DispatchArea =
    DispatchArea.values().firstOrNull { it.text.lowercase() == text.trim().lowercase() } ?: DispatchArea.Unknown


@Serializable
enum class CallStatus(override val text: String) : ParsedItem {
    Dispatched("Dispatched"),
    Arrived("Arrived"),
    Enroute("Enroute"),
    Unknown("Unknown");
}

fun parseCallStatus(text: String): CallStatus =
    CallStatus.values().firstOrNull { it.text.lowercase() == text.trim().lowercase() } ?: CallStatus.Unknown

@Serializable
enum class Agency(override val text: String) : ParsedItem {
    RPD("RPD"),
    RFD("RFD"),
    Unknown("Unknown");
}

fun parseAgency(text: String): Agency = Agency.values().firstOrNull { it.text.lowercase() == text.trim().lowercase() } ?: Agency.Unknown