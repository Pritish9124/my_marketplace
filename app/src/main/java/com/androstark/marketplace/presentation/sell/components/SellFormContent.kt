package com.androstark.marketplace.presentation.sell.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androstark.marketplace.domain.model.Category
import com.androstark.marketplace.domain.model.SellFormEvent
import com.androstark.marketplace.domain.model.SellFormState

@Composable
fun SellFormContent(
    formState: SellFormState,
    categories: List<Category>,
    conditions: List<String>,
    onEvent: (SellFormEvent) -> Unit,
    showValidationErrors: Boolean = false,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title field
        item {
            TitleField(
                formState = formState,
                onEvent = onEvent,
                showError = showValidationErrors
            )
        }

        // Description field
        item {
            DescriptionField(
                formState = formState,
                onEvent = onEvent,
                showError = showValidationErrors
            )
        }

        // Price field
        item {
            PriceField(
                formState = formState,
                onEvent = onEvent,
                showError = showValidationErrors
            )
        }

        // Category dropdown
        item {
            CategoryDropdown(
                formState = formState,
                categories = categories,
                onEvent = onEvent,
                showError = showValidationErrors
            )
        }

        // Condition dropdown
        item {
            ConditionDropdown(
                formState = formState,
                conditions = conditions,
                onEvent = onEvent,
                showError = showValidationErrors
            )
        }

        // Location field
        item {
            LocationField(
                formState = formState,
                onEvent = onEvent,
                showError = showValidationErrors
            )
        }

        // Images section
        item {
            ImagePickerSection(
                formState = formState,
                onEvent = onEvent,
                showError = showValidationErrors
            )
        }
    }
}
