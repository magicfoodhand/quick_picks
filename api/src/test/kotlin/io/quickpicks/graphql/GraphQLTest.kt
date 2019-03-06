package io.quickpicks.graphql

import io.quickpicks.db.Persistence
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.util.concurrent.CompletableFuture
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private typealias GraphQLData = Map<String, List<Map<String, Any>>>

class GraphQLTest {

    private val persistence = mockk<Persistence>()
    // All Tests in this class will fail if GraphQL object cannot be built
    private val graphql = graphql(persistence)

    private val drawingsNameQuery = """
            {
                drawings(abbr: "powerball") {
                    all {
                        id
                    }
                }
            }
        """.trimIndent()

    @Test
    fun `base specification - bad query`() {
        val badQuery = graphql.execute("")

        val specification = badQuery.toSpecification()
        assertFalse(specification.isEmpty())
        assertEquals(setOf("extensions", "data", "errors"), specification.keys)

        assertFalse(badQuery.errors.isEmpty())
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `request drawings - no drawing`() {
        every { persistence.drawings(any()) } returns CompletableFuture.completedFuture(emptyList())

        val drawingsResult = graphql.execute(drawingsNameQuery)

        verify(exactly = 1) { persistence.drawings(any()) }

        val specification = drawingsResult.toSpecification()

        val drawingsSpec = specification["data"] as GraphQLData
        val drawings = (drawingsSpec["drawings"] as Map<String, List<Map<String, Any>>>)["all"]

        assertNotNull(drawings)
        assertTrue(drawings.isEmpty())

        clearMocks(persistence)
    }
}