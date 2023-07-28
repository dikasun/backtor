package com.andikas.plugins

import com.andikas.routes.auth.authRouting
import com.andikas.routes.auth.userRouting
import com.andikas.routes.fileRouting
import com.andikas.routes.personal.profileRouting
import com.andikas.security.hashing.service.HashingService
import com.andikas.security.token.TokenConfig
import com.andikas.security.token.service.TokenService
import io.imagekit.sdk.ImageKit
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.*
import kotlinx.html.*

fun Application.configureRouting(
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    imageKit: ImageKit
) {
    routing {
        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color("#181818")
                    maxWidth = LinearDimension.maxContent
                    fontFamily = "sans-serif"
                    color = Color("#FAFAFA")
                    margin(256.px, LinearDimension.auto, LinearDimension.auto)
                }
            }
        }
        get("/") {
            call.respondHtml(HttpStatusCode.OK) {
                head {
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                    title {
                        +"Backtor"
                    }
                }
                body {
                    h1 {
                        +"Backtor"
                    }
                    h2 {
                        +"Simple Rest API for my personal website build with Ktor"
                    }
                    h2 {
                        +"Check it out "
                        a(href = "https://andikas.pages.dev") {
                            +"Andikas"
                        }
                    }
                }
            }
        }
        authRouting(hashingService, tokenService, tokenConfig)
        route("/api") {
            profileRouting()
            authenticate("auth-jwt") {
                userRouting(hashingService)
                fileRouting(imageKit)
            }
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}