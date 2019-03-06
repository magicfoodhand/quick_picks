package io.quickpicks.graphql

import com.expedia.graphql.annotations.GraphQLDescription
import io.quickpicks.api.Drawing
import io.quickpicks.extensions.FutureList

// TODO: Make this work with generics, maybe create a Custom GraphQL Type...
typealias T = Drawing

@GraphQLDescription("List Filtering")
class FilterableList(
    private val resolver: FutureList<T>
) {
    @GraphQLDescription("Get Everything")
    fun all() : FutureList<T> = resolver

    @GraphQLDescription("Skip the First `number` Elements")
    fun skip(number: Int) = all().thenApply { it.drop(number) }

    @GraphQLDescription("Take the First `number` Elements")
    fun first(number: Int = 1) = all().thenApply { it.take(number) }

    @GraphQLDescription("Take the Last `number` Elements")
    fun last(number: Int = 1) = all().thenApply { it.takeLast(number) }

    @GraphQLDescription("Take All Except for the Last `number` of Elements")
    fun leave(number: Int) = all().thenApply { it.dropLast(number) }

    @GraphQLDescription("Get Item at `index`")
    fun get(index: Int) = all().thenApply { it[index] }

    @GraphQLDescription("Take Elements Between `start` and `end`")
    fun subset(start: Int, end: Int) = all().thenApply { it.subList(start, end) }
}