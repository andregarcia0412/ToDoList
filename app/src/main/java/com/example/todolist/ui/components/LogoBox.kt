package com.example.todolist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LogoBox(
    boxSize: Dp = 200.dp,
    imageSize: Dp = 120.dp
) {
    Box(
        modifier = Modifier
            .size(boxSize)
            .background(color = Color(0xFF1C1B1B), shape = RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = Color(0xFF393939),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ListAlt,
            contentDescription = "Logo UNIFOR",
            modifier = Modifier
                .size(imageSize),
            tint = Color(0xFFE4F2FE)
        )
    }
}