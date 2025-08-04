package com.androstark.marketplace.presentation.sell.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.androstark.marketplace.domain.model.Category
import com.androstark.marketplace.domain.model.SellFormEvent
import com.androstark.marketplace.domain.model.SellFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    formState: SellFormState,
    categories: List<Category>,
    onEvent: (SellFormEvent) -> Unit,
    showError: Boolean = false,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = formState.category,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category *") },
            placeholder = { Text("Select category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            isError = showError && formState.validation.categoryError != null,
            supportingText = if (showError) formState.validation.categoryError?.let { { Text(it) } } else null
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        onEvent(SellFormEvent.CategoryChanged(category.name))
                        expanded = false
                    }
                )
            }
        }
    }
}
