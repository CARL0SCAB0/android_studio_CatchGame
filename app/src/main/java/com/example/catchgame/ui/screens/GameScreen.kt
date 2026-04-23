package com.example.catchgame.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import com.example.catchgame.R
import com.example.catchgame.game.config.GameConfig
import com.example.catchgame.game.data.TriviaJsonLoader
import com.example.catchgame.game.data.TriviaRepository
import com.example.catchgame.game.engine.GameController
import com.example.catchgame.game.model.DifficultyLevel
import com.example.catchgame.ui.components.GameHud
import com.example.catchgame.ui.components.TriviaDialog
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun GameScreen(
    difficultyLevel: DifficultyLevel,
    sessionId: Int,
    onGameOver: (Int) -> Unit
) {
    val context = LocalContext.current

    val questions = remember {
        TriviaJsonLoader.loadQuestions(context)
    }

    val triviaRepository = remember(questions) {
        TriviaRepository(questions)
    }

    val controller = remember(sessionId) {
        triviaRepository.resetSession()
        GameController(
            difficultyLevel = difficultyLevel,
            triviaRepository = triviaRepository
        )
    }

    val uiState = controller.uiState
    val density = LocalDensity.current

    LaunchedEffect(controller) {
        var lastFrameTimeNanos = 0L

        while (true) {
            withFrameNanos { frameTime ->
                if (lastFrameTimeNanos != 0L) {
                    val deltaSeconds = (frameTime - lastFrameTimeNanos) / 1_000_000_000f
                    controller.update(deltaSeconds)
                }
                lastFrameTimeNanos = frameTime
            }
        }
    }

    LaunchedEffect(uiState.isTriviaVisible, uiState.isTriviaAnswerLocked) {
        while (uiState.isTriviaVisible && !uiState.isGameOver && !uiState.isTriviaAnswerLocked) {
            delay(1000)
            controller.tickTriviaTimer()
        }
    }

    LaunchedEffect(uiState.triviaFeedbackMessage, uiState.isTriviaAnswerLocked) {
        if (uiState.isTriviaVisible && uiState.isTriviaAnswerLocked && uiState.triviaFeedbackMessage != null) {
            delay(1200)
            controller.resolveTriviaAfterFeedback()
        }
    }

    LaunchedEffect(uiState.isGameOver) {
        if (uiState.isGameOver) {
            onGameOver(uiState.score)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(uiState.isTriviaVisible) {
                if (!uiState.isTriviaVisible) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            controller.setPlayerTargetByTouch(offset.x)
                        },
                        onDrag = { change, _ ->
                            controller.setPlayerTargetByTouch(change.position.x)
                        }
                    )
                }
            }
    ) {
        val screenWidthPx = with(density) { maxWidth.toPx() }
        val screenHeightPx = with(density) { maxHeight.toPx() }
        val playerWidthPx = with(density) { GameConfig.PLAYER_WIDTH.toPx() }
        val playerHeightPx = with(density) { GameConfig.PLAYER_HEIGHT.toPx() }
        val itemSizePx = with(density) { GameConfig.ITEM_SIZE.toPx() }
        val floorMarginPx = with(density) { GameConfig.FLOOR_MARGIN.toPx() }

        LaunchedEffect(screenWidthPx, screenHeightPx) {
            controller.initializeLayout(
                screenWidthPx = screenWidthPx,
                screenHeightPx = screenHeightPx,
                playerWidthPx = playerWidthPx,
                playerHeightPx = playerHeightPx,
                itemSizePx = itemSizePx,
                floorMarginPx = floorMarginPx
            )
        }

        Image(
            painter = painterResource(id = R.drawable.bg_game),
            contentDescription = "Fondo del juego",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        uiState.items.forEach { item ->
            Image(
                painter = painterResource(id = item.drawableRes),
                contentDescription = null,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = item.x.roundToInt(),
                            y = item.y.roundToInt()
                        )
                    }
                    .size(GameConfig.ITEM_SIZE)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.player_character),
            contentDescription = "Jugador",
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = uiState.playerX.roundToInt(),
                        y = uiState.playerY.roundToInt()
                    )
                }
                .size(
                    width = GameConfig.PLAYER_WIDTH,
                    height = GameConfig.PLAYER_HEIGHT
                )
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            GameHud(
                score = uiState.score,
                lives = uiState.lives
            )
        }

        if (uiState.isTriviaVisible && uiState.activeTriviaQuestion != null) {
            TriviaDialog(
                question = uiState.activeTriviaQuestion,
                timeLeftSeconds = uiState.triviaTimeLeftSeconds,
                feedbackMessage = uiState.triviaFeedbackMessage,
                isAnswerLocked = uiState.isTriviaAnswerLocked,
                onAnswerSelected = { selectedIndex ->
                    controller.answerTrivia(selectedIndex)
                }
            )
        }
    }
}