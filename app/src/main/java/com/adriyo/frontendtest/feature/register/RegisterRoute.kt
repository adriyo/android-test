package com.adriyo.frontendtest.feature.register

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegisterRoute(
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.error) {
        if (uiState.error != null) {
            Toast.makeText(
                context,
                " ${uiState.error?.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    LaunchedEffect(key1 = uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigateUp()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
            )
        }
    ) { padding ->
        RegisterPage(
            modifier = Modifier.padding(padding),
            formEvent = viewModel::formEvent,
            uiState = uiState,
        )
    }
}

