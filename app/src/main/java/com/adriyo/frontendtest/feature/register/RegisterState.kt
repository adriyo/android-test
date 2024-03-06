package com.adriyo.frontendtest.feature.register

import com.adriyo.frontendtest.shared.ItemOption

data class RegisterState(
    val isLoading: Boolean = false,
    val email: String = "",
    val emailError: String? = null,
    val username: String = "",
    val usernameError: String? = null,
    val role: ItemOption<Int>? = null,
    val roleError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val error: Throwable? = null,
    val isSuccess: Boolean = false,
    val roles: List<ItemOption<Int>> = listOf(),
)