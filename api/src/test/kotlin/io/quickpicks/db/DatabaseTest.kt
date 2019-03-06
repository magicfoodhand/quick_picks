package io.quickpicks.db

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class DatabaseTest {

    @Test
    fun `run database migrations`() {
        val persistence = Persistence(DbConfig.testConnection())
        persistence.prepareDatabase()

        assertTrue(true)
    }
}
