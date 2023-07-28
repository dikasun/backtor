package com.andikas.routes.personal

import com.andikas.data.dao.personal.experience.experienceDao
import com.andikas.models.request.personal.ExperienceBody
import com.andikas.models.response.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.experienceRouting() {
    route("/experiences") {
        get {
            val profileId = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(
                status = HttpStatusCode.BadRequest,
                BaseResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = "Missing Profile Id",
                    data = null
                )
            )

            call.respond(
                status = HttpStatusCode.OK,
                BaseResponse(
                    code = HttpStatusCode.OK.value,
                    message = "Success",
                    data = experienceDao.allExperiences(profileId)
                )
            )
        }
        authenticate("auth-jwt") {
            post {
                val profileId = call.parameters["id"]?.toLongOrNull() ?: return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Profile Id",
                        data = null
                    )
                )
                val expBody = call.receiveNullable<ExperienceBody>() ?: return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Request Body",
                        data = null
                    )
                )

                experienceDao.addExperience(profileId, expBody).also {
                    call.respondRedirect("/profiles/$profileId")
                }
            }
        }
        route("{expId?}") {
            get {
                val profileId = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Profile Id",
                        data = null
                    )
                )
                val expId = call.parameters["expId"]?.toLongOrNull() ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Id",
                        data = null
                    )
                )

                experienceDao.experience(profileId, expId)?.let {
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
                        message = "No Experience with Id $expId",
                        data = null
                    )
                )
            }
            authenticate("auth-jwt") {
                put {
                    val profileId = call.parameters["id"]?.toLongOrNull() ?: return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Profile Id",
                            data = null
                        )
                    )
                    val expId = call.parameters["expId"]?.toLongOrNull() ?: return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Id",
                            data = null
                        )
                    )
                    val experienceBody = call.receiveNullable<ExperienceBody>() ?: return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Request Body",
                            data = null
                        )
                    )

                    experienceDao.editExperience(profileId, expId, experienceBody).also {
                        if (it) call.respondRedirect("/profiles/$profileId")
                        else call.respond(
                            status = HttpStatusCode.BadRequest,
                            BaseResponse(
                                code = HttpStatusCode.BadRequest.value,
                                message = "No Experience with Id $expId",
                                data = null
                            )
                        )
                    }
                }
                delete {
                    val profileId = call.parameters["id"]?.toLongOrNull() ?: return@delete call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Profile Id",
                            data = null
                        )
                    )
                    val expId = call.parameters["expId"]?.toLongOrNull() ?: return@delete call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Id",
                            data = null
                        )
                    )

                    experienceDao.deleteExperience(profileId, expId).also {
                        if (it) call.respondRedirect("/profiles")
                        else call.respond(
                            status = HttpStatusCode.BadRequest,
                            BaseResponse(
                                code = HttpStatusCode.BadRequest.value,
                                message = "No Experience with Id $expId",
                                data = null
                            )
                        )
                    }
                }
            }
        }
    }
}