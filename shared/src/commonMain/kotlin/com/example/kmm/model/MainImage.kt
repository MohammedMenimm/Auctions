package com.example.kmm.model

import kotlinx.serialization.Serializable

@Serializable
data class MainImage(
    val imageUrlThumb: String,
    val imageUrlLarge: String
)