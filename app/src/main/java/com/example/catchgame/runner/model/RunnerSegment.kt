package com.example.catchgame.runner.model

data class RunnerSegment(
    val id: String,
    val title: String,
    val drawableRes: Int,
    val startProgress: Float,
    val endProgress: Float
)