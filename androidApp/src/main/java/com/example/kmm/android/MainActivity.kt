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
                        Column(modifier = Modifier.fillMaxSize()) {
                            when {
                                error != null -> {
                                    Text("Error: $error", color = Color.Red)
                                }
                                selectedProduct != null -> {
                                    val item = selectedProduct!!

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(bottom = 12.dp)
                                            .clickable { selectedProduct = null }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Tillbaka",
                                            tint = Color.Black
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text("Tillbaka", color = Color.Black)
                                    }
                                    Text("Produktnamn: ${item.name}", style = MaterialTheme.typography.titleMedium)
                                    Spacer(Modifier.height(8.dp))
                                    Text("Beskrivning: ${item.description ?: "Ingen beskrivning"}")
                                    Spacer(Modifier.height(8.dp))
                                    Text("Pris: ${item.currentBid} kr")
                                    Text("Kommun: ${item.municipalityName}")
                                }
                                products.isEmpty() -> {
                                    Text("Laddar produkter…")
                                }
                                else -> {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(products) { item ->
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { selectedProduct = item }
                                                    .padding(vertical = 8.dp)
                                            ) {
                                                Text(
                                                    text = item.name,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = Color.Black
                                                )
                                                Divider()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}