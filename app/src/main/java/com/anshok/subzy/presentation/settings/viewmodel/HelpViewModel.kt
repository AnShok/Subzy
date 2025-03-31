package com.anshok.subzy.presentation.settings.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshok.subzy.domain.api.HelpInteractor
import com.anshok.subzy.util.Event

class HelpViewModel(
    private val interactor: HelpInteractor
) : ViewModel() {

    private val _openIntent = MutableLiveData<Event<Intent>>()
    val openIntent: LiveData<Event<Intent>> = _openIntent

    fun onEmailClick() {
        _openIntent.value = Event(interactor.getEmailIntent())
    }

    fun onTelegramClick() {
        _openIntent.value = Event(interactor.getTelegramIntent())
    }
}
