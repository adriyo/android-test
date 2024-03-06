package com.adriyo.frontendtest.shared.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.adriyo.frontendtest.R

/**
 * Created by adriyo on 05/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@Composable
fun LoadingDialog(
    show: Boolean = false,
    onDismissRequest: () -> Unit = {},
) {
    if (!show) return
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(48.dp),
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = stringResource(R.string.sedang_diproses),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}