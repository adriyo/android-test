package com.adriyo.frontendtest.shared

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
data class ItemOption<T>(
    val label: String = "",
    val value: T,
)
