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

    private var isLayoutInitialized = false

    fun initializeLayout(
        screenHeightPx: Float,
        playerHeightPx: Float,
        groundBottomMarginPx: Float
    ) {
        if (isLayoutInitialized) return

        val groundY = screenHeightPx - playerHeightPx - groundBottomMarginPx

        uiState = uiState.copy(
            playerY = groundY,
            groundY = groundY,
            playerVelocityY = 0f,
            isJumping = false
        )

        isLayoutInitialized = true
    }

    fun onJumpRequested() {
        if (uiState.isFinished) return
        if (uiState.isJumping) return

        uiState = uiState.copy(
            playerVelocityY = RunnerConfig.JUMP_FORCE_PX_PER_SECOND,
            isJumping = true
        )
    }

    fun update(deltaSeconds: Float) {
        if (uiState.isFinished) return

        updateProgress(deltaSeconds)
        updatePlayerPhysics(deltaSeconds)
    }

    fun reset() {
        isLayoutInitialized = false
        uiState = RunnerUiState(
            currentSegment = RunnerConfig.getSegmentForProgress(0f)
        )
    }

    private fun updateProgress(deltaSeconds: Float) {
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

    private fun updatePlayerPhysics(deltaSeconds: Float) {
        if (!isLayoutInitialized) return

        val nextVelocity = uiState.playerVelocityY +
                RunnerConfig.GRAVITY_PX_PER_SECOND * deltaSeconds

        val nextY = uiState.playerY + nextVelocity * deltaSeconds

        if (nextY >= uiState.groundY) {
            uiState = uiState.copy(
                playerY = uiState.groundY,
                playerVelocityY = 0f,
                isJumping = false
            )
        } else {
            uiState = uiState.copy(
                playerY = nextY,
                playerVelocityY = nextVelocity,
                isJumping = true
            )
        }
    }
}