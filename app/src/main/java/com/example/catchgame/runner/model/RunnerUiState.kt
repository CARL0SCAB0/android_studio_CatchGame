package com.example.catchgame.runner.model

data class RunnerUiState(
    val progress: Float = 0f,
    val distanceMeters: Float = 0f,
    val currentSegment: RunnerSegment? = null,
    val isFinished: Boolean = false,
    val playerY: Float = 0f,
    val playerVelocityY: Float = 0f,
    val groundY: Float = 0f,
    val isJumping: Boolean = false
)