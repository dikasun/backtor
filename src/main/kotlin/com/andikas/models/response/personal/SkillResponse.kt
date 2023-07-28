package com.andikas.models.response.personal

import kotlinx.serialization.Serializable

@Serializable
data class SkillResponse(
    var id: Long,
    var name: String
)