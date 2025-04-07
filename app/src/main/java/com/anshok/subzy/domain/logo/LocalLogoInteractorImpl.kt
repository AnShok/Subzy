package com.anshok.subzy.domain.logo

import com.anshok.subzy.data.local.impl.EmbeddedLogoProvider
import com.anshok.subzy.domain.logo.model.Logo

class LocalLogoInteractorImpl : LocalLogoInteractor {
    override fun getAllLogos(): List<Logo> {
        return EmbeddedLogoProvider.getAll().map {
            Logo(it.name, it.domain, logoResId = it.logoResId)
        }
    }
}