package com.andikas.data.dao.personal.education

import com.andikas.models.request.personal.EducationBody
import com.andikas.models.response.personal.EducationResponse

interface EducationDao {
    suspend fun allEducations(profileId: Long): List<EducationResponse>
    suspend fun education(profileId: Long, id: Long): EducationResponse?
    suspend fun addEducation(profileId: Long, body: EducationBody): Long
    suspend fun editEducation(profileId: Long, id: Long, body: EducationBody): Boolean
    suspend fun deleteEducation(profileId: Long, id: Long): Boolean
}