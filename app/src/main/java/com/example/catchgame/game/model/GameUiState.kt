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
    val rescueChanceUsed: Boolean = false,
<<<<<<< HEAD
    val triviaTimeLeftSeconds: Int = 0,
    val triviaFeedbackMessage: String? = null,
    val isTriviaAnswerLocked: Boolean = false,
    val triviaAnswerWasCorrect: Boolean? = null
=======
    val triviaTimeLeftSeconds: Int = 0
>>>>>>> ac753394584083abda6cebc57966da7fe7c7b46d
)