package com.android.randomuser.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.randomuser.R

@Composable
fun RUOfflineBanner(
    text: String = stringResource(id = R.string.offline_message),
    isInternetConnected: Boolean,
) {
    AnimatedVisibility(
        visible = !isInternetConnected,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        RUText(
            text = text,
            modifier = Modifier
                .background(
                    shape = RectangleShape,
                    color = MaterialTheme.colorScheme.error
                )
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
        )
    }
}