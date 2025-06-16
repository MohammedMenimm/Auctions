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
            println("DEBUG: Raw JSON response = $raw")

            val products = response.body<List<Product>>()
            println("DEBUG: Parsed products = $products")

            return products
        } catch (e: Exception) {
            println("DEBUG: Exception while fetching or parsing: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}