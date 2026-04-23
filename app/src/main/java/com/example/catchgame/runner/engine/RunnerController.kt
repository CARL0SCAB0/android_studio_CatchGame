package com.example.catchgame.runner.engine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.catchgame.runner.config.RunnerConfig
import com.example.catchgame.runner.model.RunnerUiState

class RunnerController {

    var uiState by mutableStateOf(
        RunnerUiState(
            currentSegment = RunnerConfig.getSegmentForProgress(0f)
        )
    )
        private set

    fun update(deltaSeconds: Float) {
        if (uiState.isFinished) return

        val newDistance = uiState.distanceMeters +
                RunnerConfig.AUTO_PROGRESS_SPEED_METERS_PER_SECOND * deltaSeconds

        val clampedDistance = newDistance.coerceAtMost(RunnerConfig.TOTAL_DISTANCE_METERS)
        val progress = (clampedDistance / RunnerConfig.TOTAL_DISTANCE_METERS).coerceIn(0f, 1f)
        val isFinished = clampedDistance >= RunnerConfig.TOTAL_DISTANCE_METERS
        val currentSegment = RunnerConfig.getSegmentForProgress(progress)

        uiState = uiState.copy(
            distanceMeters = clampedDistance,
            progress = progress,
            currentSegment = currentSegment,
            isFinished = isFinished
        )
    }

    fun reset() {
        uiState = RunnerUiState(
            currentSegment = RunnerConfig.getSegmentForProgress(0f)
        )
    }
}