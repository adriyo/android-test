package com.adriyo.frontendtest.feature.users

import com.adriyo.frontendtest.data.model.User
import com.adriyo.frontendtest.shared.ItemOption

sealed interface UiEvent {
    data object OnLogout : UiEvent
    data object OnClearSelectedUser : UiEvent
    data class OnSearchInputChange(val text: String) : UiEvent
    data class OnEditClick(val user: User) : UiEvent
    data class OnUsernameChange(val newValue: String) : UiEvent
    data class OnEmailChange(val newValue: String) : UiEvent
    data class OnRoleChange(val newValue: ItemOption<Int>) : UiEvent
    data class OnPasswordChange(val newValue: String) : UiEvent
    data class OnNewPasswordChange(val newValue: String) : UiEvent
    data object OnEditSubmit : UiEvent
    data object OnDeleteUser : UiEvent
    data class OnChangePasswordDialogVisibility(val show: Boolean): UiEvent
}

sealed interface UserEvent {
    data object OnLogout : UserEvent
    data class ShowToast(val message: String?) : UserEvent
}