package com.adriyo.frontendtest.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriyo.frontendtest.data.model.UserRole
import com.adriyo.frontendtest.data.repository.AuthRepository
import com.adriyo.frontendtest.shared.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val validator: Validator,
) : ViewModel() {

    private val _loginEvent = Channel<AppEvent>()
    val loginEvent = _loginEvent.receiveAsFlow()

    private var _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun formEvent(event: FormEvent) {
        when (event) {
            is FormEvent.OnEmailChange -> {
                val email = event.newValue
                _uiState.update {
                    it.copy(
                        email = email,
                        emailError = null
                    )
                }
            }

            is FormEvent.OnPasswordChange -> {
                val password = event.newValue
                _uiState.update {
                    it.copy(
                        password = password,
                        passwordError = null
                    )
                }
            }

            FormEvent.OnLoginSubmit -> {
                submit()
            }
        }
    }

    private fun validated(): Boolean {
        val email = uiState.value.email
        val password = uiState.value.password
        val (isEmailValid, emailError) = validator.isEmailValid(email)
        val (isPasswordvalid, passwordError) = validator.isPasswordValid(password)
        _uiState.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError
            )
        }
        return isEmailValid && isPasswordvalid
    }

    private fun submit() {
        if (!validated()) return
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(1000)
            val email = uiState.value.email
            val password = uiState.value.password
            val result = authRepository.login(email, password)
            val loggedInUser = result.getOrNull()
            if (result.isFailure || loggedInUser == null) {
                _uiState.update { it.copy(isLoading = false) }
                _loginEvent.send(AppEvent.ShowToast(result.exceptionOrNull()))
                return@launch
            }

            _uiState.update { it.copy(isLoading = false) }
            if (loggedInUser.role == UserRole.REGULAR) {
                _loginEvent.send(AppEvent.NavigateToPhotoScreen)
            } else {
                _loginEvent.send(AppEvent.NavigateToUserScreen)
            }
        }
    }
}

sealed interface AppEvent {
    data class ShowToast(val error: Throwable? = null): AppEvent
    data object NavigateToUserScreen: AppEvent
    data object NavigateToPhotoScreen: AppEvent
}

