package com.example.catchgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.catchgame.game.model.DifficultyLevel
import com.example.catchgame.ui.screens.GameOverScreen
import com.example.catchgame.ui.screens.GameScreen
import com.example.catchgame.ui.screens.MenuScreen
import com.example.catchgame.ui.theme.CatchgameTheme

class MainActivity : ComponentActivity() {

    sealed interface AppScreen {
        data object Menu : AppScreen
        data class Playing(val difficulty: DifficultyLevel, val sessionId: Int) : AppScreen
        data class GameOver(val score: Int, val difficulty: DifficultyLevel) : AppScreen
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CatchgameTheme() {
                var selectedDifficulty by remember { mutableStateOf(DifficultyLevel.EASY) }
                var sessionId by remember { mutableIntStateOf(0) }
                var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Menu) }

                when (val screen = currentScreen) {
                    AppScreen.Menu -> {
                        MenuScreen(
                            selectedDifficulty = selectedDifficulty,
                            onDifficultySelected = { selectedDifficulty = it },
                            onStartGame = {
                                sessionId++
                                currentScreen = AppScreen.Playing(
                                    difficulty = selectedDifficulty,
                                    sessionId = sessionId
                                )
                            }
                        )
                    }

                    is AppScreen.Playing -> {
                        GameScreen(
                            difficultyLevel = screen.difficulty,
                            sessionId = screen.sessionId,
                            onGameOver = { finalScore ->
                                currentScreen = AppScreen.GameOver(
                                    score = finalScore,
                                    difficulty = screen.difficulty
                                )
                            }
                        )
                    }

                    is AppScreen.GameOver -> {
                        GameOverScreen(
                            score = screen.score,
                            onRestart = {
                                sessionId++
                                currentScreen = AppScreen.Playing(
                                    difficulty = screen.difficulty,
                                    sessionId = sessionId
                                )
                            },
                            onBackToMenu = {
                                currentScreen = AppScreen.Menu
                            }
                        )
                    }
                }
            }
        }
    }
}