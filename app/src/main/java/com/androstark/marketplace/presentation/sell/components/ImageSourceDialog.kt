package com.androstark.marketplace.presentation.sell.components

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImageSourceDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onImageSelected: (Uri) -> Unit,
    cameraPermissions: MultiplePermissionsState,
    galleryPermissions: MultiplePermissionsState
) {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
        onDismiss()
    }
    
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Select Image Source") },
            text = { Text("Choose how you want to add a photo") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        if (cameraPermissions.allPermissionsGranted) {
                            // Launch camera - would need camera launcher implementation
                        } else {
                            cameraPermissions.launchMultiplePermissionRequest()
                        }
                    }
                ) {
                    Text("Camera")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        if (galleryPermissions.allPermissionsGranted) {
                            galleryLauncher.launch("image/*")
                        } else {
                            galleryPermissions.launchMultiplePermissionRequest()
                        }
                    }
                ) {
                    Text("Gallery")
                }
            }
        )
    }
}
