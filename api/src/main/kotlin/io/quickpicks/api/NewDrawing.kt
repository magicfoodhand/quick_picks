package io.quickpicks.api

import com.expedia.graphql.annotations.GraphQLDescription
import org.joda.time.DateTime

@GraphQLDescription("Create a new Drawing")
data class NewDrawing(
    @GraphQLDescription("The date and time of the drawing") val drawDate: DateTime,
    @GraphQLDescription("The id for this Drawing Type") val drawingTypeId: Int,
    @GraphQLDescription("The ball type Id of the special") val specialBallTypeId: Int,
    @GraphQLDescription("The ball type Id of the rest") val ballTypeId: Int,
    @GraphQLDescription("The last number; ex: megaball, powerball") val special: String,
    @GraphQLDescription("The jackpot") val jackpot: String?,
    @GraphQLDescription("The multiplier") val multiplier: String?,
    @GraphQLDescription("The balls in this drawing") val balls: List<String>
)