package com.anshok.subzy.data.converters

import com.anshok.subzy.data.local.models.EmbeddedLogo
import com.anshok.subzy.domain.logo.model.Logo

object EmbeddedLogoMapper {
    fun mapToDomain(embeddedLogo: EmbeddedLogo): Logo {
        return Logo(
            name = embeddedLogo.name,
            domain = embeddedLogo.domain,
            logoResId = embeddedLogo.logoResId
        )
    }

    fun mapToDomainList(embeddedLogos: List<EmbeddedLogo>): List<Logo> {
        return embeddedLogos.map { mapToDomain(it) }
    }
}
