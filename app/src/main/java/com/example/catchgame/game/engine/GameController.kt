package com.example.catchgame.game.engine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.catchgame.game.config.GameConfig
import com.example.catchgame.game.model.DifficultyLevel
import com.example.catchgame.game.model.FallingItem
import com.example.catchgame.game.model.GameUiState
import kotlin.math.max
import kotlin.random.Random

class GameController(
    difficultyLevel: DifficultyLevel
) {

    var uiState by mutableStateOf(
        GameUiState(lives = GameConfig.INITIAL_LIVES)
    )
        private set

    private val difficulty = GameConfig.difficultySettings(difficultyLevel)
    private val random = Random(System.currentTimeMillis())

    private var screenWidthPx = 0f
    private var screenHeightPx = 0f
    private var playerWidthPx = 0f
    private var playerHeightPx = 0f
    private var itemSizePx = 0f
    private var floorMarginPx = 0f

    private var isInitialized = false
    private var targetPlayerX = 0f
    private var spawnAccumulatorMs = 0f
    private var nextItemId = 0L

    fun initializeLayout(
        screenWidthPx: Float,
        screenHeightPx: Float,
        playerWidthPx: Float,
        playerHeightPx: Float,
        itemSizePx: Float,
        floorMarginPx: Float
    ) {
        if (isInitialized) return

        this.screenWidthPx = screenWidthPx
        this.screenHeightPx = screenHeightPx
        this.playerWidthPx = playerWidthPx
        this.playerHeightPx = playerHeightPx
        this.itemSizePx = itemSizePx
        this.floorMarginPx = floorMarginPx

        val initialPlayerX = (screenWidthPx - playerWidthPx) / 2f
        val initialPlayerY = screenHeightPx - playerHeightPx - floorMarginPx

        targetPlayerX = initialPlayerX

        uiState = uiState.copy(
            playerX = initialPlayerX,
            playerY = initialPlayerY
        )

        isInitialized = true
    }

    fun setPlayerTargetByTouch(touchX: Float) {
        if (!isInitialized || uiState.isGameOver) return

        targetPlayerX = touchX - (playerWidthPx / 2f)
        targetPlayerX = targetPlayerX.coerceIn(0f, screenWidthPx - playerWidthPx)
    }

    fun update(deltaSeconds: Float) {
        if (!isInitialized || uiState.isGameOver) return

        updatePlayer(deltaSeconds)
        updateSpawner(deltaSeconds)
        updateItems(deltaSeconds)
    }

    private fun updatePlayer(deltaSeconds: Float) {
        val currentX = uiState.playerX
        val newX = currentX + (targetPlayerX - currentX) * GameConfig.PLAYER_SMOOTHING * deltaSeconds
        val clampedX = newX.coerceIn(0f, screenWidthPx - playerWidthPx)

        uiState = uiState.copy(playerX = clampedX)
    }

    private fun updateSpawner(deltaSeconds: Float) {
        spawnAccumulatorMs += deltaSeconds * 1000f

        while (spawnAccumulatorMs >= difficulty.spawnIntervalMs) {
            spawnAccumulatorMs -= difficulty.spawnIntervalMs
            spawnItem()
        }
    }

    private fun spawnItem() {
        val isBad = random.nextFloat() < difficulty.badObjectProbability

        val drawableRes = if (isBad) {
            GameConfig.BAD_DRAWABLES.random(random)
        } else {
            GameConfig.FOOD_DRAWABLES.random(random)
        }

        val speed = random.nextFloat().let { randomFactor ->
            difficulty.minFallSpeedPxPerSecond +
                    randomFactor * (difficulty.maxFallSpeedPxPerSecond - difficulty.minFallSpeedPxPerSecond)
        }

        val maxX = max(0f, screenWidthPx - itemSizePx)
        val x = random.nextFloat() * maxX

        val item = FallingItem(
            id = nextItemId++,
            x = x,
            y = -itemSizePx,
            speedPxPerSecond = speed,
            drawableRes = drawableRes,
            isBad = isBad
        )

        uiState = uiState.copy(items = uiState.items + item)
    }

    private fun updateItems(deltaSeconds: Float) {
        val updatedItems = mutableListOf<FallingItem>()
        var score = uiState.score
        var lives = uiState.lives
        var gameOver = uiState.isGameOver

        for (item in uiState.items) {
            val movedItem = item.copy(
                y = item.y + item.speedPxPerSecond * deltaSeconds
            )

            if (isCollidingWithPlayer(movedItem)) {
                if (movedItem.isBad) {
                    lives -= 1
                    if (lives <= 0) {
                        lives = 0
                        gameOver = true
                    }
                } else {
                    score += 1
                }
                continue
            }

            if (movedItem.y <= screenHeightPx) {
                updatedItems.add(movedItem)
            }
        }

        uiState = uiState.copy(
            score = score,
            lives = lives,
            items = updatedItems,
            isGameOver = gameOver
        )
    }

    private fun isCollidingWithPlayer(item: FallingItem): Boolean {
        val playerLeft = uiState.playerX
        val playerTop = uiState.playerY
        val playerRight = playerLeft + playerWidthPx
        val playerBottom = playerTop + playerHeightPx

        val itemLeft = item.x
        val itemTop = item.y
        val itemRight = itemLeft + itemSizePx
        val itemBottom = itemTop + itemSizePx

        return itemRight > playerLeft &&
                itemLeft < playerRight &&
                itemBottom > playerTop &&
                itemTop < playerBottom
    }
}