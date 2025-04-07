package com.anshok.subzy.data.repository.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.anshok.subzy.R
import com.anshok.subzy.domain.help.HelpRepository

class HelpRepositoryImpl(private val context: Context) : HelpRepository {
    override fun getEmailIntent(): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:anshokbox@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_body))
        }
    }

    override fun getTelegramIntent(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/SubzyApp"))
    }
}
