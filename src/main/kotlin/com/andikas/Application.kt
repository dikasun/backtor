package com.andikas

import com.andikas.data.DatabaseFactory
import com.andikas.plugins.*
import com.andikas.security.hashing.service.SHA256HashingService
import com.andikas.security.token.TokenConfig
import com.andikas.security.token.service.JwtTokenService
import com.typesafe.config.ConfigFactory
import io.imagekit.sdk.ImageKit
import io.imagekit.sdk.config.Configuration
import io.ktor.server.application.*
import io.ktor.server.config.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = config.property("jwt.issuer").getString(),
        audience = config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = config.property("jwt.secret").getString(),
        realm = config.property("jwt.realm").getString()
    )
    val hashingService = SHA256HashingService()

    val dbUrl = config.property("db.url").getString()
    val dbUsername = config.property("db.username").getString()
    val dbPassword = config.property("db.password").getString()

    val ikPublicKey = config.property("ik.public.key").getString()
    val ikPrivateKey = config.property("ik.private.key").getString()
    val ikUrlEndpoint = config.property("ik.url").getString()
    val imageKit = ImageKit.getInstance().apply {
        this.config = Configuration(ikPublicKey, ikPrivateKey, ikUrlEndpoint)
    }

    DatabaseFactory.init(dbUrl, dbUsername, dbPassword)
    configureSerialization()
    configureAuthentication(tokenConfig)
    configureDoubleReceive()
    configureCallLogging()
    configureRequestValidation()
    configureStatusPage()
    configureRouting(hashingService, tokenService, tokenConfig, imageKit)
}
