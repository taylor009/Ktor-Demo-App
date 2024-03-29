package com.demo.model

import io.ktor.auth.*
import org.jetbrains.exposed.sql.*

import java.io.Serializable

data class User(
    val userId: String,
    val email: String,
    val displayName: String,
    val passwordHash: String
) : Serializable

object Users: Table() {
    val id = varchar("id", 20).primaryKey()
    val email = varchar("email", 128).uniqueIndex()
    val displayName = varchar("display_name", 256)
    val passwordHash = varchar("password_hash", 64)
}