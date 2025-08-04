package com.androstark.marketplace.presentation.sell.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androstark.marketplace.domain.model.SellFormEvent
import com.androstark.marketplace.domain.model.SellFormState

@Composable
fun DescriptionField(
    formState: SellFormState,
    onEvent: (SellFormEvent) -> Unit,
    showError: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = formState.description,
        onValueChange = { onEvent(SellFormEvent.DescriptionChanged(it)) },
        label = { Text("Description *") },
        placeholder = { Text("Describe your item...") },
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        maxLines = 5,
        isError = showError && formState.validation.descriptionError != null,
        supportingText = if (showError) formState.validation.descriptionError?.let { { Text(it) } } else null
    )
}
