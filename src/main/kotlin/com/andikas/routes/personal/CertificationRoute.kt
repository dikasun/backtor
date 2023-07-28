package com.andikas.routes.personal

import com.andikas.data.dao.personal.certification.certificationDao
import com.andikas.models.request.personal.CertificationBody
import com.andikas.models.response.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.certificationRouting() {
    route("/certifications") {
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
                    data = certificationDao.allCertifications(profileId)
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
                val certificationBody = call.receiveNullable<CertificationBody>() ?: return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Request Body",
                        data = null
                    )
                )

                certificationDao.addCertification(profileId, certificationBody).also {
                    call.respondRedirect("/profiles/$profileId")
                }
            }
        }
        route("{certificationId?}") {
            get {
                val profileId = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Profile Id",
                        data = null
                    )
                )
                val certificationId = call.parameters["certificationId"]?.toLongOrNull() ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Id",
                        data = null
                    )
                )

                certificationDao.certification(profileId, certificationId)?.let {
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
                        message = "No Certification with Id $certificationId",
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
                    val certificationId = call.parameters["certificationId"]?.toLongOrNull() ?: return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Id",
                            data = null
                        )
                    )
                    val certificationBody = call.receiveNullable<CertificationBody>() ?: return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Request Body",
                            data = null
                        )
                    )

                    certificationDao.editCertification(profileId, certificationId, certificationBody).also {
                        if (it) call.respondRedirect("/profiles/$profileId")
                        else call.respond(
                            status = HttpStatusCode.BadRequest,
                            BaseResponse(
                                code = HttpStatusCode.BadRequest.value,
                                message = "No Certification with Id $certificationId",
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
                    val certificationId =
                        call.parameters["certificationId"]?.toLongOrNull() ?: return@delete call.respond(
                            status = HttpStatusCode.BadRequest,
                            BaseResponse(
                                code = HttpStatusCode.BadRequest.value,
                                message = "Missing Id",
                                data = null
                            )
                        )

                    certificationDao.deleteCertification(profileId, certificationId).also {
                        if (it) call.respondRedirect("/profiles")
                        else call.respond(
                            status = HttpStatusCode.BadRequest,
                            BaseResponse(
                                code = HttpStatusCode.BadRequest.value,
                                message = "No Certification with Id $certificationId",
                                data = null
                            )
                        )
                    }
                }
            }
        }
    }
}