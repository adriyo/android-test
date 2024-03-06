package com.adriyo.frontendtest.feature.login

data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
)