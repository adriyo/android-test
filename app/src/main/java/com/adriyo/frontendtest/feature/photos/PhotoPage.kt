package com.adriyo.frontendtest.feature.photos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.adriyo.frontendtest.R
import com.adriyo.frontendtest.data.model.Photo
import com.adriyo.frontendtest.shared.toast

@Composable
fun PhotoPage(
    modifier: Modifier = Modifier,
    photosPaging: LazyPagingItems<Photo>,
    onLinkClicked: (String?) -> Unit,
) {
    val context = LocalContext.current
    val state = photosPaging.loadState
    LaunchedEffect(key1 = state) {
        if (state.refresh is LoadState.Error) {
            val message = (state.refresh as LoadState.Error).error.message
            context.toast(message)
        }
        if (state.append is LoadState.Error) {
            val message = (state.append as LoadState.Error).error.message
            context.toast(message)
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (state.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .align(Alignment.Center)
            )
            return
        }
        if (state.refresh is LoadState.Error) {
            ErrorScreen(
                message = "${(state.refresh as LoadState.Error).error.message}",
                onRefresh = { photosPaging.refresh() },
            )
            return
        }
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(photosPaging.itemCount) { index ->
                val photo = photosPaging[index]
                RowPhotoGrid(
                    photo = photo,
                    onLinkClicked = onLinkClicked,
                )
            }

            if (state.refresh is LoadState.Loading || state.append is LoadState.Loading) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = stringResource(id = R.string.msg_loading))
                    }
                }
            }

            if (state.append is LoadState.Error) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    ErrorScreen(
                        message = "${(state.append as LoadState.Error).error.message} ",
                        onRefresh = { photosPaging.retry() },
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorScreen(message: String?, onRefresh: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Error: $message", textAlign = TextAlign.Center)
        Button(onClick = onRefresh) {
            Text(text = stringResource(R.string.refresh))
        }
    }
}

@Composable
fun RowPhotoGrid(photo: Photo?, onLinkClicked: (String?) -> Unit) {
    if (photo == null) return
    val borderColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background
    val avatarBorderColor = MaterialTheme.colorScheme.onSurface
    val roundedSize = 10.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(roundedSize)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(roundedSize)
            )
            .padding(bottom = 8.dp),
    ) {
        Box {
            AsyncImage(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = roundedSize,
                            topEnd = roundedSize,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(1.dp),
                model = photo.thumbnailUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Text(
                text = "${photo.id}",
                modifier = Modifier
                    .padding(horizontal = 0.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(
                            topStart = roundedSize,
                            topEnd = 0.dp,
                            bottomEnd = roundedSize,
                            bottomStart = 0.dp
                        )
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.background
            )
        }

        Text(
            text = "${photo.title}",
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light),
            color = avatarBorderColor
        )

        ClickableText(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                        textDecoration = TextDecoration.Underline
                    ),
                ) {
                    append("${photo.url}")
                }
                addStringAnnotation(
                    tag = "",
                    annotation = "${photo.url}",
                    start = 0,
                    end = "${photo.url}".length - 1
                )
            },
            style = MaterialTheme.typography.labelSmall,
            onClick = { onLinkClicked(photo.url) },
        )
    }
}