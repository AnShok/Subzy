package com.anshok.subzy.domain.rate

import android.content.Intent

interface RateRepository {
    fun getGooglePlayIntent(): Intent
}

