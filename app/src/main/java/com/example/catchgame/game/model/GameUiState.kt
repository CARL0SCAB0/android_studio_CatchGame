package com.example.catchgame.game.model

data class GameUiState(
    val score: Int = 0,
    val lives: Int = 3,
    val playerX: Float = 0f,
    val playerY: Float = 0f,
    val items: List<FallingItem> = emptyList(),
    val isGameOver: Boolean = false,
    val activeTriviaQuestion: TriviaQuestion? = null,
    val isTriviaVisible: Boolean = false,
    val rescueChanceUsed: Boolean = false
)