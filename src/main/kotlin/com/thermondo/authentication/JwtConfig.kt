package com.thermondo.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.thermondo.commons.Constants
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/12/21
 */
class JwtConfig {

    private val jwtAlgorithm = Algorithm.HMAC512(Constants.KTOR_TODOLIST_JWT_SECRET)
    private val jwtVerifier: JWTVerifier = JWT
        .require(jwtAlgorithm)
        .withIssuer(Constants.JWT_ISSUER)
        .build()

    /**
     * Generate a token for a authenticated user
     */
    fun generateToken(user: JwtUser): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(Constants.JWT_ISSUER)
        .withClaim(Constants.CLAIM_USERID, user.userId)
        .withClaim(Constants.CLAIM_USERNAME, user.userName)
        .withExpiresAt(expiresAt())
        .sign(jwtAlgorithm)

    /**
     * Configure the jwt ktor authentication feature
     */
    fun configureKtorFeature(config: JWTAuthenticationProvider.Configuration) = with(config) {
        verifier(jwtVerifier)
        realm = Constants.JWT_REALM
        validate {
            val userId = it.payload.getClaim(Constants.CLAIM_USERID).asInt()
            val userName = it.payload.getClaim(Constants.CLAIM_USERNAME).asString()

            if (userId != null && userName != null) {
                JwtUser(userId, userName)
            } else {
                null
            }
        }
    }

    /**
     * POKO, that contains information of an authenticated user that is authenticated via jwt
     */
    data class JwtUser(val userId: Int, val userName: String) : Principal

    private fun expiresAt() = Date(System.currentTimeMillis() + 3_600_000 * 24) // 24 hours
}
