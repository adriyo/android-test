package com.adriyo.frontendtest.feature.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriyo.frontendtest.R
import com.adriyo.frontendtest.data.model.UserRole
import com.adriyo.frontendtest.data.repository.AuthRepository
import com.adriyo.frontendtest.data.repository.PhotoRepository
import com.adriyo.frontendtest.data.repository.UserRepository
import com.adriyo.frontendtest.shared.ItemOption
import com.adriyo.frontendtest.shared.Resource
import com.adriyo.frontendtest.shared.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val photoRepository: PhotoRepository,
    private val resource: Resource,
    private val validator: Validator,
) : ViewModel() {

    private val _uiEvent = Channel<UserEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val loggedInUser = authRepository.getLoggedInUser() ?: return@launch
            userRepository.getUsers()
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    _uiState.update {
                        it.copy(error = throwable)
                    }
                }
                .collect { users ->
                    _uiState.update {
                        it.copy(
                            users = users,
                            loggedInUser = loggedInUser,
                            roles = getRoles()
                        )
                    }
                }
        }
    }

    private fun getSelectedRole(role: Int): ItemOption<Int> {
        return getRoles().find { it.value == role } ?: ItemOption(value = -1)
    }

    private fun getRoles(): List<ItemOption<Int>> {
        return listOf(
            ItemOption(label = resource.getString(R.string.choose_role), value = -1),
            ItemOption(label = resource.getString(R.string.admin), value = UserRole.ADMIN),
            ItemOption(label = resource.getString(R.string.regular), value = UserRole.REGULAR),
        )
    }

    fun onUiEvent(event: UiEvent) {
        when (event) {
            UiEvent.OnLogout -> {
                viewModelScope.launch {
                    photoRepository.deleteAll()
                    authRepository.logout()
                    _uiEvent.send(UserEvent.OnLogout)
                }
            }

            is UiEvent.OnSearchInputChange -> {
                _uiState.update { it.copy(searchInput = event.text) }
            }

            is UiEvent.OnEditClick -> {
                _uiState.update {
                    it.copy(
                        selectedUser = event.user,
                        inputEmail = event.user.email,
                        emailError = "",
                        inputUsername = event.user.username,
                        usernameError = "",
                        inputRole = getSelectedRole(event.user.role),
                        roleError = "",
                        inputPassword = "",
                        passwordError = ""
                    )
                }
            }

            is UiEvent.OnEmailChange -> {
                _uiState.update { it.copy(inputEmail = event.newValue, emailError = null) }
            }

            is UiEvent.OnRoleChange -> {
                _uiState.update { it.copy(inputRole = event.newValue, roleError = null) }
            }

            is UiEvent.OnUsernameChange -> {
                _uiState.update { it.copy(inputUsername = event.newValue, usernameError = null) }
            }

            is UiEvent.OnPasswordChange -> {
                _uiState.update { it.copy(inputPassword = event.newValue, passwordError = null) }
            }

            is UiEvent.OnNewPasswordChange -> {
                _uiState.update {
                    it.copy(
                        inputNewPassword = event.newValue,
                        newPasswordError = null
                    )
                }
            }

            UiEvent.OnClearSelectedUser -> {
                _uiState.update {
                    it.copy(
                        selectedUser = null,
                        inputEmail = "",
                        emailError = null,
                        inputUsername = "",
                        usernameError = null,
                        inputPassword = "",
                        passwordError = null,
                        inputNewPassword = "",
                        newPasswordError = null
                    )
                }
            }

            UiEvent.OnEditSubmit -> {
                onEditAction()
            }

            UiEvent.OnDeleteUser -> {
                onDeleteAction()
            }

            is UiEvent.OnChangePasswordDialogVisibility -> {
                _uiState.update { it.copy(showInputPassword = event.show) }
            }
        }
    }

    private fun editActionValidated(): Boolean {
        val selectedUser = uiState.value.selectedUser ?: return false
        val loggedInUser = uiState.value.loggedInUser
        val username = uiState.value.inputUsername
        val email = uiState.value.inputEmail
        val role = uiState.value.inputRole
        val password = uiState.value.inputPassword
        val newPassword = uiState.value.inputNewPassword

        val (isUsernameValid, usernameError) = validator.isUsernameValid(username)
        val (isEmailValid, emailError) = validator.isEmailValid(email)
        val (isRoleValid, roleError) = validator.isRoleValid(role)

        if (loggedInUser.id == selectedUser.id && newPassword.isNotEmpty()) {
            val (isPasswordValid, passwordError) = validator.isLoggedInUserPasswordValid(
                password,
                loggedInUser.password
            )
            val (isNewPasswordValid, newPasswordError) = validator.isRegisterPasswordValid(
                newPassword
            )
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    usernameError = usernameError,
                    roleError = roleError,
                    passwordError = passwordError,
                    newPasswordError = newPasswordError,
                )
            }
            return isUsernameValid && isEmailValid && isRoleValid && isPasswordValid && isNewPasswordValid
        }

        _uiState.update {
            it.copy(
                emailError = emailError,
                usernameError = usernameError,
                roleError = roleError,
            )
        }
        return isUsernameValid && isEmailValid && isRoleValid
    }

    private fun deleteActionValidated(): Boolean {
        val password = uiState.value.inputPassword
        val (isPasswordValid, passwordError) = validator.isLoggedInUserPasswordValid(
            input = password,
            currentPassword = uiState.value.selectedUser?.password
        )
        _uiState.update {
            it.copy(
                passwordError = passwordError,
            )
        }
        return isPasswordValid
    }

    private fun onDeleteAction() {
        val selectedUser = uiState.value.selectedUser ?: return
        if (!deleteActionValidated()) return
        _uiState.update { it.copy(isLoading = true, showInputPassword = false) }
        viewModelScope.launch {
            val result = userRepository.deleteUser(
                selectedUser = selectedUser,
            )
            delay(1000)
            if (result.isFailure) {
                _uiState.update { it.copy(isLoading = false) }
                _uiEvent.send(UserEvent.ShowToast(result.exceptionOrNull()?.message))
            } else {
                _uiEvent.send(UserEvent.ShowToast(result.getOrNull()))
                _uiState.update { it.copy(isLoading = false, selectedUser = null) }
            }
        }
    }

    private fun onEditAction() {
        val selectedUser = uiState.value.selectedUser ?: return

        val inputEmail = uiState.value.inputEmail
        val inputUsername = uiState.value.inputUsername
        val inputRole = uiState.value.inputRole
        val inputNewPassword = uiState.value.inputNewPassword

        if (!editActionValidated()) return

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = userRepository.updateUser(
                selectedUser = selectedUser,
                loggedInUser = uiState.value.loggedInUser,
                inputEmail = inputEmail,
                inputUsername = inputUsername,
                inputRole = inputRole,
                inputNewPassword = inputNewPassword
            )
            delay(1000)
            if (result.isFailure) {
                _uiState.update { it.copy(isLoading = false) }
                _uiEvent.send(UserEvent.ShowToast(result.exceptionOrNull()?.message))
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        selectedUser = null,
                        inputNewPassword = "",
                        inputPassword = ""
                    )
                }
                _uiEvent.send(UserEvent.ShowToast(result.getOrNull()))
            }
        }
    }

}
