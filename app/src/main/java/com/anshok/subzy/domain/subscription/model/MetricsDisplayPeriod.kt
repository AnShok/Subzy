package com.anshok.subzy.domain.subscription.model

enum class MetricsDisplayPeriod {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY;

    fun next(): MetricsDisplayPeriod = when (this) {
        DAILY -> WEEKLY
        WEEKLY -> MONTHLY
        MONTHLY -> YEARLY
        YEARLY -> DAILY
    }

    fun displayText(): String = when (this) {
        DAILY -> "Daily"
        WEEKLY -> "Weekly"
        MONTHLY -> "Monthly"
        YEARLY -> "Yearly"
    }
}

