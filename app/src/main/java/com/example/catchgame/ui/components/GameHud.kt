package com.example.catchgame.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.catchgame.R
import com.example.catchgame.game.config.GameConfig

@Composable
fun GameHud(
    score: Int,
    lives: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Color(0x66000000))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Puntos: $score",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(lives) {
                Image(
                    painter = painterResource(id = R.drawable.ui_heart),
                    contentDescription = "Vida",
                    modifier = Modifier.size(GameConfig.HEART_SIZE)
                )
            }
        }
    }
}