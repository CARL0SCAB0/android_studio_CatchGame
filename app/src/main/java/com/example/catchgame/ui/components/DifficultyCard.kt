package com.example.catchgame.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.catchgame.game.model.DifficultyLevel

@Composable
fun DifficultyCard(
    title: String,
    description: String,
    level: DifficultyLevel,
    selectedLevel: DifficultyLevel,
    onSelect: (DifficultyLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = level == selectedLevel

    Column(
        modifier = modifier
            .width(180.dp)
            .background(
                color = if (isSelected) Color(0xAA1E88E5) else Color(0xAA000000),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color.White else Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelect(level) }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = description,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}