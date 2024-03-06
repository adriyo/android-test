package com.adriyo.frontendtest.feature.login

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

sealed interface FormEvent {
    data class OnEmailChange(val newValue: String) : FormEvent
    data class OnPasswordChange(val newValue: String) : FormEvent
    data object OnLoginSubmit : FormEvent
}