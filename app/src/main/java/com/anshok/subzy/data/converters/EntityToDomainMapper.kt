package com.anshok.subzy.data.converters

import com.anshok.subzy.data.local.entities.CategoryEntity
import com.anshok.subzy.data.local.entities.PaymentMethodEntity
import com.anshok.subzy.data.local.entities.SubscriptionEntity
import com.anshok.subzy.data.remote.logo.dto.LogoResponse
import com.anshok.subzy.domain.category.model.Category
import com.anshok.subzy.domain.logo.model.Logo
import com.anshok.subzy.domain.paymentMethod.model.PaymentMethod
import com.anshok.subzy.domain.paymentPeriod.model.PaymentPeriodType
import com.anshok.subzy.domain.reminder.model.ReminderType
import com.anshok.subzy.domain.subscription.model.Subscription

object EntityToDomainMapper {

    fun subscription(entity: SubscriptionEntity) = Subscription(
        id = entity.id,
        name = entity.name,
        logoUrl = entity.logoUrl,
        price = entity.price,
        currencyCode = entity.currencyCode,
        description = entity.description,
        paymentPeriod = entity.paymentPeriod,
        paymentPeriodType = PaymentPeriodType.valueOf(entity.paymentPeriodType),
        firstPaymentDate = entity.firstPaymentDate,
        nextPaymentDate = entity.nextPaymentDate,
        paymentMethodId = entity.paymentMethodId,
        categoryId = entity.categoryId,
        comment = entity.comment,
        reminderType = ReminderType.valueOf(entity.reminderType)
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

//    fun reminder(entity: ReminderEntity) = Reminder(
//        id = entity.id,
//        subscriptionId = entity.subscriptionId,
//        reminderTime = entity.reminderTime
//    )

    fun logo(response: LogoResponse) = Logo(
        name = response.name,
        domain = response.domain,
        logoUrl = response.logoUrl
    )
}
