package com.example.catchgame.runner.engine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.catchgame.runner.config.RunnerConfig
import com.example.catchgame.runner.model.RunnerObstacle
import com.example.catchgame.runner.model.RunnerUiState

class RunnerController {

    var uiState by mutableStateOf(
        RunnerUiState(
            currentSegment = RunnerConfig.getSegmentForProgress(0f)
        )
    )
        private set

    private var isLayoutInitialized = false

    private var screenWidthPx = 0f
    private var obstacleWidthPx = 0f
    private var obstacleHeightPx = 0f
    private var playerStartXPx = 0f
    private var playerWidthPx = 0f
    private var playerHeightPx = 0f

    private var nextObstacleId = 0L
    private var obstacleSpawnAccumulator = 0f

    fun initializeLayout(
        screenWidthPx: Float,
        screenHeightPx: Float,
        playerHeightPx: Float,
        groundBottomMarginPx: Float,
        playerStartXPx: Float,
        playerWidthPx: Float,
        obstacleWidthPx: Float,
        obstacleHeightPx: Float
    ) {
        if (isLayoutInitialized) return

        this.screenWidthPx = screenWidthPx
        this.obstacleWidthPx = obstacleWidthPx
        this.obstacleHeightPx = obstacleHeightPx
        this.playerStartXPx = playerStartXPx
        this.playerWidthPx = playerWidthPx
        this.playerHeightPx = playerHeightPx

        val groundY = screenHeightPx - playerHeightPx - groundBottomMarginPx

        uiState = uiState.copy(
            playerY = groundY,
            groundY = groundY,
            playerVelocityY = 0f,
            isJumping = false,
            obstacles = emptyList(),
            isGameOver = false,
            isFinished = false
        )

        isLayoutInitialized = true
    }

    fun onJumpRequested() {
        if (uiState.isFinished || uiState.isGameOver) return
        if (uiState.isJumping) return

        uiState = uiState.copy(
            playerVelocityY = RunnerConfig.JUMP_FORCE_PX_PER_SECOND,
            isJumping = true
        )
    }

    fun update(deltaSeconds: Float) {
        if (uiState.isFinished || uiState.isGameOver) return

        updateProgress(deltaSeconds)
        updatePlayerPhysics(deltaSeconds)
        updateObstacles(deltaSeconds)
        spawnObstacles(deltaSeconds)
        checkCollisions()
    }

    fun reset() {
        isLayoutInitialized = false
        nextObstacleId = 0L
        obstacleSpawnAccumulator = 0f

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

    private fun spawnObstacles(deltaSeconds: Float) {
        if (!isLayoutInitialized) return

        obstacleSpawnAccumulator += deltaSeconds

        if (obstacleSpawnAccumulator < RunnerConfig.OBSTACLE_SPAWN_INTERVAL_SECONDS) return

        obstacleSpawnAccumulator = 0f

        val obstacle = RunnerObstacle(
            id = nextObstacleId++,
            x = screenWidthPx,
            y = uiState.groundY + (playerHeightPx - obstacleHeightPx),
            width = obstacleWidthPx,
            height = obstacleHeightPx
        )

        uiState = uiState.copy(
            obstacles = uiState.obstacles + obstacle
        )
    }

    private fun updateObstacles(deltaSeconds: Float) {
        val movedObstacles = uiState.obstacles.map { obstacle ->
            obstacle.copy(
                x = obstacle.x - RunnerConfig.OBSTACLE_SPEED_PX_PER_SECOND * deltaSeconds
            )
        }.filter { obstacle ->
            obstacle.x + obstacle.width > 0f
        }

        uiState = uiState.copy(
            obstacles = movedObstacles
        )
    }

    private fun checkCollisions() {
        val playerLeft = playerStartXPx
        val playerTop = uiState.playerY
        val playerRight = playerLeft + playerWidthPx
        val playerBottom = playerTop + playerHeightPx

        val collided = uiState.obstacles.any { obstacle ->
            val obstacleLeft = obstacle.x
            val obstacleTop = obstacle.y
            val obstacleRight = obstacle.x + obstacle.width
            val obstacleBottom = obstacle.y + obstacle.height

            obstacleRight > playerLeft &&
                    obstacleLeft < playerRight &&
                    obstacleBottom > playerTop &&
                    obstacleTop < playerBottom
        }

        if (collided) {
            uiState = uiState.copy(
                isGameOver = true
            )
        }
    }
}