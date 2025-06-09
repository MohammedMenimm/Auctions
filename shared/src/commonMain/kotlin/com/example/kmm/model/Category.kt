package com.example.kmm.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int,
    val headline: String,
    val level: Int,
    val parentId: Int? = null
)