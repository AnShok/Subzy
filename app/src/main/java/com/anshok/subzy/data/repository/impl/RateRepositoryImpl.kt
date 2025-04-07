package com.anshok.subzy.data.repository.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.anshok.subzy.domain.rate.RateRepository

class RateRepositoryImpl(
    private val context: Context
) : RateRepository {

    override fun getGooglePlayIntent(): Intent {
        val uri = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
        return Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}

