package com.adriyo.frontendtest.feature.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriyo.frontendtest.R
import com.adriyo.frontendtest.data.model.UserRole
import com.adriyo.frontendtest.data.repository.AuthRepository
import com.adriyo.frontendtest.shared.ItemOption
import com.adriyo.frontendtest.shared.Resource
import com.adriyo.frontendtest.shared.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val validator: Validator,
    private val resource: Resource,
) : ViewModel() {

    private var _uiState = MutableStateFlow(
        RegisterState(
            email = savedStateHandle.get<String>("email") ?: "",
            username = savedStateHandle.get<String>("username") ?: "",
            password = savedStateHandle.get<String>("password") ?: "",
            role = getSelectedRole(savedStateHandle.get<Int>("role") ?: -1),
            roles = getRoles()
        )
    )

    private fun getSelectedRole(role: Int): ItemOption<Int>? {
        return getRoles().find { it.value == role }
    }

    private fun getRoles(): List<ItemOption<Int>> {
        return listOf(
            ItemOption(label = resource.getString(R.string.choose_role), value = -1),
            ItemOption(label = resource.getString(R.string.admin), value = UserRole.ADMIN),
            ItemOption(label = resource.getString(R.string.regular), value = UserRole.REGULAR),
        )
    }

    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    fun formEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnEmailChange -> {
                val email = event.newValue
                savedStateHandle["email"] = email
                _uiState.update {
                    it.copy(
                        email = email,
                        emailError = null
                    )
                }
            }

            is RegisterEvent.OnUsernameChange -> {
                val username = event.newValue
                savedStateHandle["username"] = username
                _uiState.update {
                    it.copy(
                        username = username,
                        usernameError = null
                    )
                }
            }

            is RegisterEvent.OnPasswordChange -> {
                val password = event.newValue
                savedStateHandle["password"] = password
                _uiState.update {
                    it.copy(
                        password = password,
                        passwordError = null
                    )
                }
            }

            is RegisterEvent.OnRoleChange -> {
                val role = event.newValue
                savedStateHandle["role"] = role.value
                _uiState.update {
                    it.copy(
                        role = role,
                        roleError = null
                    )
                }
            }

            RegisterEvent.OnSignUpSubmit -> {
                submit()
            }
        }
    }

    private fun validated(): Boolean {
        val username = uiState.value.username
        val email = uiState.value.email
        val role = uiState.value.role
        val password = uiState.value.password
        val (isUsernameValid, usernameError) = validator.isUsernameValid(username)
        val (isEmailValid, emailError) = validator.isEmailValid(email)
        val (isRoleValid, roleError) = validator.isRoleValid(role)
        val (isPasswordvalid, passwordError) = validator.isRegisterPasswordValid(password)
        _uiState.update {
            it.copy(
                emailError = emailError,
                usernameError = usernameError,
                roleError = roleError,
                passwordError = passwordError,
            )
        }
        return isUsernameValid && isEmailValid && isRoleValid && isPasswordvalid
    }

    private fun submit() {
        if (!validated()) return
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(1000)
            val email = uiState.value.email
            val username = uiState.value.username
            val role = uiState.value.role
            val password = uiState.value.password
            val result = authRepository.register(username, email, role!!.value, password)
            if (result.isFailure) {
                _uiState.update { it.copy(isLoading = false, error = result.exceptionOrNull()) }
                return@launch
            }

            _uiState.update { it.copy(isLoading = false, isSuccess = true, error = Exception(result.getOrNull())) }
        }
    }

}