package com.anshok.subzy.data.converters

import com.anshok.subzy.data.local.db.entities.*
import com.anshok.subzy.domain.model.*

object DomainToEntityMapper {

    fun subscription(domain: Subscription) = SubscriptionEntity(
        id = domain.id,
        name = domain.name,
        logoUrl = domain.logoUrl,
        price = domain.price,
        currencyCode = domain.currencyCode,
        description = domain.description,
        paymentPeriodDays = domain.paymentPeriodDays,
        firstPaymentDate = domain.firstPaymentDate,
        nextPaymentDate = domain.nextPaymentDate,
        paymentMethodId = domain.paymentMethodId,
        categoryId = domain.categoryId,
        comment = domain.comment
    )

    fun category(domain: Category) = CategoryEntity(
        id = domain.id,
        name = domain.name,
        budget = domain.budget,
        color = domain.color
    )

    fun paymentMethod(domain: PaymentMethod) = PaymentMethodEntity(
        id = domain.id,
        name = domain.name,
        cardNumber = domain.cardNumber,
        expirationDate = domain.expirationDate,
        isDefault = domain.isDefault
    )

    fun reminder(domain: Reminder) = ReminderEntity(
        id = domain.id,
        subscriptionId = domain.subscriptionId,
        reminderTime = domain.reminderTime
    )
}
