package com.andikas.routes.auth

import com.andikas.data.dao.auth.userDao
import com.andikas.models.request.auth.AuthBody
import com.andikas.models.response.BaseResponse
import com.andikas.models.response.auth.UserResponse
import com.andikas.security.hashing.service.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting(hashingService: HashingService) {
    route("/users") {
        get {
            call.respond(
                status = HttpStatusCode.OK,
                BaseResponse(
                    code = HttpStatusCode.OK.value,
                    message = "Success",
                    data = userDao.allUsers()
                )
            )
        }
        route("{id?}") {
            get {
                val userId = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Profile Id",
                        data = null
                    )
                )

                userDao.user(userId)?.let {
                    call.respond(
                        status = HttpStatusCode.OK,
                        BaseResponse(
                            code = HttpStatusCode.OK.value,
                            message = "Success",
                            data = it
                        )
                    )
                } ?: call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "No User with Id $userId",
                        data = null
                    )
                )
            }
            put {
                val userId = call.parameters["id"]?.toLongOrNull() ?: return@put call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Profile Id",
                        data = null
                    )
                )
                val authBody = call.receiveNullable<AuthBody>() ?: return@put call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Request Body",
                        data = null
                    )
                )

                val areFieldsBlank = authBody.username.isBlank() || authBody.password.isBlank()
                val isPwTooShort = authBody.password.length < 8
                if (areFieldsBlank || isPwTooShort) return@put call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Invalid Username or Password format",
                        data = null
                    )
                )

                val isUsernameTaken = userDao.userByUsername(authBody.username)
                if (isUsernameTaken != null) return@put call.respond(
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

                userDao.editUser(userId, AuthBody(user.username, user.password), user.salt).let {
                    if (it) call.respondRedirect("/users/$userId")
                    else call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "No User with Id $userId",
                            data = null
                        )
                    )
                }
            }
            delete {
                val userId = call.parameters["id"]?.toLongOrNull() ?: return@delete call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Profile Id",
                        data = null
                    )
                )

                userDao.deleteUser(userId).also {
                    if (it) call.respondRedirect("/users")
                    else call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "No User with Id $userId",
                            data = null
                        )
                    )
                }
            }
        }
    }
}