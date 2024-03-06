package com.adriyo.frontendtest.shared.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adriyo.frontendtest.R
import com.adriyo.frontendtest.shared.theme.FrontendTestTheme

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@Composable
fun StyledInput(
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    icon: ImageVector? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    value: String,
    onValueChange: ((String) -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorText: String? = null,
    required: Boolean = false,
    imeAction: ImeAction = ImeAction.Done,
    enabled: Boolean = true,
) {
    val typography = MaterialTheme.typography
    val _leadingIcon = if (icon != null) {
        { Icon(imageVector = icon, contentDescription = "") }
    } else leadingIcon
    var passwordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
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
            enabled = enabled,
            value = value,
            onValueChange = { onValueChange?.invoke(it) },
            trailingIcon = {
                if (keyboardType == KeyboardType.Password) {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier
                            .clickable { passwordVisible = !passwordVisible }
                            .padding(8.dp)
                    ) {
                        if (passwordVisible) {
                            Icon(
                                painter = painterResource(id = R.drawable.visibility_24px),
                                contentDescription = "password visibility icon on"
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.visibility_off_24px),
                                contentDescription = "password visibility icon off"
                            )
                        }
                    }
                }
            },
            modifier = modifier
                .fillMaxWidth(),
            leadingIcon = _leadingIcon,
            placeholder = { Text(placeholder) },
            visualTransformation = if (keyboardType == KeyboardType.Password && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            isError = !errorText.isNullOrEmpty(),
        )
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

@Preview
@Composable
fun StyledInputPreview() {
    FrontendTestTheme {
        Column {
            var textValue by remember { mutableStateOf("") }
            StyledInput(value = textValue, onValueChange = { textValue = it })
        }
    }
}