package com.anshok.subzy.data.local.impl

import android.content.Context
import com.anshok.subzy.data.local.models.EmbeddedLogo
import com.anshok.subzy.data.local.models.EmbeddedLogoJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object EmbeddedLogoProvider {

    private var embeddedLogos: List<EmbeddedLogo> = emptyList()

    fun init(context: Context) {
        val json = context.assets.open("logos.json").bufferedReader().use { it.readText() }

        val logoJsonList: List<EmbeddedLogoJson> = Gson().fromJson(
            json,
            object : TypeToken<List<EmbeddedLogoJson>>() {}.type
        )

        embeddedLogos = logoJsonList.map { jsonItem ->
            val resId =
                context.resources.getIdentifier(jsonItem.resName, "drawable", context.packageName)
            EmbeddedLogo(
                name = jsonItem.name,
                domain = jsonItem.domain,
                logoResId = if (resId != 0) resId else null
            )
        }
    }

    fun getAll(): List<EmbeddedLogo> = embeddedLogos

    fun search(query: String): List<EmbeddedLogo> {
        val lowerQuery = query.lowercase()
        return embeddedLogos.filter {
            it.name.lowercase().contains(lowerQuery) || it.domain.lowercase().contains(lowerQuery)
        }
    }
}
