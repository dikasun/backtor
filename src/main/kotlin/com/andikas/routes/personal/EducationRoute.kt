package com.andikas.routes.personal

import com.andikas.data.dao.personal.education.educationDao
import com.andikas.models.request.personal.EducationBody
import com.andikas.models.response.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.educationRouting() {
    route("/educations") {
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
                    data = educationDao.allEducations(profileId)
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
                val eduBody = call.receiveNullable<EducationBody>() ?: return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Request Body",
                        data = null
                    )
                )

                educationDao.addEducation(profileId, eduBody).also {
                    call.respondRedirect("/profiles/$profileId")
                }
            }
        }
        route("{eduId?}") {
            get {
                val profileId = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Profile Id",
                        data = null
                    )
                )
                val eduId = call.parameters["eduId"]?.toLongOrNull() ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Id",
                        data = null
                    )
                )

                educationDao.education(profileId, eduId)?.let {
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
                        message = "No Education with Id $eduId",
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
                    val eduId = call.parameters["eduId"]?.toLongOrNull() ?: return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Id",
                            data = null
                        )
                    )
                    val educationBody = call.receiveNullable<EducationBody>() ?: return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Request Body",
                            data = null
                        )
                    )

                    educationDao.editEducation(profileId, eduId, educationBody).also {
                        if (it) call.respondRedirect("/profiles/$profileId")
                        else call.respond(
                            status = HttpStatusCode.BadRequest,
                            BaseResponse(
                                code = HttpStatusCode.BadRequest.value,
                                message = "No Education with Id $eduId",
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
                    val eduId = call.parameters["eduId"]?.toLongOrNull() ?: return@delete call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Id",
                            data = null
                        )
                    )

                    educationDao.deleteEducation(profileId, eduId).also {
                        if (it) call.respondRedirect("/profiles")
                        else call.respond(
                            status = HttpStatusCode.BadRequest,
                            BaseResponse(
                                code = HttpStatusCode.BadRequest.value,
                                message = "No Education with Id $eduId",
                                data = null
                            )
                        )
                    }
                }
            }
        }
    }
}