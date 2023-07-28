package com.andikas.models.request.personal

import kotlinx.serialization.Serializable

@Serializable
data class CertificationBody(
    var url: String,
    var description: String
)
