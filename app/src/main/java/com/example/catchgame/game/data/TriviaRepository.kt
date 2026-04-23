package com.example.catchgame.game.data

import com.example.catchgame.game.model.TriviaQuestion
import kotlin.random.Random

class TriviaRepository(
    private val questions: List<TriviaQuestion>
) {

    private val usedQuestionIds = mutableSetOf<Int>()

    fun getAllQuestions(): List<TriviaQuestion> = questions

    fun resetSession() {
        usedQuestionIds.clear()
    }

    fun getRandomQuestion(): TriviaQuestion {
        val availableQuestions = questions.filterNot { usedQuestionIds.contains(it.id) }

        val selectedQuestion = if (availableQuestions.isNotEmpty()) {
            availableQuestions.random(Random(System.currentTimeMillis()))
        } else {
            usedQuestionIds.clear()
            questions.random(Random(System.currentTimeMillis()))
        }

        usedQuestionIds.add(selectedQuestion.id)
        return selectedQuestion
    }
}