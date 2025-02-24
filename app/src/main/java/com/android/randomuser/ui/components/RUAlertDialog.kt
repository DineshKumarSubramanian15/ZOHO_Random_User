package com.android.randomuser.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.android.randomuser.R

@Composable
fun RUBasicAlertDialog(
    title: String? = null,
    message: String,
    isDismissButtonRequired: Boolean = false,
    shape: Shape = MaterialTheme.shapes.small,
    confirmButtonText: String = stringResource(id = R.string.ok),
    dismissButtonText: String = stringResource(id = R.string.cancel),
    onConfirmButtonClicked: () -> Unit = {},
    onDismissButtonClicked: () -> Unit = {},
    onDismissed: () -> Unit = {},
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        onDismissRequest = {},
        text = {
            RUText(
                text = message,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmButtonClicked()
                    onDismissed()
                }
            ) {
                RUText(
                    text = confirmButtonText,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            if (isDismissButtonRequired) {
                TextButton(
                    onClick = {
                        onDismissButtonClicked()
                        onDismissed()
                    }
                ) {
                    RUText(
                        text = dismissButtonText,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        title = {
            title?.let {
                RUText(
                    text = it,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
        },
    )
}

@Composable
fun RUBasicAlertDialog(
    message: String,
    isDismissButtonRequired: Boolean = false,
    shape: Shape = MaterialTheme.shapes.small,
    confirmButtonText: String = stringResource(id = R.string.ok),
    dismissButtonText: String = stringResource(id = R.string.cancel),
    onConfirmButtonClicked: () -> Unit = {},
    onDismissButtonClicked: () -> Unit = {},
    onDismissed: () -> Unit = {},
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        onDismissRequest = {},
        text = {
            RUText(
                text = message,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmButtonClicked()
                    onDismissed()
                }
            ) {
                RUText(
                    text = confirmButtonText,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            if (isDismissButtonRequired) {
                TextButton(
                    onClick = {
                        onDismissButtonClicked()
                        onDismissed()
                    }
                ) {
                    RUText(
                        text = dismissButtonText,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
    )
}