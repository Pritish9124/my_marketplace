package com.androstark.marketplace.presentation.sell.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.androstark.marketplace.domain.model.SellFormEvent
import com.androstark.marketplace.domain.model.SellFormState

@Composable
fun LocationField(
    formState: SellFormState,
    onEvent: (SellFormEvent) -> Unit,
    showError: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = formState.location?.address ?: "",
        onValueChange = {},
        readOnly = true,
        label = { Text("Location *") },
        placeholder = { Text("Select location") },
        trailingIcon = {
            IconButton(
                onClick = { onEvent(SellFormEvent.SelectLocation) }
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Select location"
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable { onEvent(SellFormEvent.SelectLocation) },
        isError = showError && formState.validation.locationError != null,
        supportingText = if (showError) formState.validation.locationError?.let { { Text(it) } } else null
    )
}
