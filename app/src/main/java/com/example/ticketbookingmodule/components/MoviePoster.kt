package com.example.ticketbookingmodule.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.rememberImagePainter

@Composable
fun MoviePoster(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    // Load and display the image from the URL
    Image(
        painter = rememberImagePainter(imageUrl),
        contentDescription = null,
        modifier = modifier
    )
}

