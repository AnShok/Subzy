package com.anshok.subzy.domain.api

import android.content.Intent

interface HelpInteractor {
    fun getEmailIntent(): Intent
    fun getTelegramIntent(): Intent
}
