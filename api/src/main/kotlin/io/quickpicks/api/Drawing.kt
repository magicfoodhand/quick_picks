package io.quickpicks.api

import org.joda.time.DateTime
import java.util.*

data class BallType(
    val id: Int,
    val abbr: String,
    val name: String
)

data class Ball(
    val id: UUID,
    val ballType: BallType,
    val value: String
)

data class DrawingType(
    val id: Int,
    val abbr: String,
    val name: String
)

data class Drawing(
    val id : UUID,
    val drawingType: DrawingType,
    val drawDate : DateTime,
    val multiplier : String,
    val jackpot : String,
    val created : DateTime,
    val balls: List<Ball> = listOf()
)