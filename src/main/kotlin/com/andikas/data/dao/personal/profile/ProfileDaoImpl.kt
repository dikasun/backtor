package com.andikas.data.dao.personal.profile

import com.andikas.data.DatabaseFactory.dbQuery
import com.andikas.models.entity.personal.ProfileEntity
import com.andikas.models.request.personal.ProfileBody
import com.andikas.models.response.personal.*
import org.jetbrains.exposed.dao.with

class ProfileDaoImpl : ProfileDao {
    override suspend fun allProfiles(): List<ProfileResponse> = dbQuery {
        ProfileEntity.all().with(
            ProfileEntity::skills,
            ProfileEntity::experiences,
            ProfileEntity::educations,
            ProfileEntity::projects,
            ProfileEntity::certifications
        ).map { entity -> mapToResponse(entity) }
    }

    override suspend fun profile(id: Long): ProfileResponse? = dbQuery {
        ProfileEntity.findById(id)?.let {
            mapToResponse(it)
        }
    }

    override suspend fun addProfile(body: ProfileBody): Long = dbQuery {
        ProfileEntity.new {
            this.picture = body.picture
            this.name = body.name
            this.role = body.role
            this.description = body.description
        }.id.value
    }

    override suspend fun editProfile(id: Long, body: ProfileBody): Boolean = dbQuery {
        ProfileEntity.findById(id)?.let {
            it.picture = body.picture
            it.name = body.name
            it.role = body.role
            it.description = body.description
        } != null
    }

    override suspend fun deleteProfile(id: Long): Boolean = dbQuery {
        ProfileEntity.findById(id)?.delete() != null
    }

    private fun mapToResponse(entity: ProfileEntity) = ProfileResponse(
        id = entity.id.value,
        picture = entity.picture,
        name = entity.name,
        role = entity.role,
        description = entity.description,
        skills = entity.skills.map { skillEntity ->
            SkillResponse(
                id = skillEntity.id.value,
                name = skillEntity.name
            )
        },
        experiences = entity.experiences.map { expEntity ->
            ExperienceResponse(
                id = expEntity.id.value,
                startYear = expEntity.startYear,
                endYear = expEntity.endYear,
                institution = expEntity.institution,
                role = expEntity.role,
                description = expEntity.description
            )
        },
        educations = entity.educations.map { eduEntity ->
            EducationResponse(
                id = eduEntity.id.value,
                year = eduEntity.year,
                institution = eduEntity.institution,
                role = eduEntity.role,
                description = eduEntity.description
            )
        },
        projects = entity.projects.map { projectEntity ->
            ProjectResponse(
                id = projectEntity.id.value,
                description = projectEntity.description
            )
        },
        certifications = entity.certifications.map { certificationEntity ->
            CertificationResponse(
                id = certificationEntity.id.value,
                url = certificationEntity.url,
                description = certificationEntity.description
            )
        }
    )
}

val profileDao: ProfileDao = ProfileDaoImpl()