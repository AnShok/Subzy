package com.anshok.subzy.data.local.models

import androidx.annotation.DrawableRes

data class EmbeddedLogo(
    val name: String,
    val domain: String,
    @DrawableRes val logoResId: Int? = null
)

