package io.quickpicks.db

import io.quickpicks.api.Ball
import io.quickpicks.api.BallType
import io.quickpicks.api.Drawing
import io.quickpicks.api.DrawingType
import io.quickpicks.extensions.FutureList
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.sql.DataSource

class Persistence(private val dataSource: DataSource) {

    fun prepareDatabase() {
        val currentTime = System.currentTimeMillis()
        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(BallTypes, DrawingTypes, Drawings, Balls)
        }

        LOG.info("Database migrations took {} ms", System.currentTimeMillis() - currentTime)
    }

    fun createBallType(abbr: String, name: String): CompletableFuture<Int?> {
        return BallTypes.save(BallTypes.id) {
            it[this.abbr] = abbr
            it[this.name] = name
        }
    }

    fun createDrawingType(abbr: String, name: String): CompletableFuture<Int?> {
        return DrawingTypes.save(DrawingTypes.id) {
            it[this.abbr] = abbr
            it[this.name] = name
        }
    }

    fun createBall(drawingId: UUID, value: String, ballTypeId: Int): CompletableFuture<UUID?> {
        return Balls.save(Balls.id) {
            it[this.drawingId] = drawingId
            it[this.value] = value
            it[this.ballTypeId] = ballTypeId
        }
    }

    fun createDrawing(
        drawingTypeId: Int, drawDate: DateTime, multiplier: String, jackpot: String)
    : CompletableFuture<UUID?> {
        return Drawings.save(Drawings.id) {
            it[this.drawingTypeId] = drawingTypeId
            it[this.drawDate] = drawDate
            it[this.multiplier] = multiplier
            it[this.jackpot] = jackpot
        }
    }

    fun latestDrawing(drawingTypeAbbr: String): CompletableFuture<Drawing?> {
        return drawingType(drawingTypeAbbr).thenApply { drawingType ->
            if(drawingType != null)
                transaction {
                    Drawings.slice(Drawings.id, Drawings.drawingTypeId, Drawings.drawDate, Drawings.multiplier,
                        Drawings.jackpot, Drawings.created)
                    .select { Drawings.drawingTypeId eq drawingType.id }
                    .orderBy(Drawings.id, false)
                    .limit(1)
                    .map {
                        val id = it[Drawings.id]
                        Drawing(
                            id,
                            drawingType,
                            it[Drawings.drawDate],
                            it[Drawings.multiplier],
                            it[Drawings.jackpot],
                            it[Drawings.created],
                            balls(id).join()
                        )
                    }.firstOrNull()
                }
            else
                null
        }
    }

    private fun balls(id: UUID) = Balls.query(Balls.id, Balls.ballTypeId, Balls.value,
        where = { Balls.drawingId eq id},
        adapter = { Ball(it[Balls.id], ballType(it[Balls.ballTypeId]).join()!!, it[Balls.value]) })

    fun ballTypes() = BallTypes.where { BallTypes.abbr eq BallTypes.abbr }

    fun drawingTypes() = DrawingTypes.where { DrawingTypes.abbr eq DrawingTypes.abbr }

    fun ballType(id: Int): CompletableFuture<BallType?> {
        return BallTypes.where { BallTypes.id eq id }.thenApply { it.firstOrNull() }
    }

    fun ballType(abbr: String): CompletableFuture<BallType?> {
        return BallTypes.where { BallTypes.abbr eq abbr }.thenApply { it.firstOrNull() }
    }

    fun drawingType(abbr: String): CompletableFuture<DrawingType?> {
        return DrawingTypes.where { DrawingTypes.abbr eq abbr}.thenApply { it.firstOrNull() }
    }

    fun drawings(drawingTypeAbbr: String): FutureList<Drawing> {
        return drawingType(drawingTypeAbbr).thenApply { drawingType ->
            if(drawingType != null)
                Drawings.query(
                    Drawings.id, Drawings.drawingTypeId, Drawings.drawDate,
                    Drawings.multiplier, Drawings.jackpot, Drawings.created,
                    where = { Drawings.drawingTypeId eq drawingType.id },
                    adapter = {
                        Drawing(
                            it[Drawings.id],
                            drawingType,
                            it[Drawings.drawDate],
                            it[Drawings.multiplier],
                            it[Drawings.jackpot],
                            it[Drawings.created]
                        )
                    }
                )
            else
                CompletableFuture.completedFuture(emptyList())
        }.join()
    }

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(Persistence::class.java)
    }
}