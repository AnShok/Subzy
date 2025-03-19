package com.anshok.subzy.data.remote.search.dto

data class LogoResponse(
    val name: String? = null,
    val domain: String? = null,
    val logoUrl: String? = null
) : Response() {
    override var resultCode: Int = 0 // Используем override
}