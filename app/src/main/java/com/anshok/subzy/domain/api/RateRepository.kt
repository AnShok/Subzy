package com.anshok.subzy.domain.api

import android.content.Intent

interface RateRepository {
    fun getGooglePlayIntent(): Intent
}

