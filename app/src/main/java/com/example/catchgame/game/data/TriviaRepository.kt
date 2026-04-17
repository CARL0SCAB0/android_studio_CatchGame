package com.example.catchgame.game.data

import com.example.catchgame.game.model.TriviaQuestion
import kotlin.random.Random

object TriviaRepository {

    private val questions = listOf(
        TriviaQuestion(
            id = 1,
            question = "¿Cuál es la capital de México?",
            options = listOf("Guadalajara", "Monterrey", "Ciudad de México", "Puebla"),
            correctAnswerIndex = 2
        ),
        TriviaQuestion(
            id = 2,
            question = "¿Cuánto es 7 x 8?",
            options = listOf("54", "56", "64", "48"),
            correctAnswerIndex = 1
        ),
        TriviaQuestion(
            id = 3,
            question = "¿Qué planeta es conocido como el planeta rojo?",
            options = listOf("Venus", "Marte", "Júpiter", "Saturno"),
            correctAnswerIndex = 1
        ),
        TriviaQuestion(
            id = 4,
            question = "¿Cuál es el océano más grande del mundo?",
            options = listOf("Atlántico", "Pacífico", "Índico", "Ártico"),
            correctAnswerIndex = 1
        ),
        TriviaQuestion(
            id = 5,
            question = "¿Cuál es el resultado de 15 + 27?",
            options = listOf("42", "41", "43", "40"),
            correctAnswerIndex = 0
        ),
        TriviaQuestion(
            id = 6,
            question = "¿Qué gas respiramos principalmente para vivir?",
            options = listOf("Nitrógeno", "Hidrógeno", "Oxígeno", "Helio"),
            correctAnswerIndex = 2
        )
    )

    fun getRandomQuestion(): TriviaQuestion {
        return questions.random(Random(System.currentTimeMillis()))
    }
}