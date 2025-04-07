package com.anshok.subzy.util

import android.content.Context
import com.anshok.subzy.R

object MonthUtils {
    fun getMonthNameResId(month: Int): Int {
        return when (month) {
            1 -> R.string.month_1
            2 -> R.string.month_2
            3 -> R.string.month_3
            4 -> R.string.month_4
            5 -> R.string.month_5
            6 -> R.string.month_6
            7 -> R.string.month_7
            8 -> R.string.month_8
            9 -> R.string.month_9
            10 -> R.string.month_10
            11 -> R.string.month_11
            12 -> R.string.month_12
            else -> throw IllegalArgumentException("Invalid month value: $month")
        }
    }

    fun getLocalizedMonthName(context: Context, month: Int): String {
        val resId = getMonthNameResId(month)
        return context.getString(resId)
    }
}
