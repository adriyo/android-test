package com.adriyo.frontendtest.feature.photos

sealed interface UiEvent {
    data object OnLogout: UiEvent
}