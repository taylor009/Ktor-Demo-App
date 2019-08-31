package com.demo.webapp

import com.demo.*
import com.demo.model.*
import com.demo.repository.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

const val PHRASES = "/phrases"

@KtorExperimentalLocationsAPI
@Location(PHRASES)
class Phrases

@KtorExperimentalLocationsAPI
fun Route.phrases(db: Repository, hashFunction: (String) -> String) {

    get<Phrases> {
        val user = call.sessions.get<EPSession>()?.let{db.user(it.userId)}
        if(user == null){
            call.redirect(Signin())
        } else {
            val phrases = db.phrases()
            val date = System.currentTimeMillis()
            val code = call.securityCode(date, user, hashFunction)
            call.respond(FreeMarkerContent("phrases.ftl", mapOf("phrases" to phrases, "user" to user,
                "date" to date, "code" to code), user.userId))
        }
    }
    post<Phrases> {
        val user = call.sessions.get<EPSession>()?.let{db.user(it.userId)}
        val params = call.receiveParameters()
        val date = params["date"]?.toLongOrNull() ?: return@post call.redirect(it)
        val code = params["code"] ?: return@post call.redirect(it)
        if (user == null || !call.verifyCode(date, user, code, hashFunction)){
            call.redirect(Signin())
        }

        when (params["action"] ?: throw IllegalArgumentException("Missing parameter: action")) {
            "delete" -> {
                val id = params["id"] ?: throw IllegalArgumentException("Missing parameter: id")
                db.remove(id)
            }
            "add" -> {
                val emoji = params["emoji"] ?: throw IllegalArgumentException("Missing parameter: emoji")
                val phrase = params["phrase"] ?: throw java.lang.IllegalArgumentException("Missing parameter: phrase")
                db.add(user!!.userId, emoji, phrase)
            }
        }
        call.redirect(Phrases())
    }

}