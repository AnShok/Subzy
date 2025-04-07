package com.anshok.subzy.domain.help

import android.content.Intent

interface HelpInteractor {
    fun getEmailIntent(): Intent
    fun getTelegramIntent(): Intent
}
