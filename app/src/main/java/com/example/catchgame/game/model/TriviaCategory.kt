package com.example.catchgame.game.model

enum class TriviaCategory(val displayName: String) {
    MATH("Matemáticas"),
    GEOGRAPHY("Geografía"),
    SCIENCE("Ciencia"),
    HISTORY("Historia");

    companion object {
        fun fromValue(value: String): TriviaCategory {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: SCIENCE
        }
    }
}