package com.adriyo.frontendtest.feature.users

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adriyo.frontendtest.R
import com.adriyo.frontendtest.data.model.User
import com.adriyo.frontendtest.shared.widget.MainAppBar

@Composable
fun UsersPage(
    modifier: Modifier = Modifier,
    uiState: UiState,
    uiEvent: (UiEvent) -> Unit = {},
) {
    val searchInput = uiState.searchInput
    Scaffold(
        topBar = {
            MainAppBar(
                title = stringResource(id = R.string.users),
                onLogoutClick = { uiEvent(UiEvent.OnLogout) },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Hello,", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = uiState.loggedInUser.username,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            item {
                OutlinedTextField(
                    shape = RoundedCornerShape(10.dp),
                    value = searchInput,
                    onValueChange = { uiEvent(UiEvent.OnSearchInputChange(it)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "search icon"
                        )
                    },
                    trailingIcon = {
                        if (searchInput.isNotEmpty()) {
                            IconButton(
                                onClick = { uiEvent(UiEvent.OnSearchInputChange("")) },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "clear search"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp),
                )
            }
            val users = if (searchInput.isEmpty()) {
                uiState.users
            } else {
                uiState.users.filter { it.username.contains(searchInput, ignoreCase = true) }
            }
            items(users) { user ->
                RowUser(
                    user = user,
                    onEditClick = { uiEvent(UiEvent.OnEditClick(user)) },
                )
            }
        }
    }
}

@Composable
fun RowUser(user: User?, onEditClick: () -> Unit) {
    if (user == null) return
    val borderColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background
    val avatarBorderColor = MaterialTheme.colorScheme.onSurface
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${user.id}",
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.displaySmall,
            color = avatarBorderColor
        )
        VerticalDivider(
            color = borderColor,
            modifier = Modifier
                .width(1.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = user.username,
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.titleSmall,
                color = avatarBorderColor
            )
            Text(
                text = stringResource(id = R.string.email_with_val, user.email),
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light),
                color = avatarBorderColor
            )

            Text(
                text = stringResource(id = R.string.role_with_val, user.getRoleName()),
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light),
                color = avatarBorderColor
            )
        }
        IconButton(
            onClick = onEditClick,
        ) {
            Icon(imageVector = Icons.Rounded.Edit, contentDescription = "edit user")
        }
    }
}