package com.anshok.subzy.domain.api

import com.anshok.subzy.domain.model.Logo

interface LocalLogoInteractor {
    fun getAllLogos(): List<Logo>
}
