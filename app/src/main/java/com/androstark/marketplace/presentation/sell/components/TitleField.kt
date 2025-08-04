package com.androstark.marketplace.presentation.sell.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.androstark.marketplace.domain.model.SellFormEvent
import com.androstark.marketplace.domain.model.SellFormState

@Composable
fun TitleField(
    formState: SellFormState,
    onEvent: (SellFormEvent) -> Unit,
    showError: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = formState.title,
        onValueChange = { onEvent(SellFormEvent.TitleChanged(it)) },
        label = { Text("Title *") },
        placeholder = { Text("What are you selling?") },
        modifier = modifier.fillMaxWidth(),
        isError = showError && formState.validation.titleError != null,
        supportingText = if (showError) formState.validation.titleError?.let { { Text(it) } } else null
    )
}
