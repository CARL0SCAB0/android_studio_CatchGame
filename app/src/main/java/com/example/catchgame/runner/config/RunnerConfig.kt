package com.example.catchgame.runner.config

import com.example.catchgame.R
import com.example.catchgame.runner.model.RunnerSegment

object RunnerConfig {

    const val TOTAL_DISTANCE_METERS = 1000f

    /**
     * Velocidad base visual del recorrido.
     * En esta fase solo sirve para avanzar automáticamente.
     */
    const val AUTO_PROGRESS_SPEED_METERS_PER_SECOND = 80f

    val SEGMENTS = listOf(
        RunnerSegment(
            id = "centro_medico",
            title = "Centro Médico",
            drawableRes = R.drawable.segment_01_centro_medico,
            startProgress = 0.0f,
            endProgress = 0.20f
        ),
        RunnerSegment(
            id = "linea_metro",
            title = "Ruta hacia La Raza",
            drawableRes = R.drawable.segment_02_linea_metro,
            startProgress = 0.20f,
            endProgress = 0.45f
        ),
        RunnerSegment(
            id = "tunel_ciencia",
            title = "Túnel de la Ciencia",
            drawableRes = R.drawable.segment_03_tunel_ciencia,
            startProgress = 0.45f,
            endProgress = 0.65f
        ),
        RunnerSegment(
            id = "politecnico",
            title = "Politécnico",
            drawableRes = R.drawable.segment_04_politecnico,
            startProgress = 0.65f,
            endProgress = 0.80f
        ),
        RunnerSegment(
            id = "camino_escom",
            title = "Camino a ESCOM",
            drawableRes = R.drawable.segment_05_camino_escom,
            startProgress = 0.80f,
            endProgress = 1.00f
        )
    )

    fun getSegmentForProgress(progress: Float): RunnerSegment {
        return SEGMENTS.firstOrNull { progress >= it.startProgress && progress < it.endProgress }
            ?: SEGMENTS.last()
    }
}