package com.demo

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import java.util.*

class JwtService {
    private val issuer = "emojiphrases"
    private val jwtSecret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    private fun expiresAt() = Date(System.currentTimeMillis() + 3_600_000 * 24) // 24 hours
}