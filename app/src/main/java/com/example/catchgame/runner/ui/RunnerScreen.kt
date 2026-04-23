package com.example.catchgame.runner.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.catchgame.runner.config.RunnerConfig
import com.example.catchgame.runner.engine.RunnerController
import com.example.catchgame.runner.ui.components.RunnerPlayer
import kotlin.math.roundToInt

@Composable
fun RunnerScreen(
    sessionId: Int,
    onBackToMenu: () -> Unit
) {
    val controller = remember(sessionId) {
        RunnerController()
    }

    val uiState = controller.uiState
    val currentSegment = uiState.currentSegment
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(uiState.isGameOver, uiState.isFinished) {
                detectTapGestures(
                    onTap = {
                        if (!uiState.isGameOver && !uiState.isFinished) {
                            controller.onJumpRequested()
                        }
                    }
                )
            }
    ) {
        val screenWidthPx = with(density) { maxWidth.toPx() }
        val screenHeightPx = with(density) { maxHeight.toPx() }
        val playerHeightPx = with(density) { RunnerConfig.PLAYER_HEIGHT.toPx() }
        val playerWidthPx = with(density) { RunnerConfig.PLAYER_WIDTH.toPx() }
        val groundBottomMarginPx = with(density) { RunnerConfig.GROUND_BOTTOM_MARGIN.toPx() }
        val playerStartXPx = with(density) { RunnerConfig.PLAYER_START_X.toPx() }
        val obstacleWidthPx = with(density) { RunnerConfig.OBSTACLE_WIDTH.toPx() }
        val obstacleHeightPx = with(density) { RunnerConfig.OBSTACLE_HEIGHT.toPx() }

        LaunchedEffect(screenWidthPx, screenHeightPx) {
            controller.initializeLayout(
                screenWidthPx = screenWidthPx,
                screenHeightPx = screenHeightPx,
                playerHeightPx = playerHeightPx,
                groundBottomMarginPx = groundBottomMarginPx,
                playerStartXPx = playerStartXPx,
                playerWidthPx = playerWidthPx,
                obstacleWidthPx = obstacleWidthPx,
                obstacleHeightPx = obstacleHeightPx
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (currentSegment != null) {
                Image(
                    painter = painterResource(id = currentSegment.drawableRes),
                    contentDescription = currentSegment.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = "ESCOM Runner",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )

                    Text(
                        text = "Tramo: ${currentSegment?.title ?: "Sin tramo"}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = "Distancia: ${uiState.distanceMeters.toInt()} m / 1000 m",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )

                    Text(
                        text = if (uiState.isJumping) "Estado: Saltando" else "Estado: Corriendo",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )

                    LinearProgressIndicator(
                        progress = { uiState.progress },
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(width = 280.dp, height = 12.dp)
                    )
                }

                Row(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Button(
                        onClick = { controller.reset() },
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text("Reiniciar")
                    }

                    Button(onClick = onBackToMenu) {
                        Text("Menú")
                    }
                }

                uiState.obstacles.forEach { obstacle ->
                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = obstacle.x.roundToInt(),
                                    y = obstacle.y.roundToInt()
                                )
                            }
                            .size(
                                width = RunnerConfig.OBSTACLE_WIDTH,
                                height = RunnerConfig.OBSTACLE_HEIGHT
                            )
                            .background(
                                color = Color(0xFFD32F2F),
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                }

                RunnerPlayer(
                    modifier = Modifier.offset {
                        IntOffset(
                            x = playerStartXPx.roundToInt(),
                            y = uiState.playerY.roundToInt()
                        )
                    }
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 120.dp)
                        .background(
                            color = Color(0x99000000),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = if (uiState.isGameOver) {
                            "Has chocado"
                        } else {
                            "Toca la pantalla para saltar"
                        },
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                if (uiState.isGameOver) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(Color(0xAA000000))
                            .padding(24.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Game Over",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White
                            )

                            Button(
                                onClick = { controller.reset() },
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                if (uiState.isFinished) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(Color(0xAA000000))
                            .padding(24.dp)
                    ) {
                        Text(
                            text = "Has llegado a ESCOM",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}