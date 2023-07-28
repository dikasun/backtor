package com.andikas.models.entity.personal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Skills : LongIdTable() {
    val profileId = reference("profile_id", Profiles.id)
    val name = varchar("name", 1024).uniqueIndex()
}

class SkillEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SkillEntity>(Skills)

    var profileId by Skills.profileId
    var name by Skills.name
}