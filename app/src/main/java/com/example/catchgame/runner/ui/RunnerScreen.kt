package com.example.catchgame.runner.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.catchgame.runner.engine.RunnerController

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

                LinearProgressIndicator(
                    progress = { uiState.progress },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(width = 280.dp, height = 12.dp)
                )
            }

            Row(
                modifier = Modifier.align(Alignment.TopEnd),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = {
                    controller.reset()
                }) {
                    Text("Reiniciar")
                }

                Button(onClick = onBackToMenu) {
                    Text("Menú")
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 140.dp, bottom = 160.dp)
                    .size(90.dp)
                    .background(Color(0xCC1E88E5))
            ) {
                Text(
                    text = "RUN",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
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