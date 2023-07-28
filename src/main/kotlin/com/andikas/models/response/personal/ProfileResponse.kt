package com.andikas.models.response.personal

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    var id: Long,
    var picture: String,
    var name: String,
    var role: String,
    var description: String,
    var skills: List<SkillResponse>,
    var experiences: List<ExperienceResponse>,
    var educations: List<EducationResponse>,
    var projects: List<ProjectResponse>,
    var certifications: List<CertificationResponse>
)
