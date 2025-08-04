package com.androstark.marketplace.domain.usecase

import android.content.Context
import android.net.Uri
import javax.inject.Inject

class ImagePickerUseCase @Inject constructor() {
    
    sealed class ImageSource {
        object Camera : ImageSource()
        object Gallery : ImageSource()
    }
    
    sealed class ImagePickerResult {
        data class Success(val imageUri: Uri) : ImagePickerResult()
        data class Error(val message: String) : ImagePickerResult()
        object Cancelled : ImagePickerResult()
    }
    
    fun getRequiredPermissions(source: ImageSource): List<String> {
        return when (source) {
            ImageSource.Camera -> listOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ImageSource.Gallery -> listOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }
    
    fun createImageUri(context: Context): Uri {
        // Create a temporary file URI for camera capture
        val timeStamp = System.currentTimeMillis()
        val fileName = "marketplace_image_$timeStamp.jpg"
        
        return androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            java.io.File(context.cacheDir, fileName)
        )
    }
    
    fun validateImageSize(context: Context, uri: Uri): Boolean {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val sizeInBytes = inputStream?.available() ?: 0
            inputStream?.close()
            
            // Limit to 10MB
            sizeInBytes <= 10 * 1024 * 1024
        } catch (e: Exception) {
            false
        }
    }
    
    fun compressImage(context: Context, uri: Uri): Uri? {
        return try {
            val bitmap = android.graphics.BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(uri)
            )
            
            val compressedFile = java.io.File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
            val outputStream = java.io.FileOutputStream(compressedFile)
            
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.close()
            
            Uri.fromFile(compressedFile)
        } catch (e: Exception) {
            null
        }
    }
}
