package com.anshok.subzy.domain.impl

import android.content.Intent
import com.anshok.subzy.domain.api.RateInteractor
import com.anshok.subzy.domain.api.RateRepository

class RateInteractorImpl(
    private val repository: RateRepository
) : RateInteractor {
    override fun getGooglePlayIntent(): Intent = repository.getGooglePlayIntent()
}

