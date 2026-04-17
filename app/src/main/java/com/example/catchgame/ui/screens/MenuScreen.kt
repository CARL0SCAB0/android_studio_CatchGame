package com.example.catchgame.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.catchgame.R
import com.example.catchgame.game.model.DifficultyLevel
import com.example.catchgame.ui.components.DifficultyCard

@Composable
fun MenuScreen(
    selectedDifficulty: DifficultyLevel,
    onDifficultySelected: (DifficultyLevel) -> Unit,
    onStartGame: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = "Fondo menú",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Catch Game",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                Text(
                    text = "Selecciona la dificultad",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DifficultyCard(
                    title = "Fácil",
                    description = "Más comida,\nmenos peligro",
                    level = DifficultyLevel.EASY,
                    selectedLevel = selectedDifficulty,
                    onSelect = onDifficultySelected
                )

                DifficultyCard(
                    title = "Medio",
                    description = "Equilibrado",
                    level = DifficultyLevel.MEDIUM,
                    selectedLevel = selectedDifficulty,
                    onSelect = onDifficultySelected
                )

                DifficultyCard(
                    title = "Difícil",
                    description = "Más peligro,\nmás velocidad",
                    level = DifficultyLevel.HARD,
                    selectedLevel = selectedDifficulty,
                    onSelect = onDifficultySelected
                )
            }

            Button(onClick = onStartGame) {
                Text(text = "Iniciar juego")
            }
        }
    }
}