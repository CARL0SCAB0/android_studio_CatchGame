package com.example.catchgame.game.model

data class FallingItem(
    val id: Long,
    val x: Float,
    val y: Float,
    val speedPxPerSecond: Float,
    val drawableRes: Int,
    val isBad: Boolean
)