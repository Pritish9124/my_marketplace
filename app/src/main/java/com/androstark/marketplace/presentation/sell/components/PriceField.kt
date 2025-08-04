package com.androstark.marketplace.presentation.sell.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.androstark.marketplace.domain.model.SellFormEvent
import com.androstark.marketplace.domain.model.SellFormState

@Composable
fun PriceField(
    formState: SellFormState,
    onEvent: (SellFormEvent) -> Unit,
    showError: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = formState.price,
            onValueChange = { onEvent(SellFormEvent.PriceChanged(it)) },
            label = { Text("Price *") },
            placeholder = { Text("Enter price") },
            leadingIcon = {
                Text(
                    text = "₹",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = showError && formState.validation.priceError != null,
            supportingText = if (showError) formState.validation.priceError?.let { { Text(it) } } else null
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = formState.isNegotiable,
                onCheckedChange = { onEvent(SellFormEvent.NegotiableChanged(it)) }
            )
            Text(
                text = "Price is negotiable",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
