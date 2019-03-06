package io.quickpicks.graphql

import com.expedia.graphql.annotations.GraphQLDescription
import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import io.quickpicks.api.NewDrawing
import io.quickpicks.db.Persistence
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class Mutation(private val persistence: Persistence) {

    private val authToken: Supplier<String> = Suppliers.memoizeWithExpiration({
        val newToken = "${UUID.randomUUID()}-${UUID.randomUUID()}"
        LOG.info("Current token {}", newToken) // TODO: Use real authentication
        newToken
    }, 30, TimeUnit.MINUTES)

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

    private fun <R> ifAuthenticated(authToken: String, action: () -> R) : R? {
        return if(this.authToken.get() == authToken)
            action()
        else null
    }

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