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

// AuctionViewModel.kt

    fun placeLocalBid(productId: String, newBid: Double) {
        val productIdInt = productId.toIntOrNull() ?: return // säkert avbryt om det inte går att konvertera

        val updatedList = _items.value.map {
            if (it.id == productIdInt && newBid > it.currentBid) {
                it.copy(currentBid = newBid)
            } else it
        }
        _items.value = updatedList
    }


}