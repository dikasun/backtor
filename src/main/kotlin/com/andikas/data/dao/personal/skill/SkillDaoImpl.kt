package com.andikas.data.dao.personal.skill

import com.andikas.data.DatabaseFactory.dbQuery
import com.andikas.models.entity.personal.ProfileEntity
import com.andikas.models.entity.personal.Profiles
import com.andikas.models.entity.personal.SkillEntity
import com.andikas.models.request.personal.SkillBody
import com.andikas.models.response.personal.SkillResponse
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedCollection

class SkillDaoImpl : SkillDao {
    override suspend fun allSkills(profileId: Long): List<SkillResponse> = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.skills.map { entity -> mapToResponse(entity) }
        } ?: emptyList()
    }

    override suspend fun skill(profileId: Long, id: Long): SkillResponse? = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.skills.find { skill -> skill.id.value == id }?.id?.let { skillId ->
                SkillEntity.findById(skillId)?.let { entity -> mapToResponse(entity) }
            }
        }
    }

    override suspend fun addSkill(profileId: Long, body: SkillBody): Long = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            SizedCollection(
                it.skills + body.let {
                    SkillEntity.new {
                        this.name = body.name
                        this.profileId = EntityID(profileId, Profiles)
                    }
                }
            )
        }
        profileId
    }

    override suspend fun editSkill(profileId: Long, id: Long, body: SkillBody): Boolean = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.skills.find { skill -> skill.id.value == id }?.id?.let { skillId ->
                SkillEntity.findById(skillId)?.let { entity ->
                    entity.name = body.name
                }
            }
        } != null
    }

    override suspend fun deleteSkill(profileId: Long, id: Long): Boolean = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.skills.find { skill -> skill.id.value == id }?.id?.let { skillId ->
                SkillEntity.findById(skillId)?.delete()
            }
        } != null
    }

    private fun mapToResponse(entity: SkillEntity) = SkillResponse(
        id = entity.id.value,
        name = entity.name
    )
}

val skillDao: SkillDao = SkillDaoImpl()