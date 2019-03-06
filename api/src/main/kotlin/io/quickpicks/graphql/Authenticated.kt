package io.quickpicks.graphql

import com.expedia.graphql.annotations.GraphQLContext
import com.expedia.graphql.annotations.GraphQLIgnore
import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * While I like the idea of having a base class handle authentication, this is terrible.
 *
 * Long term goal is to switch to Auth0, so anyone can create an account in order to view historical results
 *
 * Needs to be role based (ex: view information vs add new drawings)
 *
 * Probably should use GraphQLContext to pass value here
 */
open class Authenticated {
    private val authToken: Supplier<String> = Suppliers.memoizeWithExpiration({
        val newToken = "${UUID.randomUUID()}-${UUID.randomUUID()}"
        LOG.info("Current token {}", newToken) // TODO: Use real authentication
        newToken
    }, 30, TimeUnit.MINUTES)

    @GraphQLIgnore
    fun <R> ifAuthenticated(authToken: String, action: () -> R) : R? {
        return if(this.authToken.get() == authToken)
            action()
        else null
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(Authenticated::class.java)
    }
}