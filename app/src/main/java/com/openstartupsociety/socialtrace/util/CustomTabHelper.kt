package com.openstartupsociety.socialtrace.util

import android.content.ActivityNotFoundException
import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.util.extensions.toast

object CustomTabHelper {

    fun openCustomTab(context: Context, url: String) {
        val customTabsIntent = CustomTabsIntent.Builder().apply {
            setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }.build()
        try {
            customTabsIntent.launchUrl(context, url.toUri())
        } catch (e: ActivityNotFoundException) {
            context.toast("No browser found to open web page")
        }
    }
}
