package com.andikas.data.dao.personal.skill

import com.andikas.models.request.personal.SkillBody
import com.andikas.models.response.personal.SkillResponse

interface SkillDao {
    suspend fun allSkills(profileId: Long): List<SkillResponse>
    suspend fun skill(profileId: Long, id: Long): SkillResponse?
    suspend fun addSkill(profileId: Long, body: SkillBody): Long
    suspend fun editSkill(profileId: Long, id: Long, body: SkillBody): Boolean
    suspend fun deleteSkill(profileId: Long, id: Long): Boolean
}