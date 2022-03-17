package com.virginiaprivacy.rvalerts

data class ActiveCalls(val timeScraped: Long, val calls: List<CallAlert>): java.io.Serializable {
    fun isEmpty(): Boolean = calls.isEmpty()
}
