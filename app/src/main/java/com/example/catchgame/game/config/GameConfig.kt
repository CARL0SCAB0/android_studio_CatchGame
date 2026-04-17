package com.example.catchgame.game.config

import androidx.compose.ui.unit.dp
import com.example.catchgame.R
import com.example.catchgame.game.model.DifficultyLevel
import com.example.catchgame.game.model.DifficultySettings

object GameConfig {

    const val INITIAL_LIVES = 3

    val PLAYER_WIDTH = 92.dp
    val PLAYER_HEIGHT = 92.dp
    val ITEM_SIZE = 56.dp
    val HEART_SIZE = 28.dp
    val FLOOR_MARGIN = 16.dp

    /**
     * Controla la suavidad del movimiento del personaje.
     * Un valor mayor hace que llegue más rápido al punto objetivo.
     */
    const val PLAYER_SMOOTHING = 10f

    val FOOD_DRAWABLES = listOf(
        R.drawable.food_apple,
        R.drawable.food_banana,
        R.drawable.food_burger,
        R.drawable.food_cake
    )

    val BAD_DRAWABLES = listOf(
        R.drawable.bad_bomb,
        R.drawable.bad_poison,
        R.drawable.bad_rock
    )

    fun difficultySettings(level: DifficultyLevel): DifficultySettings {
        return when (level) {
            DifficultyLevel.EASY -> DifficultySettings(
                label = "Fácil",
                spawnIntervalMs = 900L,
                badObjectProbability = 0.25f,
                minFallSpeedPxPerSecond = 260f,
                maxFallSpeedPxPerSecond = 380f
            )

            DifficultyLevel.MEDIUM -> DifficultySettings(
                label = "Medio",
                spawnIntervalMs = 700L,
                badObjectProbability = 0.45f,
                minFallSpeedPxPerSecond = 320f,
                maxFallSpeedPxPerSecond = 480f
            )

            DifficultyLevel.HARD -> DifficultySettings(
                label = "Difícil",
                spawnIntervalMs = 520L,
                badObjectProbability = 0.65f,
                minFallSpeedPxPerSecond = 400f,
                maxFallSpeedPxPerSecond = 620f
            )
        }
    }
}