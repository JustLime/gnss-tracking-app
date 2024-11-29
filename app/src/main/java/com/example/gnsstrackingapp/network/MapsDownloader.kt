package com.example.gnsstrackingapp.network

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri

class MapsDownloader(private val context: Context) {
    private val downloadManager: DownloadManager =
        context.getSystemService(DownloadManager::class.java)

    companion object {
        const val REQUEST_PERMISSION = 1001
    }

    // Function to save file using DownloadManager
    fun saveFileToPublicDirectory(fileUrl: String, fileName: String): Long {
        // For Android 11 and above, use MediaStore API to save to a public folder
        val folderName = "maps" // The folder within the Downloads folder

        // Create a ContentValues object to specify file information
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                "Download/$folderName/" // Custom folder inside "Download"
            )
        }

        // Insert the file metadata into MediaStore
        val downloadUri = context.contentResolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues
        )

        // Check if the URI was created successfully
        if (downloadUri != null) {
            try {
                // Download the file using DownloadManager
                val fileUri = fileUrl.toUri()
                val request = DownloadManager.Request(fileUri).apply {
                    setTitle(fileName)
                    setDescription("Downloading $fileName")
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    setDestinationUri(downloadUri)
                }

                Log.d("fileUri", fileUri.toString())
                Log.d("downloadUri", downloadUri.toString())

                return downloadManager.enqueue(request) // Enqueue the download request
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MapsDownloader", "Failed to download file: ${e.message}")
                Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                return -1
            }
        } else {
            // If the download URI is null, show a message to the user
            Toast.makeText(context, "Failed to create custom folder", Toast.LENGTH_SHORT).show()
            return -1
        }
    }
}
