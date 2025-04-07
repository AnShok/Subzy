package com.anshok.subzy.domain.logo

import com.anshok.subzy.domain.logo.model.Logo

interface LocalLogoInteractor {
    fun getAllLogos(): List<Logo>
}
