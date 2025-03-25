package com.anshok.subzy.data.converters

import com.anshok.subzy.data.local.db.entities.CategoryEntity
import com.anshok.subzy.data.local.db.entities.PaymentMethodEntity
import com.anshok.subzy.data.local.db.entities.ReminderEntity
import com.anshok.subzy.data.local.db.entities.SubscriptionEntity
import com.anshok.subzy.data.remote.search.dto.LogoResponse
import com.anshok.subzy.domain.model.Category
import com.anshok.subzy.domain.model.Logo
import com.anshok.subzy.domain.model.PaymentMethod
import com.anshok.subzy.domain.model.Reminder
import com.anshok.subzy.domain.model.Subscription

object EntityToDomainMapper {

    fun subscription(entity: SubscriptionEntity) = Subscription(
        id = entity.id,
        name = entity.name,
        logoUrl = entity.logoUrl,
        price = entity.price,
        currencyCode = entity.currencyCode,
        description = entity.description,
        paymentPeriodDays = entity.paymentPeriodDays,
        firstPaymentDate = entity.firstPaymentDate,
        nextPaymentDate = entity.nextPaymentDate,
        paymentMethodId = entity.paymentMethodId,
        categoryId = entity.categoryId,
        comment = entity.comment
    )

    fun category(entity: CategoryEntity) = Category(
        id = entity.id,
        name = entity.name,
        budget = entity.budget,
        color = entity.color
    )

    fun paymentMethod(entity: PaymentMethodEntity) = PaymentMethod(
        id = entity.id,
        name = entity.name,
        cardNumber = entity.cardNumber,
        expirationDate = entity.expirationDate,
        isDefault = entity.isDefault
    )

    fun reminder(entity: ReminderEntity) = Reminder(
        id = entity.id,
        subscriptionId = entity.subscriptionId,
        reminderTime = entity.reminderTime
    )

    fun logo(response: LogoResponse) = Logo(
        name = response.name,
        domain = response.domain,
        logoUrl = response.logoUrl
    )
}
