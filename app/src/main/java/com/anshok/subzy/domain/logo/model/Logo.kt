package com.anshok.subzy.domain.logo.model

import androidx.annotation.DrawableRes

data class Logo(
    val name: String?,
    val domain: String?,
    val logoUrl: String? = null,
    @DrawableRes val logoResId: Int? = null
)
