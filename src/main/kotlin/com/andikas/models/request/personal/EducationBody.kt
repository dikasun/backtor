package com.andikas.models.request.personal

import kotlinx.serialization.Serializable

@Serializable
data class EducationBody(
    var year: String,
    var institution: String,
    var role: String,
    var description: String
)
