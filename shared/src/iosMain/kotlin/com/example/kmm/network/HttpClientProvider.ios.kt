package com.example.kmm.network

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

actual fun getHttpClient(): HttpClient = HttpClient(Darwin) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}