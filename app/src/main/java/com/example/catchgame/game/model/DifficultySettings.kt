package com.example.catchgame.game.model

data class DifficultySettings(
    val label: String,
    val spawnIntervalMs: Long,
    val badObjectProbability: Float,
    val minFallSpeedPxPerSecond: Float,
    val maxFallSpeedPxPerSecond: Float
)