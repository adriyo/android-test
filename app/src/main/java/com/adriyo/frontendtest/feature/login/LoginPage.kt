package com.adriyo.frontendtest.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import com.adriyo.frontendtest.shared.widget.StyledOutlinedButton

@Composable
fun LoginPage(
    formEvent: (FormEvent) -> Unit = {},
    onSignUpClick: () -> Unit,
    uiState: LoginState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.displayMedium
        )
        StyledInput(
            enabled = !uiState.isLoading,
            value = uiState.email,
            errorText = uiState.emailError,
            onValueChange = { formEvent(FormEvent.OnEmailChange(it)) },
            icon = Icons.Default.Email,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            placeholder = stringResource(id = R.string.email)
        )
        StyledInput(
            enabled = !uiState.isLoading,
            value = uiState.password,
            errorText = uiState.passwordError,
            onValueChange = { formEvent(FormEvent.OnPasswordChange(it)) },
            icon = Icons.Default.Lock,
            keyboardType = KeyboardType.Password,
            placeholder = stringResource(id = R.string.password)
        )
        Spacer(modifier = Modifier.height(20.dp))
        StyledButton(
            loading = uiState.isLoading,
            enabled = !uiState.isLoading,
            title = stringResource(id = R.string.login),
            onClick = {
                formEvent(FormEvent.OnLoginSubmit)
            },
        )
        StyledOutlinedButton(
            title = stringResource(id = R.string.sign_up),
            onClick = onSignUpClick,
            enabled = !uiState.isLoading,
        )
    }
}