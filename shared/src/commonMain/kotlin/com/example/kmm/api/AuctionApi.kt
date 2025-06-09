package com.example.kmm.api

import Product
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText

class AuctionApi(
    private val httpClient: HttpClient,
    private val apiKey: String
) {
    suspend fun fetchProducts(): List<Product> {
        try {
            val response = httpClient.get("https://app.klaravik.dev/dev-test-api/products") {
                header("X-API-Key", apiKey)
            }
            val raw = response.bodyAsText()
            println("DEBUG: Raw JSON response = $raw") // <--- 1. See what the API sent you

            val products = response.body<List<Product>>() // <--- 2. Try to parse it
            println("DEBUG: Parsed products = $products") // <--- 3. See what you actually got

            return products
        } catch (e: Exception) {
            println("DEBUG: Exception while fetching or parsing: ${e.message}")
            e.printStackTrace() // <--- 4. Stacktrace in case of error
            throw e // Let your ViewModel catch and display the error
        }
    }
}