package io.quickpicks.db

import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PersistenceTest {

    private val persistence = Persistence(DbConfig.testConnection())

    init {
        persistence.prepareDatabase()
    }

    @Test
    fun `create ball type`() {
        assertNotNull(persistence.createBallType("plain", "Plain").join())
        transaction { BallTypes.deleteAll() }
    }

    @Test
    fun `lookup ball type`() {
        val id = persistence.createBallType("plain", "Plain").join()
        val ballType = persistence.ballType("plain").join()
        assertNotNull(ballType)
        assertEquals(id, ballType.id)
        assertEquals("Plain", ballType.name)
        transaction { BallTypes.deleteAll() }
    }

    @Test
    fun `create drawing`() {
        val drawingTypeId = persistence.createDrawingType("lotto", "Lottery").join()!!
        val id = persistence.createDrawing(drawingTypeId, DateTime.now(), "1X", "$1000").join()
        assertNotNull(id)
        transaction { Drawings.deleteAll() }
        transaction { DrawingTypes.deleteAll() }
    }

    @Test
    fun `create ball`() {
        val ballTypeId = persistence.createBallType("plain", "Plain").join()!!
        val drawingTypeId = persistence.createDrawingType("lotto", "Lottery").join()!!
        val drawingId = persistence.createDrawing(drawingTypeId, DateTime.now(), "1X", "$1000").join()!!
        val id = persistence.createBall(drawingId, "43", ballTypeId).join()
        assertNotNull(id)
        transaction { Balls.deleteAll() }
        transaction { BallTypes.deleteAll() }
    }

    @Test
    fun `create drawing type`() {
        assertNotNull(persistence.createDrawingType("lotto", "Lottery").join())
        transaction { DrawingTypes.deleteAll() }
    }

    @Test
    fun `lookup drawing type`() {
        val id = persistence.createDrawingType("lotto", "Lottery").join()
        val drawingType = persistence.drawingType("lotto").join()
        assertNotNull(drawingType)
        assertEquals(id, drawingType.id)
        assertEquals("Lottery", drawingType.name)
        transaction { DrawingTypes.deleteAll() }
    }
}
