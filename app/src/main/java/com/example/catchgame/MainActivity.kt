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
import com.example.catchgame.runner.ui.RunnerScreen
import com.example.catchgame.ui.screens.GameOverScreen
import com.example.catchgame.ui.screens.GameScreen
import com.example.catchgame.ui.screens.MainMenuScreen
import com.example.catchgame.ui.screens.MenuScreen
import com.example.catchgame.ui.theme.CatchgameTheme

class MainActivity : ComponentActivity() {

    sealed interface AppScreen {
        data object MainMenu : AppScreen
        data object CatchMenu : AppScreen
        data class PlayingCatch(val difficulty: DifficultyLevel, val sessionId: Int) : AppScreen
        data class CatchGameOver(val score: Int, val difficulty: DifficultyLevel) : AppScreen
        data class PlayingRunner(val sessionId: Int) : AppScreen
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CatchgameTheme {
                var selectedDifficulty by remember { mutableStateOf(DifficultyLevel.EASY) }
                var catchSessionId by remember { mutableIntStateOf(0) }
                var runnerSessionId by remember { mutableIntStateOf(0) }
                var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.MainMenu) }

                when (val screen = currentScreen) {
                    AppScreen.MainMenu -> {
                        MainMenuScreen(
                            onOpenCatchGame = {
                                currentScreen = AppScreen.CatchMenu
                            },
                            onOpenRunnerGame = {
                                runnerSessionId++
                                currentScreen = AppScreen.PlayingRunner(
                                    sessionId = runnerSessionId
                                )
                            }
                        )
                    }

                    AppScreen.CatchMenu -> {
                        MenuScreen(
                            selectedDifficulty = selectedDifficulty,
                            onDifficultySelected = { selectedDifficulty = it },
                            onStartGame = {
                                catchSessionId++
                                currentScreen = AppScreen.PlayingCatch(
                                    difficulty = selectedDifficulty,
                                    sessionId = catchSessionId
                                )
                            }
                        )
                    }

                    is AppScreen.PlayingCatch -> {
                        GameScreen(
                            difficultyLevel = screen.difficulty,
                            sessionId = screen.sessionId,
                            onGameOver = { finalScore ->
                                currentScreen = AppScreen.CatchGameOver(
                                    score = finalScore,
                                    difficulty = screen.difficulty
                                )
                            }
                        )
                    }

                    is AppScreen.CatchGameOver -> {
                        GameOverScreen(
                            score = screen.score,
                            onRestart = {
                                catchSessionId++
                                currentScreen = AppScreen.PlayingCatch(
                                    difficulty = screen.difficulty,
                                    sessionId = catchSessionId
                                )
                            },
                            onBackToMenu = {
                                currentScreen = AppScreen.MainMenu
                            }
                        )
                    }

                    is AppScreen.PlayingRunner -> {
                        RunnerScreen(
                            sessionId = screen.sessionId,
                            onBackToMenu = {
                                currentScreen = AppScreen.MainMenu
                            }
                        )
                    }
                }
            }
        }
    }
}