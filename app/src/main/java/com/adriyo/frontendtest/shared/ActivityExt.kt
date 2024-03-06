package com.adriyo.frontendtest.shared

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Created by adriyo on 05/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

fun Context.toast(text: String?) {
    if (text.isNullOrEmpty()) return
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.toast(@StringRes resId: Int) {
    if (resId == -1) return
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}
