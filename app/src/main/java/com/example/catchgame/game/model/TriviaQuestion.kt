package com.example.catchgame.game.model

data class TriviaQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)