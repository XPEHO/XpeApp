package com.xpeho.xpeapp.presentation.componants

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun FirestoreImage(
    imageUrl: String,
) {
    var painter by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(imageUrl) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child(
            imageUrl
        )

        try {
            val byteArray = withContext(Dispatchers.IO) {
                storageRef.getBytes(1024 * 1024).await()
            }

            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            painter = bitmap.asImageBitmap()
        } catch (e: Exception) {
            // Gérer les erreurs ici
            e.printStackTrace()
        }
    }

    painter?.let {
        Image(
            bitmap = it,
            contentDescription = "Firestore Image"
        )
    } ?: run {
        AppLoader()
    }
}
