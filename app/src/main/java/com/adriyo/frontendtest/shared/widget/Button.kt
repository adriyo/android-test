package com.adriyo.frontendtest.shared.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adriyo.frontendtest.shared.theme.FrontendTestTheme

/**
 * Created by adriyo on 04/12/2023.
 * <a href="https://github.com/adriyo">Github</a>
 */

@Composable
fun StyledButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit = {},
    wrapContent: Boolean = false,
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingText: String = "",
    textModifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
) {
    Button(
        colors = colors,
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = if (wrapContent) modifier.wrapContentWidth() else modifier.fillMaxWidth(),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(22.dp),
            )
        }
        Text(
            text = if (loading && loadingText.isNotEmpty()) {
                loadingText
            } else {
                title
            }, modifier = textModifier
        )
    }
}


@Composable
fun StyledOutlinedButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingText: String = "",
    textModifier: Modifier = Modifier,
) {

    OutlinedButton(
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth(),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(22.dp),
            )
        }
        Text(
            text = if (loading && loadingText.isNotEmpty()) {
                loadingText
            } else {
                title
            }, modifier = textModifier
        )
    }
}

@Preview
@Composable
fun StyledButtonPreview() {
    FrontendTestTheme(darkTheme = true) {
        Column {
            StyledButton(title = "Login")
            StyledButton(title = "Login with loading", loading = true)
            StyledButton(title = "Login", wrapContent = true)
        }
    }
}