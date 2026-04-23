package com.example.catchgame.runner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RunnerPlayer(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 84.dp, height = 104.dp)
            .background(
                color = Color(0xFF1565C0),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Text(
            text = "ES",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}