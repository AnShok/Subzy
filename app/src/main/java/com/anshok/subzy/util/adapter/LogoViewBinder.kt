package com.anshok.subzy.util.adapter

import android.net.Uri
import android.widget.ImageView
import com.anshok.subzy.R
import com.anshok.subzy.domain.logo.model.Logo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

fun bindLogo(logo: Logo?, imageView: ImageView) {
    val context = imageView.context
    val cornerRadius = context.resources.getDimensionPixelSize(R.dimen.dimen_12dp)

    Glide.with(context).clear(imageView)

    when {
        logo?.logoUrl?.startsWith("content://") == true || logo?.logoUrl?.startsWith("file://") == true -> {
            Glide.with(context)
                .load(Uri.parse(logo.logoUrl))
                .placeholder(R.drawable.ic_placeholder_30px)
                .error(R.drawable.ic_placeholder_30px)
                .centerCrop()
                .transform(RoundedCorners(cornerRadius))
                .into(imageView)
        }

        logo?.logoUrl?.startsWith("http") == true -> {
            Glide.with(context)
                .load(logo.logoUrl)
                .placeholder(R.drawable.ic_placeholder_30px)
                .error(R.drawable.ic_placeholder_30px)
                .centerCrop()
                .transform(RoundedCorners(cornerRadius))
                .into(imageView)
        }

        logo?.logoResId != null -> {
            Glide.with(context)
                .load(logo.logoResId)
                .placeholder(R.drawable.ic_placeholder_30px)
                .error(R.drawable.ic_placeholder_30px)
                .centerCrop()
                .transform(RoundedCorners(cornerRadius))
                .into(imageView)
        }

        else -> {
            imageView.setImageResource(R.drawable.ic_placeholder_30px)
        }
    }
}
