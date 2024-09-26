package com.example.ticketbookingmodule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SeatItem(
    isSelected: Boolean,
    isReserved: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isReserved -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.surface
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .background(color = backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = if (isReserved) "R" else "A", color = Color.White)
    }
}