package com.anshok.subzy.util.adapter

import android.net.Uri
import android.widget.ImageView
import com.anshok.subzy.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

fun bindLogo(logoUrl: String?, imageView: ImageView) {
    val context = imageView.context
    val cornerRadius = context.resources.getDimensionPixelSize(R.dimen.dimen_12dp)

    when {
        logoUrl.isNullOrBlank() -> {
            imageView.setImageResource(R.drawable.ic_placeholder_30px)
        }

        logoUrl.startsWith("res://") -> {
            val resName = logoUrl.removePrefix("res://")
            val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
            if (resId != 0) {
                Glide.with(context)
                    .load(resId)
                    .placeholder(R.drawable.ic_placeholder_30px)
                    .error(R.drawable.ic_placeholder_30px)
                    .centerCrop()
                    .transform(RoundedCorners(cornerRadius))
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.ic_placeholder_30px)
            }
        }

        logoUrl.startsWith("content://") || logoUrl.startsWith("file://") -> {
            Glide.with(context)
                .load(Uri.parse(logoUrl)) // ⬅️ используем Uri!
                .placeholder(R.drawable.ic_placeholder_30px)
                .error(R.drawable.ic_placeholder_30px)
                .centerCrop()
                .transform(RoundedCorners(cornerRadius))
                .into(imageView)
        }

        else -> {
            Glide.with(context)
                .load(logoUrl)
                .placeholder(R.drawable.ic_placeholder_30px)
                .error(R.drawable.ic_placeholder_30px)
                .centerCrop()
                .transform(RoundedCorners(cornerRadius))
                .into(imageView)
        }
    }
}
