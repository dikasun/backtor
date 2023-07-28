package com.andikas.routes.auth

import com.andikas.data.dao.auth.userDao
import com.andikas.models.request.auth.AuthBody
import com.andikas.models.response.BaseResponse
import com.andikas.models.response.auth.AuthResponse
import com.andikas.models.response.auth.UserResponse
import com.andikas.security.hashing.SaltedHash
import com.andikas.security.hashing.service.HashingService
import com.andikas.security.token.TokenClaim
import com.andikas.security.token.TokenConfig
import com.andikas.security.token.service.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRouting(
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    route("/auth") {
        post("signup") {
            val authBody = call.receive<AuthBody>()

            val isUsernameTaken = userDao.userByUsername(authBody.username)
            if (isUsernameTaken != null) return@post call.respond(
                status = HttpStatusCode.BadRequest,
                BaseResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = "Username taken",
                    data = null
                )
            )

            val saltedHash = hashingService.generateSaltedHash(authBody.password)
            val user = UserResponse(
                username = authBody.username,
                password = saltedHash.hash,
                salt = saltedHash.salt
            )

            userDao.addUser(AuthBody(user.username, user.password), user.salt).also {
                call.respond(
                    status = HttpStatusCode.OK,
                    BaseResponse(
                        code = HttpStatusCode.OK.value,
                        message = "Success",
                        data = it
                    )
                )
            }
        }
        post("signin") {
            val authBody = call.receive<AuthBody>()

            val user = userDao.userByUsername(authBody.username) ?: return@post call.respond(
                status = HttpStatusCode.BadRequest,
                BaseResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = "Incorrect Username or Password",
                    data = null
                )
            )

            val isValidPassword = hashingService.verify(
                value = authBody.password,
                saltedHash = SaltedHash(
                    hash = user.password,
                    salt = user.salt
                )
            )

            if (!isValidPassword) return@post call.respond(
                status = HttpStatusCode.BadRequest,
                BaseResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = "Incorrect Username or Password",
                    data = null
                )
            )

            val token = tokenService.generate(
                config = tokenConfig,
                TokenClaim(
                    name = "userId",
                    value = user.id.toString()
                )
            )

            call.respond(
                status = HttpStatusCode.OK,
                BaseResponse(
                    code = HttpStatusCode.OK.value,
                    message = "Success",
                    data = AuthResponse(token)
                )
            )
        }
    }
}