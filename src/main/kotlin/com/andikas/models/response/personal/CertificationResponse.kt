package com.andikas.models.response.personal

import kotlinx.serialization.Serializable

@Serializable
data class CertificationResponse(
    var id: Long,
    var url: String,
    var description: String
)
