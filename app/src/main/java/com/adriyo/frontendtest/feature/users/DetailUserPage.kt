package com.adriyo.frontendtest.feature.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.adriyo.frontendtest.R
import com.adriyo.frontendtest.shared.ItemOption
import com.adriyo.frontendtest.shared.widget.StyledButton
import com.adriyo.frontendtest.shared.widget.StyledInput
import com.adriyo.frontendtest.shared.widget.StyledSpinner

@Composable
fun DetailUserPage(
    uiState: UiState,
    formEvent: (UiEvent) -> Unit,
    onBack: () -> Unit,
) {
    val selectedUser = uiState.selectedUser ?: return
    val loggedInUser = uiState.loggedInUser

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.detail))
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    if (loggedInUser.id != selectedUser.id) {
                        IconButton(
                            onClick = { formEvent(UiEvent.OnChangePasswordDialogVisibility(true)) },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            StyledInput(
                enabled = !uiState.isLoading,
                value = uiState.inputUsername,
                errorText = uiState.usernameError,
                onValueChange = { formEvent(UiEvent.OnUsernameChange(it)) },
                label = stringResource(R.string.username),
                imeAction = ImeAction.Next
            )
            StyledInput(
                enabled = !uiState.isLoading,
                value = uiState.inputEmail,
                errorText = uiState.emailError,
                onValueChange = { formEvent(UiEvent.OnEmailChange(it)) },
                label = stringResource(id = R.string.email),
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
            val selectedRole = getSelectedRoleIndex(uiState.inputRole.value, uiState.roles)
            StyledSpinner(
                enabled = !uiState.isLoading,
                label = stringResource(id = R.string.role),
                errorText = uiState.roleError,
                items = uiState.roles,
                onItemSelected = { option ->
                    formEvent(UiEvent.OnRoleChange(option))
                },
                selectedIndex = selectedRole,
            )
            if (loggedInUser.id == selectedUser.id) {
                StyledInput(
                    enabled = !uiState.isLoading,
                    value = uiState.inputNewPassword,
                    errorText = uiState.newPasswordError,
                    onValueChange = { formEvent(UiEvent.OnNewPasswordChange(it)) },
                    label = stringResource(id = R.string.new_password),
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
                StyledInput(
                    enabled = !uiState.isLoading,
                    value = uiState.inputPassword,
                    errorText = uiState.passwordError,
                    onValueChange = { formEvent(UiEvent.OnPasswordChange(it)) },
                    label = stringResource(id = R.string.current_password),
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            StyledButton(
                enabled = !uiState.isLoading,
                title = stringResource(id = R.string.save),
                onClick = { formEvent(UiEvent.OnEditSubmit) },
            )

            InputPasswordDialog(
                uiState = uiState,
                formEvent = formEvent,
                onDismissRequest = {},
            )
        }
    }
}

@Composable
fun InputPasswordDialog(
    uiState: UiState,
    onDismissRequest: () -> Unit = {},
    formEvent: (UiEvent) -> Unit,
) {
    if (!uiState.showInputPassword) return
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp)
            ) {
                StyledInput(
                    value = uiState.inputPassword,
                    errorText = uiState.passwordError,
                    onValueChange = { formEvent(UiEvent.OnPasswordChange(it)) },
                    label = stringResource(R.string.password),
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
                StyledButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    title = stringResource(id = R.string.delete),
                    onClick = {
                        formEvent(UiEvent.OnDeleteUser)
                        onDismissRequest()
                    },
                )
            }
        }
    }
}


private fun getSelectedRoleIndex(role: Int, roles: List<ItemOption<Int>>): Int {
    for ((index, roleItem) in roles.withIndex()) {
        if (roleItem.value == role) return index
    }
    return 0
}
