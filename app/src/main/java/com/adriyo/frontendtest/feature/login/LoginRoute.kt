package com.adriyo.frontendtest.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adriyo.frontendtest.R
import com.adriyo.frontendtest.shared.ObserveEvents
import com.adriyo.frontendtest.shared.toast
import com.adriyo.frontendtest.shared.utils.Routes

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@Composable
fun LoginRoute(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveEvents(viewModel.loginEvent) { event ->
        when (event) {
            AppEvent.NavigateToPhotoScreen -> {
                context.toast(R.string.msg_login_success)
                navController.navigate(Routes.PHOTOS) {
                    popUpTo(Routes.LOGIN) {
                        inclusive = true
                    }
                }
            }
            AppEvent.NavigateToUserScreen -> {
                context.toast(R.string.msg_login_success)
                navController.navigate(Routes.USERS) {
                    popUpTo(Routes.LOGIN) {
                        inclusive = true
                    }
                }
            }

            is AppEvent.ShowToast -> {
                context.toast(event.error?.message)
            }
        }
    }
    LoginPage(
        formEvent = viewModel::formEvent,
        onSignUpClick = { navController.navigate(Routes.REGISTER) },
        uiState = uiState
    )
}