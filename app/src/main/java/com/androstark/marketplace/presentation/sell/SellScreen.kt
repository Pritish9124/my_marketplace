package com.androstark.marketplace.presentation.sell

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androstark.marketplace.domain.model.Category
import com.androstark.marketplace.domain.model.SellFormEvent
import com.androstark.marketplace.presentation.common.UiState
import com.androstark.marketplace.presentation.sell.components.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SellScreen(
    onBackClick: () -> Unit,
    onNavigateToLocationPicker: () -> Unit,
    onNavigateToAdDetails: (Int) -> Unit,
    viewModel: SellViewModel = hiltViewModel()
) {
    LocalContext.current
    val categoriesState by viewModel.categoriesState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle()
    val showValidationErrors by viewModel.showValidationErrors.collectAsStateWithLifecycle()
    var showImageSourceDialog by remember { mutableStateOf(false) }

    // Permission states (idiomatic Compose)
    val cameraPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )
    val galleryPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )

    // Handle one-off UI events
    LaunchedEffect(uiEvent) {
        when (val event = uiEvent) {
            is SellUiEvent.NavigateToLocationPicker -> onNavigateToLocationPicker()
            is SellUiEvent.ShowImagePicker -> showImageSourceDialog = true
            is SellUiEvent.NavigateToAdDetails -> onNavigateToAdDetails(event.adId)
            is SellUiEvent.ShowError -> {
                // TODO: Show error snackbar (implement as needed)
            }

            is SellUiEvent.ShowSuccess -> {
                // TODO: Show success snackbar or navigate
            }

            null -> {}
        }
        // Optionally clear UI event if needed: viewModel.clearUiEvent()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sell Item") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.onEvent(SellFormEvent.SubmitForm) },
                        enabled = formState.validation.isValid
                    ) {
                        Text(
                            text = "Publish",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            when (categoriesState) {
                is UiState.Loading -> LoadingState()
                is UiState.Error -> ErrorState(onRetry = { /* Add a retry event if needed */ })
                is UiState.Success -> SellFormContent(
                    formState = formState,
                    categories = (categoriesState as UiState.Success<List<Category>>).data,
                    conditions = viewModel.conditions,
                    onEvent = viewModel::onEvent,
                    showValidationErrors = showValidationErrors
                )

                is UiState.Empty -> {
                    // Optionally handle empty state
                }
            }
            // Image source dialog (modal)
            val context = LocalContext.current
            ImageSourceDialog(
                showDialog = showImageSourceDialog,
                onDismiss = { showImageSourceDialog = false },
                onImageSelected = { uri -> viewModel.onImageSelected(uri, context) },
                cameraPermissions = cameraPermissions,
                galleryPermissions = galleryPermissions
            )
        }
    }
}

