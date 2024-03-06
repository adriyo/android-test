package com.adriyo.frontendtest.feature.users

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adriyo.frontendtest.shared.ObserveEvents
import com.adriyo.frontendtest.shared.toast
import com.adriyo.frontendtest.shared.utils.Routes
import com.adriyo.frontendtest.shared.widget.LoadingDialog

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

enum class ScreenType {
    LIST, DETAIL
}

@Composable
fun UserRoute(navController: NavHostController, viewModel: UserViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val screenType: ScreenType = getScreenType(uiState)

    ObserveEvents(viewModel.uiEvent) { event ->
        when (event) {
            UserEvent.OnLogout -> {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }

            is UserEvent.ShowToast -> {
                context.toast(event.message)
            }
        }
    }

    LoadingDialog(show = uiState.isLoading)

    when (screenType) {
        ScreenType.LIST -> {
            UsersPage(
                uiState = uiState,
                uiEvent = viewModel::onUiEvent,
            )
        }
        ScreenType.DETAIL -> {
            DetailUserPage(
                uiState = uiState,
                formEvent = viewModel::onUiEvent,
                onBack = {
                    viewModel.onUiEvent(UiEvent.OnClearSelectedUser)
                }
            )
            BackHandler {
                viewModel.onUiEvent(UiEvent.OnClearSelectedUser)
            }
        }
    }
}

fun getScreenType(uiState: UiState): ScreenType {
    return if (uiState.selectedUser != null) {
        ScreenType.DETAIL
    } else {
        ScreenType.LIST
    }
}