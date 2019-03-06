package io.quickpicks.graphql

import com.expedia.graphql.annotations.GraphQLDescription
import io.quickpicks.db.Persistence

class Query(private val persistence: Persistence) {

    @GraphQLDescription("Get Latest Drawing")
    fun latestDrawing(
        @GraphQLDescription("The abbreviation for this Drawing Type") abbr: String
    ) = persistence.latestDrawing(abbr)

    @GraphQLDescription("All Drawings of a certain type")
    fun drawings(
        @GraphQLDescription("The abbreviation for this Drawing Type") abbr: String
    ) = FilterableList(persistence.drawings(abbr))

    @GraphQLDescription("All Ball Types")
    fun ballTypes() = persistence.ballTypes()

    @GraphQLDescription("All Drawing Types")
    fun drawingTypes() = persistence.drawingTypes()
}