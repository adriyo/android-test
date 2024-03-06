package com.adriyo.frontendtest.feature.register

import com.adriyo.frontendtest.shared.ItemOption

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

sealed interface RegisterEvent {
    data class OnUsernameChange(val newValue: String) : RegisterEvent
    data class OnEmailChange(val newValue: String) : RegisterEvent
    data class OnRoleChange(val newValue: ItemOption<Int>) : RegisterEvent
    data class OnPasswordChange(val newValue: String) : RegisterEvent
    data object OnSignUpSubmit : RegisterEvent
}