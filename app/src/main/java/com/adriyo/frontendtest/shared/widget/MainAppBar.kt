package com.adriyo.frontendtest.shared.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adriyo.frontendtest.R

/**
 * Created by adriyo on 05/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@Composable
fun MainAppBar(title: String, onLogoutClick: () -> Unit) {
    var showOverflowMenu by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        actions = {
            IconButton(
                onClick = { showOverflowMenu = true },
                modifier = Modifier
                    .padding(end = 16.dp)
                    .border(
                        shape = CircleShape,
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    .size(28.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "appbar account menu"
                )
            }
            DropdownMenu(
                expanded = showOverflowMenu,
                onDismissRequest = { showOverflowMenu = false }) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.logout)) },
                    onClick = onLogoutClick,
                )
            }
        }
    )
}