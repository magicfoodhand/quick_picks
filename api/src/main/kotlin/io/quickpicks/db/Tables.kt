package io.quickpicks.db

import io.quickpicks.api.BallType
import io.quickpicks.api.DrawingType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime
import java.util.*

object Balls : Table() {
    val id = uuid("id").primaryKey().clientDefault { UUID.randomUUID() }
    val ballTypeId = integer("ball_type_id").index().references(BallTypes.id)
    val drawingId = uuid("drawing_id").index().references(Drawings.id)
    val value = varchar("multiplier", 4).index()
    val created = datetime("created").index().clientDefault { DateTime.now() }

    private val uniqueIndex = uniqueIndex(value, drawingId, ballTypeId)
}

object BallTypes : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val abbr = varchar("abbr", 32).uniqueIndex()
    val name = text("name")
    val created = datetime("created").index().clientDefault { DateTime.now() }

    fun where(clause: SqlExpressionBuilder.() -> Op<Boolean>) = query(
        id, abbr, name, where = clause, adapter = {
            BallType(it[id], it[abbr], it[name])
        }
    )
}

object Drawings : Table() {
    val id = uuid("id").primaryKey().clientDefault { UUID.randomUUID() }
    val drawingTypeId = integer("drawing_type_id").index().references(DrawingTypes.id)
    val drawDate = datetime("draw_date").index()
    val multiplier = varchar("multiplier", 4).index()
    val jackpot = varchar("jackpot", 16).index()
    val created = datetime("created").index().clientDefault { DateTime.now() }

    private val uniqueIndex = uniqueIndex(drawDate, drawingTypeId)
}

object DrawingTypes : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val abbr = varchar("abbr", 32).uniqueIndex()
    val name = text("name")
    val created = datetime("created").index().clientDefault { DateTime.now() }

    fun where(clause: SqlExpressionBuilder.() -> Op<Boolean>) = query(
        id, abbr, name, where = clause, adapter = {
            DrawingType(it[id], it[abbr], it[name])
        }
    )
}