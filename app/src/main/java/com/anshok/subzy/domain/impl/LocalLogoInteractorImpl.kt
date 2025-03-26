package com.anshok.subzy.domain.impl

import com.anshok.subzy.data.local.impl.EmbeddedLogoProvider
import com.anshok.subzy.domain.api.LocalLogoInteractor
import com.anshok.subzy.domain.model.Logo

class LocalLogoInteractorImpl : LocalLogoInteractor {
    override fun getAllLogos(): List<Logo> {
        return EmbeddedLogoProvider.getAll().map {
            Logo(it.name, it.domain, logoResId = it.logoResId)
        }
    }
}