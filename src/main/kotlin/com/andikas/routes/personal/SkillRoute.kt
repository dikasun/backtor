package com.andikas.routes.personal

import com.andikas.data.dao.personal.skill.skillDao
import com.andikas.models.request.personal.SkillBody
import com.andikas.models.response.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.skillRouting() {
    route("/skills") {
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
                    data = skillDao.allSkills(profileId)
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
                val skillBody = call.receiveNullable<SkillBody>() ?: return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Request Body",
                        data = null
                    )
                )

                skillDao.addSkill(profileId, skillBody).also {
                    call.respondRedirect("/profiles/$profileId")
                }
            }
        }
        route("{skillId?}") {
            get {
                val profileId = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Profile Id",
                        data = null
                    )
                )
                val skillId = call.parameters["skillId"]?.toLongOrNull() ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing Id",
                        data = null
                    )
                )

                skillDao.skill(profileId, skillId)?.let {
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
                        message = "No Skill with Id $skillId",
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
                    val skillId = call.parameters["skillId"]?.toLongOrNull() ?: return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Id",
                            data = null
                        )
                    )
                    val skillBody = call.receiveNullable<SkillBody>() ?: return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Request Body",
                            data = null
                        )
                    )

                    skillDao.editSkill(profileId, skillId, skillBody).also {
                        if (it) call.respondRedirect("/profiles/$profileId")
                        else call.respond(
                            status = HttpStatusCode.BadRequest,
                            BaseResponse(
                                code = HttpStatusCode.BadRequest.value,
                                message = "No Skill with Id $skillId",
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
                    val skillId = call.parameters["skillId"]?.toLongOrNull() ?: return@delete call.respond(
                        status = HttpStatusCode.BadRequest,
                        BaseResponse(
                            code = HttpStatusCode.BadRequest.value,
                            message = "Missing Id",
                            data = null
                        )
                    )

                    skillDao.deleteSkill(profileId, skillId).also {
                        if (it) call.respondRedirect("/profiles")
                        else call.respond(
                            status = HttpStatusCode.BadRequest,
                            BaseResponse(
                                code = HttpStatusCode.BadRequest.value,
                                message = "No Skill with Id $skillId",
                                data = null
                            )
                        )
                    }
                }
            }
        }
    }
}