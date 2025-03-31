package com.anshok.subzy.presentation.settings.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshok.subzy.domain.api.HelpInteractor
import com.anshok.subzy.domain.api.RateInteractor
import com.anshok.subzy.util.Event

class RateViewModel(
    private val rateInteractor: RateInteractor,
    private val helpInteractor: HelpInteractor
) : ViewModel() {

    private val _showFeedbackSheet = MutableLiveData<Event<Unit>>()
    val showFeedbackSheet: LiveData<Event<Unit>> = _showFeedbackSheet

    private val _openPlayMarket = MutableLiveData<Event<Intent>>()
    val openPlayMarket: LiveData<Event<Intent>> = _openPlayMarket

    private val _openEmail = MutableLiveData<Event<Intent>>()
    val openEmail: LiveData<Event<Intent>> = _openEmail

    fun onStarSelected(rating: Int) {
        if (rating <= 3) {
            _showFeedbackSheet.value = Event(Unit)
        } else {
            _openPlayMarket.value = Event(rateInteractor.getGooglePlayIntent())
        }
    }

    fun onSendEmailClicked() {
        _openEmail.value = Event(helpInteractor.getEmailIntent())
    }
}
