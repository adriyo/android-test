package com.adriyo.frontendtest.shared.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.adriyo.frontendtest.shared.ItemOption

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@Composable
fun <T> StyledSpinner(
    modifier: Modifier = Modifier,
    label: String? = null,
    errorText: String? = null,
    onItemSelected: (ItemOption<T>) -> Unit,
    items: List<ItemOption<T>> = emptyList(),
    selectedIndex: Int = 0,
    required: Boolean = false,
    enabled: Boolean = true,
) {
    val typography = MaterialTheme.typography
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxWidth()) {
        if (!label.isNullOrEmpty()) {
            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                    append("$label")
                }
                if (required) {
                    val requiredSymbol = " *"
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                        pushStringAnnotation(tag = requiredSymbol, annotation = requiredSymbol)
                        append(requiredSymbol)
                    }
                }
            }
            Text(
                text = annotatedString,
                style = typography.labelSmall,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )
        }
        OutlinedTextField(
            value = if (items.isEmpty()) "" else items[selectedIndex].label,
            enabled = false,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    if (enabled) {
                        expanded = !expanded
                    }
                },
            maxLines = 1,
            colors = if (enabled) OutlinedTextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current.copy(alpha = 1f),
                disabledBorderColor = LocalContentColor.current.copy(alpha = 1f)
            ) else OutlinedTextFieldDefaults.colors(),
            isError = !errorText.isNullOrEmpty()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = modifier
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(text = item.label, modifier = Modifier.weight(1f))
                        }
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }

        if (!errorText.isNullOrEmpty()) {
            Text(
                text = errorText,
                style = typography.labelSmall.copy(color = MaterialTheme.colorScheme.error),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp)
            )
        }
    }
}