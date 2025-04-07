package com.anshok.subzy.util.adapter

import android.content.Context
import com.anshok.subzy.domain.logo.model.Logo

fun String?.toLogo(context: Context): Logo {
    return if (this == null) {
        Logo(null, null)
    } else if (startsWith("res://")) {
        val resName = removePrefix("res://")
        val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
        Logo(null, null, logoResId = if (resId != 0) resId else null)
    } else {
        Logo(null, null, logoUrl = this)
    }
}
