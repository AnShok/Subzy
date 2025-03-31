package com.anshok.subzy.domain.api

import android.content.Intent

interface HelpRepository {
    fun getEmailIntent(): Intent
    fun getTelegramIntent(): Intent
}
