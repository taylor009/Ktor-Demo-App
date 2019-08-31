package com.demo.webapp

import com.demo.model.*
import com.demo.repository.*
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

const val ABOUT = "/about"
@KtorExperimentalLocationsAPI
@Location(ABOUT)
class About

@KtorExperimentalLocationsAPI
fun Route.about(db: Repository){
    get<About>{
        val user = call.sessions.get<EPSession>()?.let{db.user(it.userId)}
        call.respond(FreeMarkerContent("about.ftl", mapOf("user" to user)))
    }
}