package com.andikas.data.dao.personal.education

import com.andikas.data.DatabaseFactory.dbQuery
import com.andikas.models.entity.personal.EducationEntity
import com.andikas.models.entity.personal.ProfileEntity
import com.andikas.models.entity.personal.Profiles
import com.andikas.models.request.personal.EducationBody
import com.andikas.models.response.personal.EducationResponse
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedCollection

class EducationDaoImpl : EducationDao {
    override suspend fun allEducations(profileId: Long): List<EducationResponse> = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.educations.map { entity -> mapToResponse(entity) }
        } ?: emptyList()
    }

    override suspend fun education(profileId: Long, id: Long): EducationResponse? = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.educations.find { education -> education.id.value == id }?.id?.let { eduId ->
                EducationEntity.findById(eduId)?.let { entity -> mapToResponse(entity) }
            }
        }
    }

    override suspend fun addEducation(profileId: Long, body: EducationBody): Long = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            SizedCollection(
                it.educations + body.let {
                    EducationEntity.new {
                        this.year = body.year
                        this.institution = body.institution
                        this.role = body.role
                        this.description = body.description
                        this.profileId = EntityID(profileId, Profiles)
                    }
                }
            )
        }
        profileId
    }

    override suspend fun editEducation(profileId: Long, id: Long, body: EducationBody): Boolean = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.educations.find { education -> education.id.value == id }?.id?.let { eduId ->
                EducationEntity.findById(eduId)?.let { entity ->
                    entity.year = body.year
                    entity.institution = body.institution
                    entity.role = body.role
                    entity.description = body.description
                }
            }
        } != null
    }

    override suspend fun deleteEducation(profileId: Long, id: Long): Boolean = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.educations.find { education -> education.id.value == id }?.id?.let { eduId ->
                EducationEntity.findById(eduId)?.delete()
            }
        } != null
    }

    private fun mapToResponse(entity: EducationEntity) = EducationResponse(
        id = entity.id.value,
        year = entity.year,
        institution = entity.institution,
        role = entity.role,
        description = entity.description
    )
}

val educationDao: EducationDao = EducationDaoImpl()