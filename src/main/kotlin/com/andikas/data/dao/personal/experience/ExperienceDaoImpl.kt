package com.andikas.data.dao.personal.experience

import com.andikas.data.DatabaseFactory.dbQuery
import com.andikas.models.entity.personal.ExperienceEntity
import com.andikas.models.entity.personal.ProfileEntity
import com.andikas.models.entity.personal.Profiles
import com.andikas.models.request.personal.ExperienceBody
import com.andikas.models.response.personal.ExperienceResponse
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedCollection

class ExperienceDaoImpl : ExperienceDao {
    override suspend fun allExperiences(profileId: Long): List<ExperienceResponse> = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.experiences.map { entity -> mapToResponse(entity) }
        } ?: emptyList()
    }

    override suspend fun experience(profileId: Long, id: Long): ExperienceResponse? = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.experiences.find { experience -> experience.id.value == id }?.id?.let { expId ->
                ExperienceEntity.findById(expId)?.let { entity -> mapToResponse(entity) }
            }
        }
    }

    override suspend fun addExperience(profileId: Long, body: ExperienceBody): Long = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            SizedCollection(
                it.experiences + body.let {
                    ExperienceEntity.new {
                        this.startYear = body.startYear
                        this.endYear = body.endYear
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

    override suspend fun editExperience(profileId: Long, id: Long, body: ExperienceBody): Boolean = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.experiences.find { experience -> experience.id.value == id }?.id?.let { expId ->
                ExperienceEntity.findById(expId)?.let { entity ->
                    entity.startYear = body.startYear
                    entity.endYear = body.endYear
                    entity.institution = body.institution
                    entity.role = body.role
                    entity.description = body.description
                }
            }
        } != null
    }

    override suspend fun deleteExperience(profileId: Long, id: Long): Boolean = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.experiences.find { experience -> experience.id.value == id }?.id?.let { expId ->
                ExperienceEntity.findById(expId)?.delete()
            }
        } != null
    }

    private fun mapToResponse(entity: ExperienceEntity) = ExperienceResponse(
        id = entity.id.value,
        startYear = entity.startYear,
        endYear = entity.endYear,
        institution = entity.institution,
        role = entity.role,
        description = entity.description
    )
}

val experienceDao: ExperienceDao = ExperienceDaoImpl()