package com.andikas.routes

import com.andikas.models.response.BaseResponse
import com.andikas.models.response.UploadResponse
import io.imagekit.sdk.ImageKit
import io.imagekit.sdk.models.FileCreateRequest
import io.imagekit.sdk.models.GetFileListRequest
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.fileRouting(imageKit: ImageKit) {
    route("files") {
        get {
            val getFileListRequest = GetFileListRequest().apply {
                setPath("andikas")
            }
            val resultList = imageKit.getFileList(getFileListRequest).let {
                it.results.map { result -> UploadResponse(result.fileId, result.name, result.url) }
            }

            call.respond(
                status = HttpStatusCode.OK,
                BaseResponse(
                    code = HttpStatusCode.OK.value,
                    message = "Success",
                    data = resultList
                )
            )
        }
        post("upload") {
            var fileName: String
            val multipartData = call.receiveMultipart()
            val responseList = mutableListOf<UploadResponse>()

            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        fileName = part.originalFileName as String
                        val baseFolder = "/andikas"
                        val fileBytes = part.streamProvider().readBytes()
                        val fileCreateRequest = FileCreateRequest(fileBytes, fileName).apply {
                            overwriteFile = true
                            isUseUniqueFileName = false
                            folder = when (part.name) {
                                "certification" -> "$baseFolder/certification"
                                else -> baseFolder
                            }
                        }

                        imageKit.upload(fileCreateRequest).also {
                            responseList.add(UploadResponse(it.fileId, it.name, it.url))
                        }
                    }

                    else -> Unit
                }
                part.dispose()
            }

            call.respond(
                status = HttpStatusCode.OK,
                BaseResponse(
                    code = HttpStatusCode.OK.value,
                    message = "Success",
                    data = responseList
                )
            )
        }
        route("{id?}") {
            get {
                val id = call.parameters["id"] ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing File Id",
                        data = null
                    )
                )
                val result = imageKit.getFileDetail(id).let {
                    UploadResponse(it.fileId, it.name, it.url)
                }

                call.respond(
                    status = HttpStatusCode.OK,
                    BaseResponse(
                        code = HttpStatusCode.OK.value,
                        message = "Success",
                        data = result
                    )
                )
            }
            delete {
                val id = call.parameters["id"] ?: return@delete call.respond(
                    status = HttpStatusCode.BadRequest,
                    BaseResponse(
                        code = HttpStatusCode.BadRequest.value,
                        message = "Missing File Id",
                        data = null
                    )
                )
                imageKit.deleteFile(id).also { call.respondRedirect("/files") }
            }
        }
    }
}