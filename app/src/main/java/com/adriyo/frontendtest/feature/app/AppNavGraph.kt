package com.adriyo.frontendtest.feature.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.adriyo.frontendtest.feature.login.LoginRoute
import com.adriyo.frontendtest.feature.photos.PhotoRoute
import com.adriyo.frontendtest.feature.register.RegisterRoute
import com.adriyo.frontendtest.feature.users.UserRoute
import com.adriyo.frontendtest.shared.utils.Routes

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: AppViewModel = hiltViewModel()) {
    val uiState by viewModel.appState.collectAsStateWithLifecycle()
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary,
            )
        }
        return
    }
    NavHost(navController = navController, startDestination = uiState.startDestination) {
        composable(Routes.LOGIN) { LoginRoute(navController) }
        composable(Routes.REGISTER) { RegisterRoute(navController) }
        composable(Routes.PHOTOS) { PhotoRoute(navController) }
        composable(Routes.USERS) { UserRoute(navController) }
    }
}