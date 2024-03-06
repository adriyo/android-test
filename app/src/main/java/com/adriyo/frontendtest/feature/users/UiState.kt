package com.adriyo.frontendtest.feature.users

import com.adriyo.frontendtest.data.model.User
import com.adriyo.frontendtest.shared.ItemOption

data class UiState(
    val loggedInUser: User = User(),
    val users: List<User> = listOf(),
    val error: Throwable? = null,
    val searchInput: String = "",
    val selectedUser: User? = null,
    val roles: List<ItemOption<Int>> = listOf(),
    val inputEmail: String = "",
    val emailError: String? = null,
    val inputUsername: String = "",
    val usernameError: String? = null,
    val inputRole: ItemOption<Int> = ItemOption(value = -1),
    val roleError: String? = null,
    val inputPassword: String = "",
    val passwordError: String? = null,
    val inputNewPassword: String = "",
    val newPasswordError: String? = null,
    val showInputPassword: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)
