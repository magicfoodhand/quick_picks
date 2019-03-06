package io.quickpicks.graphql

import com.expedia.graphql.annotations.GraphQLDescription
import io.quickpicks.api.NewDrawing
import io.quickpicks.db.Persistence
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.CompletableFuture

class Mutation(private val persistence: Persistence) : Authenticated() {

    @GraphQLDescription("Create Ball Type")
    fun createBallType(
        @GraphQLDescription("The abbreviation for this Ball Type") abbr: String,
        @GraphQLDescription("The name of the Ball Type") name: String?,
        @GraphQLDescription("Auth Token") authToken: String
    ) = ifAuthenticated(authToken) { persistence.createBallType(abbr, name ?: abbr) }

    @GraphQLDescription("Create Drawing Type")
    fun createDrawingType(
        @GraphQLDescription("The abbreviation for this Drawing Type") abbr: String,
        @GraphQLDescription("The name of the Drawing Type") name: String?,
        @GraphQLDescription("Auth Token") authToken: String
    ) = ifAuthenticated(authToken) {  persistence.createDrawingType(abbr, name ?: abbr) }

    @GraphQLDescription("Create Drawings")
    fun addDrawings(
        @GraphQLDescription("New Drawings") drawings: Array<NewDrawing>,
        @GraphQLDescription("Auth Token") authToken: String
    ) = ifAuthenticated(authToken) {
        drawings.map(this::createDrawing).asSequence().map(CompletableFuture<UUID?>::join).toList()
    }

    @GraphQLDescription("Create Drawing")
    fun addDrawing(
        @GraphQLDescription("New Drawing") drawing: NewDrawing,
        @GraphQLDescription("Auth Token") authToken: String
    ) : UUID? = ifAuthenticated(authToken) { createDrawing(drawing).join() }

    private fun createDrawing(drawing: NewDrawing) : CompletableFuture<UUID?> {
        val (drawDate, drawingTypeId, specialBallTypeId, ballTypeId, special, jackpot, multiplier, balls) = drawing
        return persistence.createDrawing(drawingTypeId, drawDate, jackpot ?: "", multiplier ?: "")
            .thenApply { drawingId ->
                if(drawingId != null) {
                    LOG.info("Created drawing {} - {}", drawingId, balls.map {
                        persistence.createBall(drawingId, it, ballTypeId)
                    }.asSequence()
                    .plus(persistence.createBall(drawingId, special, specialBallTypeId))
                    .map(CompletableFuture<UUID?>::join)
                    .filter { it != null }
                    .toList())
                }
                drawingId
            }
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(Mutation::class.java)
    }
}