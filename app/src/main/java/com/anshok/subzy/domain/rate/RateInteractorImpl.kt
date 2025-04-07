package com.anshok.subzy.domain.rate

import android.content.Intent

class RateInteractorImpl(
    private val repository: RateRepository
) : RateInteractor {
    override fun getGooglePlayIntent(): Intent = repository.getGooglePlayIntent()
}

