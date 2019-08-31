package com.demo.webapp

import com.demo.*
import com.demo.model.*
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.sessions.*

const val SIGNOUT = "/signout"

@KtorExperimentalLocationsAPI
@Location(SIGNOUT)
class Signout

@KtorExperimentalLocationsAPI
fun Route.signout(){
    get<Signout>{
        call.sessions.clear<EPSession>()
        call.redirect(Signin())
    }
}