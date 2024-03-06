package com.adriyo.frontendtest.feature.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.adriyo.frontendtest.R
import com.adriyo.frontendtest.shared.widget.StyledButton
import com.adriyo.frontendtest.shared.widget.StyledInput
import com.adriyo.frontendtest.shared.widget.StyledSpinner

@Composable
fun RegisterPage(
    modifier: Modifier = Modifier,
    formEvent: (RegisterEvent) -> Unit = {},
    uiState: RegisterState,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.register),
            style = MaterialTheme.typography.displayMedium
        )
        StyledInput(
            value = uiState.username,
            enabled = !uiState.isLoading,
            errorText = uiState.usernameError,
            onValueChange = { formEvent(RegisterEvent.OnUsernameChange(it)) },
            label = stringResource(R.string.username),
            imeAction = ImeAction.Next
        )
        StyledInput(
            enabled = !uiState.isLoading,
            value = uiState.email,
            errorText = uiState.emailError,
            onValueChange = { formEvent(RegisterEvent.OnEmailChange(it)) },
            label = stringResource(id = R.string.email),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
        val selectedRole = if (uiState.roles.indexOf(uiState.role) == -1) {
            0
        } else {
            uiState.roles.indexOf(uiState.role)
        }
        StyledSpinner(
            enabled = !uiState.isLoading,
            label = stringResource(id = R.string.role),
            errorText = uiState.roleError,
            items = uiState.roles,
            onItemSelected = { option ->
                formEvent(RegisterEvent.OnRoleChange(option))
            },
            selectedIndex = selectedRole,
        )
        StyledInput(
            enabled = !uiState.isLoading,
            value = uiState.password,
            errorText = uiState.passwordError,
            onValueChange = { formEvent(RegisterEvent.OnPasswordChange(it)) },
            label = stringResource(R.string.password),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(20.dp))
        StyledButton(
            loading = uiState.isLoading,
            enabled = !uiState.isLoading,
            title = stringResource(id = R.string.register),
            onClick = { formEvent(RegisterEvent.OnSignUpSubmit) },
        )
    }
}