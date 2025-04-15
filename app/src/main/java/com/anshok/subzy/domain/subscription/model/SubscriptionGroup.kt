package com.anshok.subzy.domain.subscription.model

import java.time.LocalDate

data class SubscriptionGroup(
    val date: LocalDate,
    val subscriptions: List<Subscription>
)
