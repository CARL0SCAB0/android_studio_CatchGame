package com.example.catchgame.game.data

import android.content.Context
import com.example.catchgame.game.model.TriviaCategory
import com.example.catchgame.game.model.TriviaQuestion
import org.json.JSONArray

object TriviaJsonLoader {

    fun loadQuestions(context: Context): List<TriviaQuestion> {
        val jsonString = context.assets
            .open("trivia_questions.json")
            .bufferedReader()
            .use { it.readText() }

        val jsonArray = JSONArray(jsonString)

        return buildList {
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                val optionsArray = jsonObject.getJSONArray("options")
                val options = buildList {
                    for (j in 0 until optionsArray.length()) {
                        add(optionsArray.getString(j))
                    }
                }

                add(
                    TriviaQuestion(
                        id = jsonObject.getInt("id"),
                        category = TriviaCategory.fromValue(jsonObject.getString("category")),
                        question = jsonObject.getString("question"),
                        options = options,
                        correctAnswerIndex = jsonObject.getInt("correctAnswerIndex")
                    )
                )
            }
        }
    }
}