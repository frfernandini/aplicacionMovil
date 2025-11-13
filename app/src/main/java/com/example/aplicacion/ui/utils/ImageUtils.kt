package com.example.aplicacion.ui.utils

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun rememberBitmapFromBase64(base64String: String?): ImageBitmap? {
    return remember(base64String) {
        if (base64String == null) {
            return@remember null
        }

        try {

            val commaIndex = base64String.indexOf(',')


            val pureBase64 = if (commaIndex != -1) {
                base64String.substring(commaIndex + 1)
            } else {
                base64String
            }


            val imageBytes = Base64.decode(pureBase64, Base64.DEFAULT)



            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.asImageBitmap()

        } catch (e: Exception) { // Capturamos cualquier excepción (ej. IllegalArgumentException)
            null
        }
    }
}

