package com.andikas.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*

fun Application.configureDoubleReceive() {
    install(DoubleReceive)
}