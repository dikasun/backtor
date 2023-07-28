package com.andikas.data.dao.personal.experience

import com.andikas.models.request.personal.ExperienceBody
import com.andikas.models.response.personal.ExperienceResponse

interface ExperienceDao {
    suspend fun allExperiences(profileId: Long): List<ExperienceResponse>
    suspend fun experience(profileId: Long, id: Long): ExperienceResponse?
    suspend fun addExperience(profileId: Long, body: ExperienceBody): Long
    suspend fun editExperience(profileId: Long, id: Long, body: ExperienceBody): Boolean
    suspend fun deleteExperience(profileId: Long, id: Long): Boolean
}