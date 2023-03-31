package com.example.on_rental_app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform