package com.anshok.subzy.domain.help

import android.content.Intent

class HelpInteractorImpl(private val repository: HelpRepository) : HelpInteractor {
    override fun getEmailIntent(): Intent = repository.getEmailIntent()
    override fun getTelegramIntent(): Intent = repository.getTelegramIntent()
}
