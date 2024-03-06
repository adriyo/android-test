package com.adriyo.frontendtest.feature.photos

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.adriyo.frontendtest.R
import com.adriyo.frontendtest.shared.ObserveEvents
import com.adriyo.frontendtest.shared.utils.Routes
import com.adriyo.frontendtest.shared.widget.MainAppBar

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@Composable
fun PhotoRoute(navController: NavHostController, viewModel: PhotoViewModel = hiltViewModel()) {
    val photosPaging = viewModel.photosPaging.collectAsLazyPagingItems()
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {}
    ObserveEvents(viewModel.uiEvent) { event ->
        when (event) {
            UiEvent.OnLogout -> {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        }
    }
    Scaffold(
        topBar = {
            MainAppBar(
                title = stringResource(id = R.string.photos),
                onLogoutClick = {
                    viewModel.changeUiEvent(UiEvent.OnLogout)
                },
            )
        },
    ) { padding ->
        PhotoPage(
            modifier = Modifier.padding(padding),
            photosPaging = photosPaging,
            onLinkClicked = { url ->
                if (url == null) return@PhotoPage
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    launcher.launch(intent)
                } catch (_: Exception) {

                }
            }
        )
    }
}

