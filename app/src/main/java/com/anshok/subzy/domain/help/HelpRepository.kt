package com.anshok.subzy.domain.help

import android.content.Intent

interface HelpRepository {
    fun getEmailIntent(): Intent
    fun getTelegramIntent(): Intent
}
