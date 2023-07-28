package com.andikas.routes.personal

import com.andikas.data.dao.personal.project.projectDao
import com.andikas.models.request.personal.ProjectBody
import com.andikas.models.response.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.projectRouting() {
    route("/projects") {
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
                    data = projectDao.allProjects(profileId)
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
                val projectBody = call.receiveNullable<ProjectBody>() ?: return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Request Body",
                        data = null
                    )
                )

                projectDao.addProject(profileId, projectBody).also {
                    call.respondRedirect("/profiles/$profileId")
                }
            }
        }
        route("{projectId?}") {
            get {
                val profileId = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Profile Id",
                        data = null
                    )
                )
                val projectId = call.parameters["projectId"]?.toLongOrNull() ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Id",
                        data = null
                    )
                )

                projectDao.project(profileId, projectId)?.let {
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
                        message = "No Project with Id $projectId",
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
                    val projectId = call.parameters["projectId"]?.toLongOrNull() ?: return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Id",
                            data = null
                        )
                    )
                    val projectBody = call.receiveNullable<ProjectBody>() ?: return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Request Body",
                            data = null
                        )
                    )

                    projectDao.editProject(profileId, projectId, projectBody).also {
                        if (it) call.respondRedirect("/profiles/$profileId")
                        else call.respond(
                            status = HttpStatusCode.BadRequest,
                            BaseResponse(
                                code = HttpStatusCode.BadRequest.value,
                                message = "No Project with Id $projectId",
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
                    val projectId = call.parameters["projectId"]?.toLongOrNull() ?: return@delete call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Id",
                            data = null
                        )
                    )

                    projectDao.deleteProject(profileId, projectId).also {
                        if (it) call.respondRedirect("/profiles")
                        else call.respond(
                            status = HttpStatusCode.BadRequest,
                            BaseResponse(
                                code = HttpStatusCode.BadRequest.value,
                                message = "No Project with Id $projectId",
                                data = null
                            )
                        )
                    }
                }
            }
        }
    }
}