package com.academy.manager.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlin.math.max
import kotlin.math.roundToInt

object ImageUtils {

    private const val MAX_DIMENSION_PX = 512

    suspend fun saveCompressedImage(
        context: Context,
        sourceUri: Uri,
        subDir: String
    ): String? = withContext(Dispatchers.IO) {
        try {
            val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(sourceUri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, boundsOptions)
            }

            val srcWidth = boundsOptions.outWidth
            val srcHeight = boundsOptions.outHeight
            if (srcWidth <= 0 || srcHeight <= 0) return@withContext null

            var sampleSize = 1
            while (srcWidth / sampleSize > MAX_DIMENSION_PX * 2 ||
                srcHeight / sampleSize > MAX_DIMENSION_PX * 2
            ) {
                sampleSize *= 2
            }

            val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
            val decoded = context.contentResolver.openInputStream(sourceUri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, decodeOptions)
            } ?: return@withContext null

            val maxSide = max(decoded.width, decoded.height)
            val scaled = if (maxSide > MAX_DIMENSION_PX) {
                val scale = MAX_DIMENSION_PX.toFloat() / maxSide.toFloat()
                val targetW = (decoded.width * scale).roundToInt().coerceAtLeast(1)
                val targetH = (decoded.height * scale).roundToInt().coerceAtLeast(1)
                Bitmap.createScaledBitmap(decoded, targetW, targetH, true).also {
                    if (it != decoded) decoded.recycle()
                }
            } else {
                decoded
            }

            val dir = File(context.filesDir, subDir).apply { mkdirs() }
            val destFile = File(dir, "${UUID.randomUUID()}.jpg")
            FileOutputStream(destFile).use { out ->
                scaled.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }
            scaled.recycle()
            destFile.absolutePath
        } catch (_: Exception) {
            null
        }
    }

    fun loadBitmapFromPath(path: String?): Bitmap? {
        if (path.isNullOrBlank()) return null
        val file = File(path)
        if (!file.exists()) return null
        return try {
            BitmapFactory.decodeFile(file.absolutePath)
        } catch (_: Exception) {
            null
        }
    }

    fun deleteImageFile(path: String?) {
        if (path.isNullOrBlank()) return
        try {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        } catch (_: Exception) {
        }
    }
}
