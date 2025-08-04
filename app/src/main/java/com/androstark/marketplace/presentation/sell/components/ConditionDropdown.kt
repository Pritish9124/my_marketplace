package com.androstark.marketplace.presentation.sell.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.androstark.marketplace.domain.model.SellFormEvent
import com.androstark.marketplace.domain.model.SellFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConditionDropdown(
    formState: SellFormState,
    conditions: List<String>,
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
            value = formState.condition,
            onValueChange = {},
            readOnly = true,
            label = { Text("Condition *") },
            placeholder = { Text("Select condition") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            isError = showError && formState.validation.conditionError != null,
            supportingText = if (showError) formState.validation.conditionError?.let { { Text(it) } } else null
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            conditions.forEach { condition ->
                DropdownMenuItem(
                    text = { Text(condition) },
                    onClick = {
                        onEvent(SellFormEvent.ConditionChanged(condition))
                        expanded = false
                    }
                )
            }
        }
    }
}
