package com.example.kmm.android

import Product
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kmm.api.AuctionApi
import com.example.kmm.viewmodel.AuctionViewModel
import com.example.kmm.network.getHttpClient
import kotlinx.coroutines.MainScope

class MainActivity : ComponentActivity() {
    private val apiKey = "97uionf98y34oiuh3498pfy34hf43hfp9834hf9p83h4fg8ogq3hfph9348ofhiu"
    private val viewModel = AuctionViewModel(AuctionApi(getHttpClient(), apiKey), MainScope())

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        SmallTopAppBar(
                            title = { Text("TBAuctions Produkter") },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = Color.White,
                                titleContentColor = Color.Black,
                            )
                        )
                    }
                ) { innerPadding ->
                    val products by viewModel.items.collectAsState()
                    val error by viewModel.error.collectAsState()
                    var selectedProduct by remember { mutableStateOf<Product?>(null) }

                    LaunchedEffect(Unit) { viewModel.fetchProducts() }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        when {
                            error != null -> ErrorScreen(error!!)
                            selectedProduct != null -> ProductDetailScreen(
                                product = products.firstOrNull { it.id == selectedProduct?.id },
                                onBack = { selectedProduct = null },
                                onPlaceBid = { productId, bid -> viewModel.placeLocalBid(productId, bid) }
                            )
                            products.isEmpty() -> LoadingScreen()
                            else -> ProductListScreen(products) { selectedProduct = it }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Error: $message", color = Color.Red)
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Laddar produkter…")
    }
}

@Composable
fun ProductListScreen(products: List<Product>, onProductSelected: (Product) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProductSelected(product) }
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Divider()
            }
        }
    }
}

@Composable
fun ProductDetailScreen(
    product: Product?,
    onBack: () -> Unit,
    onPlaceBid: (String, Double) -> Unit
) {
    if (product == null) {
        Text("Produkten hittades inte.")
        return
    }

    var newBidText by remember { mutableStateOf("") }
    val isBidValid = newBidText.toDoubleOrNull()?.let { it > product.currentBid } == true

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .clickable { onBack() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Tillbaka",
                tint = Color.Black
            )
            Spacer(Modifier.width(8.dp))
            Text("Tillbaka", color = Color.Black)
        }

        Text("Produktnamn: ${product.name}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text("Beskrivning: ${product.description ?: "Ingen beskrivning"}")
        Spacer(Modifier.height(8.dp))
        Text("Nuvarande bud: ${"%.2f".format(product.currentBid)} kr")
        Spacer(Modifier.height(8.dp))
        Text("Kommun: ${product.municipalityName}")
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = newBidText,
            onValueChange = { newBidText = it },
            label = { Text("Ditt bud i kr") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        
        Button(
            onClick = {
                onPlaceBid(product.id.toString(), newBidText.toDouble())
                newBidText = ""
            },
            enabled = isBidValid,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Lägg bud")
        }

        if (!isBidValid && newBidText.isNotEmpty()) {
            Text("Budet måste vara högre än nuvarande.", color = Color.Red)
        }
    }
}
