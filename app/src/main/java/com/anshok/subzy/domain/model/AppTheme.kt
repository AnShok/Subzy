package com.anshok.subzy.domain.model

enum class AppTheme(val label: String) {
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System");

    companion object {
        fun fromName(name: String?): AppTheme {
            return values().find { it.name == name } ?: SYSTEM
        }
    }
}
