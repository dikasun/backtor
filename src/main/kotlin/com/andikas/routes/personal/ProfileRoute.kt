package com.andikas.routes.personal

import com.andikas.data.dao.personal.profile.profileDao
import com.andikas.models.request.personal.ProfileBody
import com.andikas.models.response.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.profileRouting() {
    route("/profiles") {
        get {
            call.respond(
                status = HttpStatusCode.OK,
                BaseResponse(
                    code = HttpStatusCode.OK.value,
                    message = "Success",
                    data = profileDao.allProfiles()
                )
            )
        }
        authenticate("auth-jwt") {
            post {
                val profileBody = call.receive<ProfileBody>()
                val newProfile = profileDao.addProfile(profileBody)

                call.respondRedirect("/profiles/$newProfile")
            }
        }
        route("{id?}") {
            get {
                val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Id",
                        data = null
                    )
                )

                profileDao.profile(id)?.let {
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
                        message = "No Profile with Id $id",
                        data = null
                    )
                )
            }
            authenticate("auth-jwt") {
                put {
                    val id = call.parameters["id"]?.toLongOrNull() ?: return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Id",
                            data = null
                        )
                    )
                    val profileBody = call.receive<ProfileBody>()

                    profileDao.editProfile(id, profileBody).also {
                        if (it) call.respondRedirect("/profiles/$id")
                        else call.respond(
                            status = HttpStatusCode.BadRequest,
                            BaseResponse(
                                code = HttpStatusCode.BadRequest.value,
                                message = "No Profile with Id $id",
                                data = null
                            )
                        )
                    }
                }
                delete {
                    val id = call.parameters["id"]?.toLongOrNull() ?: return@delete call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Id",
                            data = null
                        )
                    )

                    profileDao.deleteProfile(id).also {
                        if (it) call.respondRedirect("/profiles")
                        else call.respond(
                            status = HttpStatusCode.BadRequest,
                            BaseResponse(
                                code = HttpStatusCode.BadRequest.value,
                                message = "No Profile with Id $id",
                                data = null
                            )
                        )
                    }
                }
            }
            skillRouting()
            projectRouting()
            experienceRouting()
            educationRouting()
            certificationRouting()
        }
    }
}