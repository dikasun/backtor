package com.andikas.models.response

import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(
    val id: String,
    val filename: String,
    val url: String
)
