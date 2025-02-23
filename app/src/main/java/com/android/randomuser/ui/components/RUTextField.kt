package com.android.randomuser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun RUOutlinedTextField(
    modifier: Modifier = Modifier,
    state: RUOutlinedTextFieldState,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    placeholderTextStyle: TextStyle = textStyle.copy(color = MaterialTheme.colorScheme.onPrimary),
    shape: Shape = MaterialTheme.shapes.medium,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        cursorColor = MaterialTheme.colorScheme.onSurface,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
    ),
    maxLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = state.value ?: EmptyString,
            onValueChange = { state.onValueChanged(it) },
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            colors = colors,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(MaterialTheme.colorScheme.surface),
            maxLines = maxLines,
            placeholder = {
                RUText(
                    text = placeholder,
                    style = placeholderTextStyle,
                )
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            shape = shape
        )
    }
}

/**
 * The state holder class for the outlined text field ui component
 *
 * @param onValueUpdated Callback to let the owner of this state holder class know about the input
 * value
 */
@Stable
class RUOutlinedTextFieldState(
    val onValueUpdated: ((String?) -> Unit)? = null,
) {
    var value: String? by mutableStateOf(null)

    /**
     * To be called when new value is entered by the user. This applies the filter on the updated
     * value and return only the filtered out value for display
     *
     * @param updatedValue The updated value
     */
    fun onValueChanged(updatedValue: String?) {
        value = updatedValue
        this.onValueUpdated?.invoke(value)
    }
}