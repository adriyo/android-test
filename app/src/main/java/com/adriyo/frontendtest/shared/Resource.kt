package com.adriyo.frontendtest.shared

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
class Resource @Inject constructor(
    private val context: Context,
) {

    fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

}