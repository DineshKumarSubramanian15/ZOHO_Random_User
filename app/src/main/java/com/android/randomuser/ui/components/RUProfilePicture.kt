package com.android.randomuser.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun RUProfilePicture(profilePic: String?, name: String?, modifier: Modifier = Modifier) {
    var isLoading by remember { mutableStateOf(true) }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(profilePic)
            .crossfade(true)
            .listener(
                onStart = { isLoading = true },
                onSuccess = { _, _ -> isLoading = false },
                onError = { _, _ -> isLoading = false }
            )
            .build()
    )

    Image(
        painter = painter,
        contentDescription = "ProfilePic: $name",
        modifier = modifier
            .size(180.dp)
            .clip(MaterialTheme.shapes.medium)
            .placeholder(
                visible = isLoading,
                highlight = PlaceholderHighlight.shimmer()
            )
    )
}
