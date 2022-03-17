package com.virginiaprivacy.rvalerts

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CallScraper {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private suspend fun parseDocAsync(): Document {
        return suspendCoroutine<Document> {
            try {
                it.resume(Jsoup.connect(RVA_URL).get())
            } catch (e: Throwable) {
                it.resumeWithException(e)
            }
        }
    }

    val activeCallsState = MutableStateFlow(ActiveCalls(System.currentTimeMillis(), emptyList()))

    internal suspend fun parseCalls(): List<CallAlert>? {
        val doc = parseDocAsync()
            .getElementsByTag("tbody")
            .firstOrNull()
            ?.getElementsByTag("tr")

        return doc?.let {
            return@let getCalls(doc)
        }
    }

    fun updateActiveCallsAsync() {
        ioScope.launch {
            activeCallsState.update {
                ActiveCalls(System.currentTimeMillis(), parseCalls() ?: emptyList())
            }
        }
    }

    suspend fun updateActiveCalls() {
        activeCallsState.update {
            ActiveCalls(System.currentTimeMillis(), parseCalls() ?: emptyList())
        }
    }

    private fun getCalls(activeCalls: Elements): List<CallAlert> {
        val calls = activeCalls.map { it.getElementsByTag("td") }
        return calls.map {
            CallAlert(it.map { it.text().trim() })
        }
    }

    companion object {
        const val RVA_URL = "https://apps.richmondgov.com/applications/activecalls/Home/ActiveCalls?"
    }
}
