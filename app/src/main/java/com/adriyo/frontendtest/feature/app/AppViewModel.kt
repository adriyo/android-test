package com.adriyo.frontendtest.feature.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriyo.frontendtest.data.model.UserRole
import com.adriyo.frontendtest.data.repository.AuthRepository
import com.adriyo.frontendtest.shared.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by adriyo on 05/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    init {
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        _appState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val loggedInUser = authRepository.getLoggedInUser()
            val startDestination = if (loggedInUser == null) {
                Routes.LOGIN
            } else if (loggedInUser.role == UserRole.REGULAR) {
                Routes.PHOTOS
            } else {
                Routes.USERS
            }
            _appState.update {
                it.copy(isLoading = false, startDestination = startDestination)
            }
        }
    }

}

data class AppState(
    val isLoading: Boolean = true,
    val startDestination: String = Routes.LOGIN,
)