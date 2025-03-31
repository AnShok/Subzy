package com.anshok.subzy.presentation.settings.viewmodel

import androidx.lifecycle.ViewModel

class EasterEggViewModel : ViewModel() {

    private val _scenario = listOf(
        "👀 Привет, разработчик! Ты опять проверяешь иконку?",
        "🤖 Хочешь я покажу тебе кое-что секретное?",
        "✨ Вот, держи немного магии.\n Смотри на иконку ⬆\uFE0F",
        "P.S. Никому не рассказывай про это 🫢"
    )

    val messages: List<String> get() = _scenario
}
