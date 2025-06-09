package com.example.kmm.viewmodel

import Product
import com.example.kmm.api.AuctionApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuctionViewModel(
    private val api: AuctionApi,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    private val _items = MutableStateFlow<List<Product>>(emptyList())
    val items: StateFlow<List<Product>> = _items
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchProducts() {
        scope.launch {
            try {
                _items.value = api.fetchProducts()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // Simulerad lokal bud-funktion
    fun placeBid(product: Product, amount: Int) {
        val updatedList = _items.value.map {
            if (it.id == product.id) it.copy(currentBid = amount)
            else it
        }
        _items.value = updatedList
    }
}