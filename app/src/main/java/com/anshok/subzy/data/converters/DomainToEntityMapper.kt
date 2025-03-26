package com.anshok.subzy.data.converters

import com.anshok.subzy.data.local.db.entities.CategoryEntity
import com.anshok.subzy.data.local.db.entities.PaymentMethodEntity
import com.anshok.subzy.data.local.db.entities.ReminderEntity
import com.anshok.subzy.data.local.db.entities.SubscriptionEntity
import com.anshok.subzy.domain.model.Category
import com.anshok.subzy.domain.model.PaymentMethod
import com.anshok.subzy.domain.model.Reminder
import com.anshok.subzy.domain.model.Subscription

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
