package com.andikas.plugins

import com.andikas.models.response.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.postgresql.util.PSQLException
import java.sql.BatchUpdateException

fun Application.configureStatusPage() {
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                BaseResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = cause.reasons.joinToString(),
                    data = null
                )
            )
        }
        exception<ExposedSQLException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                BaseResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = if (cause.cause is BatchUpdateException || cause.cause is PSQLException) "Already exist" else cause.message,
                    data = null
                )
            )
        }
        exception<Throwable> { call, cause ->
            if (call.response.status() != HttpStatusCode.Unauthorized) call.respond(
                status = HttpStatusCode.InternalServerError,
                BaseResponse(
                    code = HttpStatusCode.InternalServerError.value,
                    message = cause.message,
                    data = null
                )
            )
        }
        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                status = HttpStatusCode.NotFound,
                BaseResponse(
                    code = HttpStatusCode.NotFound.value,
                    message = status.description,
                    data = null
                )
            )
        }
        status(HttpStatusCode.MethodNotAllowed) { call, _ ->
            call.respond(
                status = HttpStatusCode.MethodNotAllowed,
                BaseResponse(
                    code = HttpStatusCode.MethodNotAllowed.value,
                    message = "${call.request.httpMethod.value} method is not allowed",
                    data = null
                )
            )
        }
        status(HttpStatusCode.UnsupportedMediaType) { call, _ ->
            call.respond(
                status = HttpStatusCode.UnsupportedMediaType,
                BaseResponse(
                    code = HttpStatusCode.UnsupportedMediaType.value,
                    message = "Invalid Request",
                    data = null
                )
            )
        }
    }
}