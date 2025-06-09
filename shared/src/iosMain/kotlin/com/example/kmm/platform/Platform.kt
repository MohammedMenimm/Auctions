// File: iosMain/com/example/kmm/platform/Platform.kt
package com.example.kmm.platform

class IOSPlatform : Platform {
    override val name: String = "iOS"
}

actual fun getPlatform(): Platform = IOSPlatform()
