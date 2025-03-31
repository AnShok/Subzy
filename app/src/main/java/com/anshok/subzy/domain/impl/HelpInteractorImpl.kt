package com.anshok.subzy.domain.impl

import android.content.Intent
import com.anshok.subzy.domain.api.HelpInteractor
import com.anshok.subzy.domain.api.HelpRepository

class HelpInteractorImpl(private val repository: HelpRepository) : HelpInteractor {
    override fun getEmailIntent(): Intent = repository.getEmailIntent()
    override fun getTelegramIntent(): Intent = repository.getTelegramIntent()
}
