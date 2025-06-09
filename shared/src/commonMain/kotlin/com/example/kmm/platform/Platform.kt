package com.example.kmm.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform