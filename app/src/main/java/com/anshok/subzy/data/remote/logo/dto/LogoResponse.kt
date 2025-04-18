package com.anshok.subzy.data.remote.logo.dto

import com.google.gson.annotations.SerializedName

data class LogoResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("domain") val domain: String?,
    @SerializedName("logo_url") val logoUrl: String?,
    var resultCode: Int = 0
)
